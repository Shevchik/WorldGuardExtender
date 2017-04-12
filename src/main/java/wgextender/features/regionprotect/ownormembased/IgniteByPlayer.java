/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package wgextender.features.regionprotect.ownormembased;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class IgniteByPlayer implements Listener {

	private Config config;

	public IgniteByPlayer(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerIgnitedBlock(BlockIgniteEvent event) {
		if (!config.blockigniteotherregionbyplayer) {
			return;
		}
		Player player = event.getPlayer();
		if (player != null) {
			if (
				!WGRegionUtils.canBypassProtection(player) &&
				!WGRegionUtils.canBuild(player, event.getBlock().getLocation())
			) {
				player.sendMessage(ChatColor.RED + "Вы не можете поджечь блок в чужом регионе");
				event.setCancelled(true);
			}
		} else if (event.getCause() == IgniteCause.FIREBALL) {
			if (WGRegionUtils.isInWGRegion(event.getBlock().getLocation())) {
				event.setCancelled(true);
			}
		}
	}

}
