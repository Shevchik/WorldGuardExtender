package WGExtender;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

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
