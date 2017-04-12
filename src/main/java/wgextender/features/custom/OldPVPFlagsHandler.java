package wgextender.features.custom;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.google.common.base.Function;

import wgextender.WGExtender;
import wgextender.features.flags.OldPVPAttackSpeedFlag;
import wgextender.features.flags.OldPVPNoBowFlag;
import wgextender.features.flags.OldPVPNoShieldBlockFlag;
import wgextender.utils.ReflectionUtils;
import wgextender.utils.WGRegionUtils;

public class OldPVPFlagsHandler implements Listener {

	private final HashMap<UUID, Double> oldValues = new HashMap<>();
	private Field functionsField;

	public void start() throws NoSuchFieldException, SecurityException {
		functionsField = ReflectionUtils.getField(EntityDamageEvent.class, "modifierFunctions");
		Bukkit.getPluginManager().registerEvents(this, WGExtender.getInstance());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(WGExtender.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (WGRegionUtils.isFlagTrue(player.getLocation(), OldPVPAttackSpeedFlag.getInstance())) {
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

	public void stop() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			reset(player);
		}
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

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (player.isBlocking() && WGRegionUtils.isFlagTrue(entity.getLocation(), OldPVPNoShieldBlockFlag.getInstance())) {
				try {
					Map<DamageModifier, Function<Double, Double>> func = (Map<DamageModifier, Function<Double, Double>>) functionsField.get(event);
					double damage = event.getDamage() + event.getDamage(DamageModifier.HARD_HAT);
					//reset blocking modifier
					event.setDamage(DamageModifier.BLOCKING, 0);
					//recalculate other modifiers
					double armorModifier = func.get(DamageModifier.ARMOR).apply(damage);
					event.setDamage(DamageModifier.ARMOR, armorModifier);
					damage += armorModifier;
					double resModifier = func.get(DamageModifier.RESISTANCE).apply(damage);
					event.setDamage(DamageModifier.RESISTANCE, resModifier);
					damage += resModifier;
					double magicModifier = func.get(DamageModifier.MAGIC).apply(damage);
					event.setDamage(DamageModifier.MAGIC, magicModifier);
					damage += magicModifier;
					double absorbtionModifier = func.get(DamageModifier.ABSORPTION).apply(damage);
					event.setDamage(DamageModifier.ABSORPTION, absorbtionModifier);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					WGExtender.getInstance().getLogger().log(Level.SEVERE, "Unable to recalculate blocking damage", e);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND && event.getPlayer().getInventory().getItemInOffHand().getType() == Material.BOW) {
			if (WGRegionUtils.isFlagTrue(event.getPlayer().getLocation(), OldPVPNoBowFlag.getInstance())) {
				event.setCancelled(true);
			}
		}
	}

}
