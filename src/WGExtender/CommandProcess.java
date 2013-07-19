package WGExtender;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CommandProcess implements Listener {

	private Config config;
	
	public CommandProcess(Config config) {
		this.config = config;
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void ProcessWGCommand(PlayerCommandPreprocessEvent event)
	{
		String[] cmds = event.getMessage().split("[ ]");
		//check only WG region command
		if (!(cmds[0].equalsIgnoreCase("rg") || cmds[0].equalsIgnoreCase("region"))) {return;}
		//check only claim command
		if (!cmds[1].equalsIgnoreCase("claim")) {return;}
		//now process command
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		System.out.println(we.getSelection(event.getPlayer()).getArea());
		System.out.println(wg.getGroups(event.getPlayer()));
	}
	
}
