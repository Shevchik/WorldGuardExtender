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

package wgextender.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import wgextender.Config;
import wgextender.features.claimcommand.AutoFlags;
import wgextender.utils.StringUtils;
import wgextender.utils.Transform;
import wgextender.utils.WEUtils;
import wgextender.utils.WGRegionUtils;

//TODO: refactor
public class Commands implements CommandExecutor, TabCompleter {

	protected final Config config;
	public Commands(Config config) {
		this.config = config;
	}

	protected static List<String> getRegionsInPlayerSelection(Player player) throws IncompleteRegionException {
		Region psel = WEUtils.getSelection(player);
		ProtectedRegion fakerg = new ProtectedCuboidRegion("wgexfakerg", psel.getMaximumPoint().toBlockVector(), psel.getMinimumPoint().toBlockVector());
		ApplicableRegionSet ars = WGRegionUtils.getRegionManager(player.getWorld()).getApplicableRegions(fakerg);
		return
			StreamSupport.stream(ars.spliterator(), false)
			.map(ProtectedRegion::getId)
			.collect(Collectors.toList());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		if (!sender.hasPermission("wgextender.admin")) {
			sender.sendMessage(ChatColor.RED+"Недостаточно прав");
			return true;
		}
		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
				case "help": {
					sender.sendMessage(ChatColor.BLUE + "wgex reload - перезагрузить конфиг");
					sender.sendMessage(ChatColor.BLUE + "wgex search - ищет регионы в выделенной области");
					sender.sendMessage(ChatColor.BLUE + "wgex setflag {world} {flag} {value}  - устанавливает флаг {flag} со значением {value} на все регионы в мире {world}");
					sender.sendMessage(ChatColor.BLUE + "wgex removeowner {name} - удаляет игрока из списков владельцев всех регионов");
					sender.sendMessage(ChatColor.BLUE + "wgex removemember {name} - удаляет игрока из списков членов всех регионов");
					return true;
				}
				case "reload": {
					config.loadConfig();
					sender.sendMessage(ChatColor.BLUE + "Конфиг перезагружен");
					return true;
				}
				case "search": {
					if (sender instanceof Player) {
						try {
							List<String> regions = getRegionsInPlayerSelection((Player) sender);
							if (regions.isEmpty()) {
								sender.sendMessage(ChatColor.BLUE + "Регионов пересекающихся с выделенной зоной не найдено");
							} else {
								sender.sendMessage(ChatColor.BLUE + "Найдены регионы пересекающиеся с выделенной зоной: "+ regions);
							}
						} catch (IncompleteRegionException e) {
							sender.sendMessage(ChatColor.BLUE + "Сначала выделите зону поиска");
						}
						return true;
					}
					return false;
				}
				case "setflag": {
					if (args.length < 4) {
						return false;
					}
					World world = Bukkit.getWorld(args[1]);
					if (world != null) {
						Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), args[2]);
						if (flag != null) {
							try {
								String value = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
								for (ProtectedRegion region : WGRegionUtils.getRegionManager(world).getRegions().values()) {
									if (region instanceof GlobalProtectedRegion) {
										continue;
									}
									AutoFlags.setFlag(world, region, flag, value);
								}
								sender.sendMessage(ChatColor.BLUE + "Флаги установлены");
							} catch (CommandException e) {
								sender.sendMessage(ChatColor.BLUE + "Неправильный формат флага "+flag.getName()+": "+e.getMessage());
							}
						}
						sender.sendMessage(ChatColor.BLUE + "Флаг не найден");
					} else {
						sender.sendMessage(ChatColor.BLUE + "Мир не найден");
					}
					return true;
				}
				case "removeowner": {
					if (args.length != 2) {
						return false;
					}
					OfflinePlayer oplayer = Bukkit.getOfflinePlayer(args[1]);
					String name = oplayer.getName();
					UUID uuid = oplayer.getUniqueId();
					for (RegionManager manager : WGRegionUtils.getRegionContainer().getLoaded()) {
						for (ProtectedRegion region : manager.getRegions().values()) {
							DefaultDomain owners = region.getOwners();
							owners.removePlayer(uuid);
							owners.removePlayer(name.toLowerCase());
							region.setOwners(owners);
						}
					}
					sender.sendMessage(ChatColor.BLUE + "Игрок удалён из списков владельцев всех регионов");
					return true;
				}
				case "removemember": {
					if (args.length != 2) {
						return false;
					}
					OfflinePlayer oplayer = Bukkit.getOfflinePlayer(args[1]);
					String name = oplayer.getName();
					UUID uuid = oplayer.getUniqueId();
					for (RegionManager manager : WGRegionUtils.getRegionContainer().getLoaded()) {
						for (ProtectedRegion region : manager.getRegions().values()) {
							DefaultDomain owners = region.getMembers();
							owners.removePlayer(uuid);
							owners.removePlayer(name.toLowerCase());
							region.setMembers(owners);
						}
					}
					sender.sendMessage(ChatColor.BLUE + "Игрок удалён из списков членов всех регионов");
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("wgextender.admin")) {
			return Collections.emptyList();
		}
		if (args.length == 1) {
			return StringUtils.filterStartsWith(
				args[0],
				sender instanceof Player ?
				new String[] { "help", "reload", "search", "setflag", "removeowner", "removemember" } :
				new String[] { "help", "reload", "setflag", "removeowner", "removemember"}
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
							return StringUtils.filterStartsWith(args[1], Transform.toList(Bukkit.getWorlds(), World::getName));
						}
						case 3: {
							return StringUtils.filterStartsWith(args[2], Transform.toList(WorldGuard.getInstance().getFlagRegistry(), Flag::getName));
						}
						case 4: {
							Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), args[2]);
							if (flag instanceof StateFlag) {
								return StringUtils.filterStartsWith(args[3], Transform.toList(StateFlag.State.values(), State::toString));
							}
							if (flag instanceof BooleanFlag) {
								return StringUtils.filterStartsWith(args[3], new String[] {"true", "false"});
							}
							if (flag instanceof EnumFlag<?>) {
								try {
									return StringUtils.filterStartsWith(args[3], Transform.toList(((EnumFlag<? extends Enum<?>>) flag).getEnumClass().getEnumConstants(), Enum::toString));
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

}
