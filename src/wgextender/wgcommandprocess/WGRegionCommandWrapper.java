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

package wgextender.wgcommandprocess;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wgextender.Config;
import wgextender.WGExtender;
import wgextender.utils.CommandUtils;
import wgextender.utils.WEUtils;
import wgextender.wgcommandprocess.BlockLimits.ProcessedClaimInfo;

public class WGRegionCommandWrapper extends Command {

	public static void inject(Config config) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Command command = CommandUtils.getCommands().get("region");
		Command wrapper = new WGRegionCommandWrapper(config, command);
		CommandUtils.unregisterCommand(WGExtender.getInstance().getWorldGuard(), command);
		CommandUtils.registerCommand(WGExtender.getInstance().getWorldGuard(), wrapper);
	}

	public static void uninject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {
		Command command = CommandUtils.getCommands().get("region");
		WGRegionCommandWrapper wrapper = (WGRegionCommandWrapper) command;
		CommandUtils.unregisterCommand(WGExtender.getInstance().getWorldGuard(), wrapper);
		CommandUtils.registerCommand(WGExtender.getInstance().getWorldGuard(), wrapper.originalcommand);
	}


	private Config config;
	private Command originalcommand;

	private WGRegionCommandWrapper(Config config, Command originalcommand) {
		super(originalcommand.getName(), originalcommand.getDescription(), originalcommand.getUsage(), originalcommand.getAliases());
		this.config = config;
		this.originalcommand = originalcommand;
	}

	private BlockLimits blocklimits = new BlockLimits();

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player && args.length >= 2 && args[0].equals("claim")) {
			Player player = (Player) sender;
			String regionname = args[1];
			if (config.expandvert) {
				boolean result = WEUtils.expandVert((Player) sender);
				if (result) {
					player.sendMessage(ChatColor.YELLOW + "Регион автоматически расширен по вертикали");
				}
			}
			ProcessedClaimInfo info = blocklimits.processClaimInfo(config, player);
			if (!info.isClaimAllowed()) {
				player.sendMessage(ChatColor.RED + "Вы не можете заприватить такой большой регион");
				if (!info.getMaxSize().equals("-1")) {
					player.sendMessage(ChatColor.RED + "Ваш лимит: "+info.getMaxSize()+", вы попытались заприватить: "+info.getClaimedSize());
				}
				return true;
			}
			boolean hasRegion = AutoFlags.hasRegion(player.getWorld(), regionname);
			boolean result = originalcommand.execute(player, label, args);
			if (!hasRegion && config.autoflagsenabled) {
				AutoFlags.setFlagsForRegion(player.getWorld(), config, regionname);
			}
			return result;
		} else {
			return originalcommand.execute(sender, label, args);
		}
	}

}
