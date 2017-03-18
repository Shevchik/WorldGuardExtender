package wgextender.features.custom;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import wgextender.WGExtender;
import wgextender.features.flags.OldPVPAttackSpeed;
import wgextender.utils.WGRegionUtils;

public class OldPVPFlagsHandler implements Listener {

	private final HashMap<UUID, Double> oldValues = new HashMap<>();

	public void start() {
		Bukkit.getPluginManager().registerEvents(this, WGExtender.getInstance());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(WGExtender.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (WGRegionUtils.isFlagTrue(player, player.getLocation(), OldPVPAttackSpeed.getInstance())) {
						if (!oldValues.containsKey(player.getUniqueId())) {
							AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
							oldValues.put(player.getUniqueId(), attribute.getBaseValue());
							attribute.setBaseValue(16.0);
						}
					} else {
						reset(player);
					}
				}
			}
		}, 0, 1);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event) {
		reset(event.getPlayer());
	}

	private void reset(Player player) {
		Double oldValue = oldValues.remove(player.getUniqueId());
		if (oldValue != null) {
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(oldValue);
		}
	}

}
