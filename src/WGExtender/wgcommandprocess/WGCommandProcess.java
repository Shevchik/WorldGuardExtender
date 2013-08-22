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

package WGExtender.wgcommandprocess;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import WGExtender.Config;
import WGExtender.Main;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class WGCommandProcess implements Listener {

	private Main main;
	private Config config;

	
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
		//process expand
		processVertExpand(event.getPlayer());
		//process group block limits
		if (!processBlockLimits(event.getPlayer()))
		{
			event.getPlayer().sendMessage(ChatColor.RED+"Вы не можете заприватить такой большой регион");
			event.setCancelled(true);
			return;
		}
	}
	
	
	private boolean processBlockLimits(Player pl)
	{
		if (config.blocklimitsenabled)
		{
			Selection psel = main.we.getSelection(pl);
			String[] pgroups = main.wg.getGroups(pl);
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
			
		}
		return true;
	}
	
	private void processVertExpand(Player pl)
	{
		if (config.expandvert)
		{
			Selection psel = main.we.getSelection(pl);
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
