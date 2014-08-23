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

package WGExtender.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import WGExtender.Config;
import WGExtender.WGExtender;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

public class Commands implements CommandExecutor {

	private WGExtender main;
	private Config config;

	public Commands(WGExtender main, Config config) {
		this.main = main;
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		if (!canExecute(sender)) {
			sender.sendMessage(ChatColor.RED+"Недостаточно прав");
			return true;
		}
		if ((args.length == 1) && args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.BLUE + "[SENDER:ANY] wgex reload - перезагрузить конфиг");
			sender.sendMessage(ChatColor.BLUE + "[SENDER:PLAYER] wgex search - ищет регионы в выделенной области");
			sender.sendMessage(ChatColor.BLUE + "[SENDER:ANY] wgex setflag {world} {flag} {value}  - устанавливает флаг {flag} со значением {value} на все регионы в мире {world}");
			return true;
		} else if ((args.length == 1) && args[0].equalsIgnoreCase("reload")) {
			config.loadConfig();
			sender.sendMessage(ChatColor.BLUE + "Конфиг перезагружен");
			return true;
		} else if ((args.length == 1) && args[0].equalsIgnoreCase("search")) {
			if (sender instanceof Player) {
				List<String> regions = RegionsInAreaSearch.getRegionsInPlayerSelection(main.getWorldEdit(), main.getWorldGuard(), (Player) sender);
				if ((regions == null) || (regions.size() == 0)) {
					sender.sendMessage(ChatColor.BLUE + "Регионов пересекающихся с выделенной зоной не найдено");
					return true;
				} else {
					sender.sendMessage(ChatColor.BLUE + "Найдены регионы пересекающиеся с выделенной зоной: "+ regions);
					return true;
				}
			}
		} else if ((args.length >= 4) && args[0].equalsIgnoreCase("setflag")) {
			World world = Bukkit.getWorld(args[1]);
			if (world != null) {
				for (Flag<?> flag : DefaultFlag.getFlags()) {
					if (flag.getName().equalsIgnoreCase(args[2])) {
						try {
							String value = joinString(Arrays.copyOfRange(args, 3, args.length), " ");
							SetFlags.setFlags(main.getWorldGuard(), flag, value, world);
							sender.sendMessage(ChatColor.BLUE + "Флаги установлены");
							return true;
						} catch (Exception e) {
							sender.sendMessage(ChatColor.BLUE + "Ошибка при обработке флага");
							return true;
						}
					}
				}
				sender.sendMessage(ChatColor.BLUE + "Флаг не найден");
				return true;
			} else {
				sender.sendMessage(ChatColor.BLUE + "Мир не найден");
				return true;
			}
		}
		return false;
	}

	private boolean canExecute(CommandSender sender) {
		if ((sender instanceof ConsoleCommandSender) || (sender instanceof RemoteConsoleCommandSender)) {
			return true;
		} else if (sender instanceof Player) {
			if (sender.isOp() || sender.hasPermission("wgextender.admin")) {
				return true;
			}
		}
		return false;
	}

	private String joinString(String[] args, String delimiter) {
		if (args.length == 0) {
			return "";
		}
		if (args.length == 1) {
			return args[0];
		}
		StringBuilder builder = new StringBuilder();
		builder.append(args[0]);
		for (int i = 1; i < args.length; i++) {
			builder.append(delimiter);
			builder.append(args[i]);
		}
		return builder.toString();
	}

}
