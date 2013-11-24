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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import WGExtender.Config;
import WGExtender.Main;

public class RestrictCommandProcess implements Listener {

	private Main main;
	private Config config;

	public RestrictCommandProcess(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void ProcessWGCommand(PlayerCommandPreprocessEvent event)
	{
		if (!config.restrictcommandsinregionsenabled) {return;}
		
		Player player = event.getPlayer();
		if (isInWGRegion(player))
		{
			String message = event.getMessage();
			message = message.replaceFirst("/", "").toLowerCase();
			for (String rcommand : config.restrictedcommands)
			{
				if (message.startsWith(rcommand) && !isOwnerOrMemberOfRegionsAtLocation(player))
				{
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED+"Вы не можете использовать эту команду на чужом регионе");
					return;
				}
			}
		}
	}
	
	
	
	protected boolean isInWGRegion(Player player)
	{
		try {
			if (main.wg.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation()).size() > 0) {return true;}
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled
		}
		return false;
	}
	
	protected boolean isOwnerOrMemberOfRegionsAtLocation(Player player)
	{
		try {
			LocalPlayer lp = main.wg.wrapPlayer(player);
			ApplicableRegionSet ars = main.wg.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
			if (ars.isOwnerOfAll(lp) || ars.isMemberOfAll(lp))
			{
				return true;
			}
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled
		}
		return false;
	}
	
}
