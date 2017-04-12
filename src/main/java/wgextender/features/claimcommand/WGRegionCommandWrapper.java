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

package wgextender.features.claimcommand;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandException;

import wgextender.Config;
import wgextender.features.claimcommand.BlockLimits.ProcessedClaimInfo;
import wgextender.utils.CommandUtils;
import wgextender.utils.WEUtils;

public class WGRegionCommandWrapper extends Command {

	public static void inject(Config config) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		WGRegionCommandWrapper wrapper = new WGRegionCommandWrapper(config, CommandUtils.getCommands().get("region"));
		CommandUtils.replaceComamnd(wrapper.originalcommand, wrapper);
	}

	public static void uninject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {
		WGRegionCommandWrapper wrapper = (WGRegionCommandWrapper) CommandUtils.getCommands().get("region");
		CommandUtils.replaceComamnd(wrapper, wrapper.originalcommand);
	}


	private Config config;
	private Command originalcommand;

	private WGRegionCommandWrapper(Config config, Command originalcommand) {
		super(originalcommand.getName(), originalcommand.getDescription(), originalcommand.getUsage(), originalcommand.getAliases());
		this.config = config;
		this.originalcommand = originalcommand;
	}

	private final BlockLimits blocklimits = new BlockLimits();

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player && args.length >= 2 && args[0].equalsIgnoreCase("claim")) {
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
			try {
				WEClaimCommand.claim(regionname, sender);
				if (!hasRegion && config.autoflagsenabled) {
					AutoFlags.setFlagsForRegion(player.getWorld(), config, regionname);
				}
			} catch (CommandException ex) {
				sender.sendMessage(ChatColor.RED + ex.getMessage());
			}
			return true;
		} else {
			return originalcommand.execute(sender, label, args);
		}
	}

}
