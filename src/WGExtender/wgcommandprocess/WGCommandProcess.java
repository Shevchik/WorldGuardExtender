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
import WGExtender.wgcommandprocess.BlockLimits.ProcessedClaimInfo;
import WGExtender.wgcommandprocess.vertexpand.VertExpand;

public class WGCommandProcess implements Listener {

	private Config config;

	public WGCommandProcess(Config config) {
		this.config = config;
	}

	private HashSet<String> wgcmds = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"/rg",
				"/region",
				"/regions"
			}
		)
	);

	private VertExpand vertexpand = new VertExpand();
	private BlockLimits blocklimits = new BlockLimits();

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
			boolean result = vertexpand.expand(event.getPlayer());
			if (result) {
				event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Регион автоматически расширен по вертикали");
			}
		}
		ProcessedClaimInfo info = blocklimits.processClaimInfo(config, event.getPlayer());
		if (!info.isClaimAllowed()) {
			event.getPlayer().sendMessage(ChatColor.RED + "Вы не можете заприватить такой большой регион");
			if (!info.getMaxSize().equals("-1")) {
				event.getPlayer().sendMessage(ChatColor.RED + "Ваш лимит: "+info.getMaxSize()+", вы попытались заприватить: "+info.getClaimedSize());
			}
			event.setCancelled(true);
			return;
		}
		if (config.autoflagsenabled) {
			AutoFlags.setFlagsForRegion(config, event.getPlayer().getWorld(), cmds[2]);
		}
	}

}
