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

package WGExtender.wgcommandprocess;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import WGExtender.Config;
import WGExtender.WGExtender;

public class WGCommandProcess implements Listener {

	private WGExtender main;
	private Config config;

	public WGCommandProcess(WGExtender main, Config config) {
		this.main = main;
		this.config = config;
	}

	private HashSet<String> wgcmds = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"/rg",
				"/region"
			}
		)
	);

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void processWGCommand(final PlayerCommandPreprocessEvent event) {
		final String[] cmds = event.getMessage().toLowerCase().split("\\s+");
		if (cmds.length < 3) {
			return;
		}
		if (!wgcmds.contains(cmds[0])) {
			return;
		}
		if (!cmds[1].equals("claim")) {
			return;
		}
		if (config.expandvert) {
			boolean result = VertExpand.expand(main.getWorldEdit(), event.getPlayer());
			if (result) {
				event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Регион автоматически расширен по вертикали");
			}
		}
		if (config.blocklimitsenabled) {
			if (!BlockLimits.allowClaim(config, main.getWorldEdit(), main.getWorldGuard(), event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.RED + "Вы не можете заприватить такой большой регион");
				event.setCancelled(true);
				return;
			}
		}
		if (config.autoflagsenabled) {
			AutoFlags.setFlagsForRegion(main, config, main.getWorldGuard(), event.getPlayer().getWorld(), cmds[2]);
		}
	}

}
