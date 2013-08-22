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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import WGExtender.Config;

public class Commands implements CommandExecutor {

	private Config config;
	
	public Commands(Config config)
	{
		this.config = config;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {
		if (canExecute(sender))
		{
			config.loadConfig();
			sender.sendMessage(ChatColor.BLUE+"Конфиг перезагружен");
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
