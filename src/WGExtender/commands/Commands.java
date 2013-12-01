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

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

import WGExtender.Config;
import WGExtender.Main;

public class Commands implements CommandExecutor {

	private Main main;
	private Config config;
	
	public Commands(Main main, Config config)
	{
		this.main = main;
		this.config = config;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		if (canExecute(sender))
		{
			if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
			{
				config.loadConfig();
				sender.sendMessage(ChatColor.BLUE+"Конфиг перезагружен");
			} else
			if (args.length == 1 && args[0].equalsIgnoreCase("search"))
			{
				if (sender instanceof Player)
				{
					List<String> regions = RegionsInAreaSearch.getRegionsInPlayerSelection(main.we,main.wg, (Player) sender);
					if (regions != null && regions.size() != 0)
					{
						sender.sendMessage(ChatColor.BLUE+"Регионов пересекающихся с выделенной зоной не найдено");
					} else
					{
						sender.sendMessage(ChatColor.BLUE+"Найдены регионы пересекающиеся с выделенной зоной: "+regions);
					}
				}
				return true;
			} else
			if (args.length == 4 && args[0].equalsIgnoreCase("setflag"))
			{
				for (Flag<?> flag : DefaultFlag.getFlags())
				{
					if (flag.getName().equalsIgnoreCase(args[1]))
					{
						try {
							World world = Bukkit.getWorld(args[3]);
							if (world != null)
							{
								SetFlags.setFlags(main.wg, flag, args[2], world);
								sender.sendMessage(ChatColor.BLUE+"Флаги установлены");
							} else
							{
								sender.sendMessage(ChatColor.BLUE+"Мир не найден");
							}
							return true;
						} catch (Exception e) {
							sender.sendMessage(ChatColor.BLUE+"Ошибка при обработке флага");
							return true;
						}
					}
				}
				sender.sendMessage(ChatColor.BLUE+"Флаг не найден");
				return true;
			}
			return true;
		}
		return false;
	}
	
	private boolean canExecute(CommandSender sender)
	{
		boolean can = false;
		if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender )
		{
			can = true;
		}
		if (sender instanceof Player)
		{
			if (sender.isOp() || sender.hasPermission("wgextender.admin"))
			{
				can = true;
			}
		}
		return can;
		
	}

}
