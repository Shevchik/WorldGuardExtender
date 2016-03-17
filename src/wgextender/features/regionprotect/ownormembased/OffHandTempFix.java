package wgextender.features.regionprotect.ownormembased;

import org.bukkit.ChatColor;s
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import wgextender.utils.WGRegionUtils;

//TODO: Remove when worldguard fixes it
public class OffHandTempFix implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (event.getHand() != EquipmentSlot.OFF_HAND) {
			return;
		}
		if (event.getItem() == null) {
			return;
		}
		if (
			!WGRegionUtils.canBypassProtection(event.getPlayer()) &&
			!WGRegionUtils.canBuild(event.getPlayer(), event.getClickedBlock().getLocation())
		) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Sorry, you can't place things there");
		}
	}

}
