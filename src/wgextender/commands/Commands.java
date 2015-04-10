/**
q * This program is free software; you can redistribute it and/or
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

package wgextender.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import wgextender.Config;
import wgextender.WGExtender;
import wgextender.commands.RegionsInAreaSearch.NoSelectionException;
import wgextender.features.claimcommand.AutoFlags;
import wgextender.utils.ReflectionUtils;
import wgextender.utils.StringUtils;
import wgextender.utils.Transform;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Commands implements CommandExecutor, TabCompleter {

	private Config config;

	public Commands(Config config) {
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		if (!canExecute(sender)) {
			sender.sendMessage(ChatColor.RED+"Недостаточно прав");
			return true;
		}
		if ((args.length == 1) && args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.BLUE + "wgex reload - перезагрузить конфиг");
			sender.sendMessage(ChatColor.BLUE + "wgex search - ищет регионы в выделенной области");
			sender.sendMessage(ChatColor.BLUE + "wgex setflag {world} {flag} {value}  - устанавливает флаг {flag} со значением {value} на все регионы в мире {world}");
			return true;
		} else if ((args.length == 1) && args[0].equalsIgnoreCase("reload")) {
			config.loadConfig();
			sender.sendMessage(ChatColor.BLUE + "Конфиг перезагружен");
			return true;
		} else if ((args.length == 1) && args[0].equalsIgnoreCase("search")) {
			if (sender instanceof Player) {
				try {
					List<String> regions = RegionsInAreaSearch.getRegionsInPlayerSelection((Player) sender);
					if (regions.isEmpty()) {
						sender.sendMessage(ChatColor.BLUE + "Регионов пересекающихся с выделенной зоной не найдено");
						return true;
					} else {
						sender.sendMessage(ChatColor.BLUE + "Найдены регионы пересекающиеся с выделенной зоной: "+ regions);
						return true;
					}
				} catch (NoSelectionException e) {
					sender.sendMessage(ChatColor.BLUE + "Сначала выделите зону поиска");
					return true;
				}
			}
		} else if ((args.length >= 4) && args[0].equalsIgnoreCase("setflag")) {
			World world = Bukkit.getWorld(args[1]);
			if (world != null) {
				Flag<?> flag = DefaultFlag.fuzzyMatchFlag(args[2]);
				if (flag != null) {
					try {
						String value = StringUtils.join(Arrays.copyOfRange(args, 3, args.length), " ");
						for (ProtectedRegion region : WGExtender.getInstance().getWorldGuard().getRegionManager(world).getRegions().values()) {
							if (region instanceof GlobalProtectedRegion) {
								continue;
							}
							AutoFlags.setFlag(region, flag, value);
						}
						sender.sendMessage(ChatColor.BLUE + "Флаги установлены");
						return true;
					} catch (InvalidFlagFormat e) {
						sender.sendMessage(ChatColor.BLUE + "Неправильное значение для флага "+flag.getName()+": "+e.getMessage());
						return true;
					} catch (CommandException e) {
						sender.sendMessage(ChatColor.BLUE + "Неправильный формат флага "+flag.getName()+": "+e.getMessage());
						return true;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!canExecute(sender)) {
			return Collections.emptyList();
		}
		if (args.length == 1) {
			return StringUtils.filterStartsWith(
				args[0],
				sender instanceof Player ? new String[] { "help", "reload", "search", "setflag" } : new String[] { "help", "reload", "setflag"}
			);
		}
		if (args.length >= 2) {
			switch (args[0].toLowerCase()) {
				case "help":
				case "reload":
				case "search": {
					return Collections.emptyList();
				}
				case "setflag": {
					switch (args.length) {
						case 2: {
							return StringUtils.filterStartsWith(args[1], Transform.toList(Bukkit.getWorlds(), new Transform.Function<String, World>() {
								@Override
								public String transform(World original) {
									return original.getName();
								}
							}));
						}
						case 3: {
							return StringUtils.filterStartsWith(args[2], Transform.toList(DefaultFlag.getFlags(), new Transform.Function<String, Flag<?>>() {
								@Override
								public String transform(Flag<?> original) {
									return original.getName();
								}
							}));
						}
						case 4: {
							Flag<?> flag = DefaultFlag.fuzzyMatchFlag(args[2]);
							if (flag instanceof StateFlag) {
								return StringUtils.filterStartsWith(args[3], Transform.toList(StateFlag.State.values(), new Transform.Function<String, StateFlag.State>() {
									@Override
									public String transform(State original) {
										return original.toString();
									}
								}));
							}
							if (flag instanceof BooleanFlag) {
								return StringUtils.filterStartsWith(args[3], new String[] {"true", "false"});
							}
							if (flag instanceof EnumFlag<?>) {
								try {
									return StringUtils.filterStartsWith(args[3], Transform.toList(
										((EnumFlag<? extends Enum<?>>) flag).getEnumClass().getEnumConstants(),
										new Transform.Function<String, Enum<?>>() {
											@Override
											public String transform(Enum<?> original) {
												return original.toString();
											}
										})
									);
								} catch (Exception e) {
								}
							}
							break;
						}
					}
					return Collections.emptyList();
				}
			}
		}
		return Collections.emptyList();
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



}
