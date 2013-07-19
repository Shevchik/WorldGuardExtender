package WGExtender;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGCommandProcess implements Listener {

	private Config config;
	private WorldEditPlugin we = null;
	private WorldGuardPlugin wg = null;
	
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
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		//process group block limits
		if (!processBlockLimits(event.getPlayer()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	
	private boolean processBlockLimits(Player pl)
	{
		if (config.blocklimitsenabled)
		{
			Selection psel = we.getSelection(pl);
			String[] pgroups = wg.getGroups(pl);
			//selection is null, allow player to process command
			if (psel == null)
			{
				return true;
			}
			else
			//no groups, allow player to process command
			if (pgroups.length == 0)
			{
				return true;
			}
			//process limits
			else
			{
				//get limit for player
				int maxblocks = 0;
				for (String pgroup : pgroups)
				{
					int blocks = config.blocklimits.get(pgroup);
					if (blocks == -1) 
					{//no limit for group, allow player to process command
						return true;
					}
					if (blocks > maxblocks) {maxblocks = blocks;}
				}
				//if player tried to claim above limit - disallow player to process command
				if (psel.getArea() > maxblocks)
				{
					return false;
				}
			}
			
		}
		return true;
	}
	
}
