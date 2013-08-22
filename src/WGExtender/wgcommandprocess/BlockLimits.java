package WGExtender.wgcommandprocess;

import org.bukkit.entity.Player;

import WGExtender.Config;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class BlockLimits {

	
	protected static boolean allowClaim(Config config, WorldEditPlugin we, WorldGuardPlugin wg, Player pl)
	{
		if (pl.hasPermission("worldguard.region.unlimited")) 
		{
			return true;
		}
			
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
				int blocks = 0;
				try {blocks = config.blocklimits.get(pgroup);} catch (Exception e) {}
				if (blocks > maxblocks) {maxblocks = blocks;}
			}
			//if player tried to claim above limit - disallow player to process command
			if (psel.getArea() > maxblocks)
			{
				return false;
			}
		}
		return true;
	}
	
}
