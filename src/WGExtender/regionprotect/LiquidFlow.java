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

package WGExtender.regionprotect;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import WGExtender.Config;
import WGExtender.Main;

public class LiquidFlow implements Listener {

	private Main main;
	private Config config;

	
	public LiquidFlow(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onLiquidFlow(BlockFromToEvent e)
	{
		if (!config.blockliquidflow) {return;}
		
		Block b = e.getBlock();
		if (b.isLiquid())
		{
			if (b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA)
			{
				if (config.blocklavaflow)
				{
					if (!allowLiquidFlow(b,e.getToBlock())) {e.setCancelled(true);}
				}
			} else
			if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER)
			{
				if (config.blockwaterflow)
				{
					if (!allowLiquidFlow(b,e.getToBlock())) {e.setCancelled(true);}
				}
			}
		}
	}
	
	
	private boolean allowLiquidFlow(Block from, Block to)
	{
		if (WGRPUtils.isInWGRegion(main.wg, to))
		{
			//block flow from unclaimed area
			if (!WGRPUtils.isInWGRegion(main.wg, from))
			{
				return false;
			}
			else
			//block flow from not the same regions
			{
				if (!WGRPUtils.isInTheSameRegion(main.wg, from,to))
				{
					return false;
				}
			}
		}
		return true;
	}
	
}
