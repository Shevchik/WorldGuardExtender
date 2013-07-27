package WGExtender;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGCommandProcess implements Listener {

	private Main main;
	private Config config;
	private WorldEditPlugin we = null;
	private WorldGuardPlugin wg = null;
	
	public WGCommandProcess(Main main, Config config) {
		this.main = main;
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
		main.debug("Processing player "+event.getPlayer().getName());
		main.debug("Processing limits");
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		//process expand
		main.debug("processing vertical expand");
		processVertExpand(event.getPlayer());
		//process group block limits
		if (!processBlockLimits(event.getPlayer()))
		{
			event.getPlayer().sendMessage(ChatColor.RED+"Вы не можете заприватить такой большой регион");
			event.setCancelled(true);
			main.debug("Finished processing player "+event.getPlayer().getName());
			return;
		}
		main.debug("Finished processing player "+event.getPlayer().getName());
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
				main.debug("Selection is null");
				return true;
			}
			else
			//no groups, allow player to process command
			if (pgroups.length == 0)
			{
				main.debug("Player doesn't have a permission group");
				return true;
			}
			//process limits
			else
			{
				main.debug("Player selection size: "+psel.getArea()+", player groups: "+Arrays.asList(pgroups));
				//get limit for player
				int maxblocks = 0;
				for (String pgroup : pgroups)
				{
					int blocks = 0;
					try {blocks = config.blocklimits.get(pgroup);} catch (Exception e) {}
					if (blocks > maxblocks) {maxblocks = blocks;}
					main.debug("Player group: "+pgroup+", blocklimit: "+blocks);
				}
				main.debug("Final player blocklimit: "+maxblocks);
				//if player tried to claim above limit - disallow player to process command
				if (psel.getArea() > maxblocks)
				{
					return false;
				}
			}
			
		}
		return true;
	}
	
	private void processVertExpand(Player pl)
	{
		if (config.expandvert)
		{
			Selection psel = we.getSelection(pl);
			if (psel == null)
			{
				return;
			}
			else
			{
				Bukkit.dispatchCommand(pl, "/expand vert");
			}
		}
	}
	
}
