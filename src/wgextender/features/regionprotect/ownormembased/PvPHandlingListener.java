package wgextender.features.regionprotect.ownormembased;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.cause.Cause;
import com.sk89q.worldguard.bukkit.event.DelegateEvent;
import com.sk89q.worldguard.bukkit.event.entity.DamageEntityEvent;
import com.sk89q.worldguard.bukkit.internal.WGMetadata;
import com.sk89q.worldguard.bukkit.listener.RegionProtectionListener;
import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.bukkit.util.Entities;
import com.sk89q.worldguard.bukkit.util.Events;
import com.sk89q.worldguard.bukkit.util.InteropUtils;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.internal.permission.RegionPermissionModel;
import com.sk89q.worldguard.protection.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import wgextender.Config;
import wgextender.features.regionprotect.WGOverrideListener;
import wgextender.utils.WEUtils;
import wgextender.utils.WGRegionUtils;

public class PvPHandlingListener extends WGOverrideListener {

	protected final Config config;

	public PvPHandlingListener(Config config) {
		this.config = config;
	}

	@Override
	protected Class<? extends Listener> getClassToReplace() {
		return RegionProtectionListener.class;
	}

	@EventHandler(ignoreCancelled = true)
	public void onDamageEntity(DamageEntityEvent event) {
		if (event.getResult() == Result.ALLOW) {
			return;
		}
		if (!WEUtils.getWorldConfig(event.getWorld()).useRegions) {
			return;
		}

		Location target = event.getTarget();
		RegionAssociable associable = createRegionAssociable(event.getCause());

		Player playerAttacker = event.getCause().getFirstPlayer();
		if (playerAttacker == null) {
			return;
		}

		LocalPlayer localPlayerAttacker = WorldGuardPlugin.inst().wrapPlayer(playerAttacker);
		com.sk89q.worldedit.util.Location weTarget = BukkitAdapter.adapt(target);
		com.sk89q.worldedit.util.Location weAttacker = BukkitAdapter.adapt(playerAttacker.getLocation());

		// Block PvP like normal even if the player has an override permission
		// because (1) this is a frequent source of confusion and
		// (2) some users want to block PvP even with the bypass permission
		boolean pvp = (event.getEntity() instanceof Player) && (playerAttacker != null) && !playerAttacker.equals(event.getEntity());
		if (isWhitelisted(event.getCause(), event.getWorld(), pvp)) {
			return;
		}

		RegionQuery query = WGRegionUtils.REGION_QUERY;

		boolean canDamage;
		String what;

		/* Hostile / ambient mob override */
		if (
			Entities.isHostile(event.getEntity()) ||
			Entities.isAmbient(event.getEntity()) ||
			Entities.isVehicle(event.getEntity().getType())
		) {
			canDamage = event.getRelevantFlags().isEmpty() || (query.queryState(weTarget, associable, combine(event)) != State.DENY);
			what = "hit that";

			/* Paintings, item frames, etc. */
		} else if (Entities.isConsideredBuildingIfUsed(event.getEntity())) {
			canDamage = query.testBuild(weTarget, associable, combine(event));
			what = "change that";

			/* PVP */
		} else if (pvp) {
			Player defender = (Player) event.getEntity();

			// add possibility to change how pvp none flag works
			// null - default wg pvp logic
			// true - allow pvp when flag not set
			// false - disallow pvp when flag not set
			if (config.miscDefaultPvPFlagOperationMode == null) {
				canDamage =
					query.testBuild(weTarget, associable, combine(event, Flags.PVP)) &&
					(query.queryState(weAttacker, localPlayerAttacker, combine(event, Flags.PVP)) != State.DENY) &&
					(query.queryState(weTarget, localPlayerAttacker, combine(event, Flags.PVP)) != State.DENY);
			} else if (config.miscDefaultPvPFlagOperationMode) {
				canDamage =
					(query.queryState(weAttacker, localPlayerAttacker, combine(event, Flags.PVP)) != State.DENY) &&
					(query.queryState(weTarget, localPlayerAttacker, combine(event, Flags.PVP)) != State.DENY);
			} else {
				if (!WGRegionUtils.isInWGRegion(playerAttacker.getLocation()) && !WGRegionUtils.isInWGRegion(target)) {
					canDamage = true;
				} else {
					canDamage = (query.queryState(weAttacker, localPlayerAttacker, combine(event, Flags.PVP)) == State.ALLOW) && (query.queryState(weTarget, localPlayerAttacker, combine(event, Flags.PVP)) == State.ALLOW);
				}

			}

			// Fire the disallow PVP event
			if (!canDamage && Events.fireAndTestCancel(new DisallowedPVPEvent(playerAttacker, defender, event.getOriginalEvent()))) {
				canDamage = true;
			}

			what = "PvP";

			/* Player damage not caused by another player */
		} else if (event.getEntity() instanceof Player) {
			canDamage = event.getRelevantFlags().isEmpty() || (query.queryState(weTarget, associable, combine(event)) != State.DENY);
			what = "damage that";

			/* damage to non-hostile mobs (e.g. animals) */
		} else if (Entities.isNonHostile(event.getEntity())) {
			canDamage = query.testBuild(weTarget, associable, combine(event, Flags.DAMAGE_ANIMALS));
			what = "harm that";

			/* Everything else */
		} else {
			canDamage = query.testBuild(weTarget, associable, combine(event, Flags.INTERACT));
			what = "hit that";
		}

		if (!canDamage) {
			tellErrorMessage(event, event.getCause(), target, what);
			event.setCancelled(true);
		}
	}

	private RegionAssociable createRegionAssociable(Cause cause) {
		Object rootCause = cause.getRootCause();

		if (!cause.isKnown()) {
			return Associables.constant(Association.NON_MEMBER);
		} else if (rootCause instanceof Player) {
			return WorldGuardPlugin.inst().wrapPlayer((Player) rootCause);
		} else if (rootCause instanceof OfflinePlayer) {
			return WorldGuardPlugin.inst().wrapOfflinePlayer((OfflinePlayer) rootCause);
		} else if (rootCause instanceof Entity) {
			return new DelayedRegionOverlapAssociation(WGRegionUtils.REGION_QUERY, BukkitAdapter.adapt(((Entity) rootCause).getLocation()));
		} else if (rootCause instanceof Block) {
			return new DelayedRegionOverlapAssociation(WGRegionUtils.REGION_QUERY, BukkitAdapter.adapt(((Block) rootCause).getLocation()));
		} else {
			return Associables.constant(Association.NON_MEMBER);
		}
	}

	private boolean isWhitelisted(Cause cause, World world, boolean pvp) {
		Object rootCause = cause.getRootCause();

		if (rootCause instanceof Block) {
			Material type = ((Block) rootCause).getType();
			return (type == Material.HOPPER) || (type == Material.DROPPER);
		} else if (rootCause instanceof Player) {
			Player player = (Player) rootCause;

			if (WEUtils.getWorldConfig(world).fakePlayerBuildOverride && InteropUtils.isFakePlayer(player)) {
				return true;
			}

			return !pvp && new RegionPermissionModel(WorldGuardPlugin.inst().wrapPlayer(player)).mayIgnoreRegionProtection(BukkitAdapter.adapt(world));
		} else {
			return false;
		}
	}

	private static final String DENY_MESSAGE_KEY = "worldguard.region.lastMessage";
	private static final int LAST_MESSAGE_DELAY = 500;

	private void tellErrorMessage(DelegateEvent event, Cause cause, Location location, String what) {
		if (event.isSilent() || cause.isIndirect()) {
			return;
		}

		Object rootCause = cause.getRootCause();

		if (rootCause instanceof Player) {
			Player player = (Player) rootCause;

			long now = System.currentTimeMillis();
			Long lastTime = WGMetadata.getIfPresent(player, DENY_MESSAGE_KEY, Long.class);
			if ((lastTime == null) || ((now - lastTime) >= LAST_MESSAGE_DELAY)) {
				String message = WGRegionUtils.REGION_QUERY.queryValue(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), Flags.DENY_MESSAGE);
				if ((message != null) && !message.isEmpty()) {
					player.sendMessage(message.replace("%what%", what));
				}
				WGMetadata.put(player, DENY_MESSAGE_KEY, now);
			}
		}
	}

	private static StateFlag[] combine(DelegateEvent event, StateFlag... flag) {
		List<StateFlag> extra = event.getRelevantFlags();
		StateFlag[] flags = Arrays.copyOf(flag, flag.length + extra.size());
		for (int i = 0; i < extra.size(); i++) {
			flags[flag.length + i] = extra.get(i);
		}
		return flags;
	}

}
