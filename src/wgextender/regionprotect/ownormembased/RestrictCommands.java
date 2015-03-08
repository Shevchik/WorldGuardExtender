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

package wgextender.regionprotect.ownormembased;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class RestrictCommands implements Listener {

	private Config config;

	public RestrictCommands(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ProcessWGCommand(PlayerCommandPreprocessEvent event) {
		if (!config.restrictcommandsinregionsenabled) {
			return;
		}
		Player player = event.getPlayer();
		if (WGRegionUtils.canBypassProtection(player)) {
			return;
		}
		if (WGRegionUtils.isInWGRegion(player.getLocation()) && !WGRegionUtils.canBuild(player, player.getLocation())) {
			String message = event.getMessage();
			message = message.replaceFirst("/", "").toLowerCase();
			for (String rcommand : config.restrictedcommands) {
				if (message.startsWith(rcommand)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду на чужом регионе");
					return;
				}
			}
		}
	}

}
