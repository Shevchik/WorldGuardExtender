package wgextender.features.regionprotect.ownormembased;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import wgextender.features.flags.ChorusFruitUseFlag;
import wgextender.utils.WGRegionUtils;

public class ChorusFruitFlagHandler implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onItemUse(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() == Material.CHORUS_FRUIT) {
			Player player = event.getPlayer();
			if (
				!WGRegionUtils.canBypassProtection(event.getPlayer()) &&
				!WGRegionUtils.isFlagAllows(player, player.getLocation(), ChorusFruitUseFlag.getInstance())
			) {
				player.sendMessage(ChatColor.RED + "Вы не можете использовать фрукт телепортации в этом регионе");
				event.setCancelled(true);
			}
		}
	}

}
