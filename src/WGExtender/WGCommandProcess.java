package WGExtender;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGCommandProcess implements Listener {

	private Config config;
	
	public WGCommandProcess(Config config) {
		this.config = config;
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void ProcessWGCommand(PlayerCommandPreprocessEvent event)
	{
		String[] cmds = event.getMessage().split("\\s+");
		//we need at least 2 arguments
		if (cmds.length < 2) {return;}
		//check only WG region command
		if (!(cmds[0].equalsIgnoreCase("/rg") || cmds[0].equalsIgnoreCase("/region"))) {return;}
		//check only claim command
		if (!cmds[1].equalsIgnoreCase("claim")) {return;}
		//now process command
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		Selection psel = we.getSelection(event.getPlayer());
		String[] pgroups = wg.getGroups(event.getPlayer());
		if (psel != null)
		{
			System.out.println(psel.getArea());
		}
		System.out.println(Arrays.asList(pgroups));
	}
	
}
