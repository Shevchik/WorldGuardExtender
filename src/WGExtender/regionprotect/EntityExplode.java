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

import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import WGExtender.Config;
import WGExtender.Main;

public class EntityExplode implements Listener {

	private Main main;
	private Config config;

	
	public EntityExplode(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onEntityExplode(EntityExplodeEvent e)
	{
		if (!config.blockentityexplosionblockdamage) {return;}
		if (e.getEntity() instanceof TNTPrimed)
		{
			if (config.blocktntexplosionblockdmage)
			{
				filterProtectedBlocks(e.blockList());
			}
		} else
		if (e.getEntity() instanceof Creeper)
		{
			if (config.blockcreeperexplosionblockdmage)
			{
				filterProtectedBlocks(e.blockList());
			}
		}
		
	}
	
	
	private void filterProtectedBlocks(List<Block> blocklist)
	{
		Iterator<Block> it = blocklist.iterator();
		while (it.hasNext())
		{
			if (WGRPUtils.isInWGRegion(main.wg, it.next()))
			{
				it.remove();
			}
		}
	}
	
}
