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
import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import WGExtender.Config;
import WGExtender.Main;

public class AutoFlags {

	@SuppressWarnings("unchecked")
	protected static void setFlagsForRegion(Main main, final Config config, WorldGuardPlugin wg, World world, final String regionname)
	{
		final RegionManager rm = wg.getRegionManager(world);
		//ignore if rm is null
		if (rm == null) {return;}		
		//ignore setting flags for region if it already exist
		if (rm.getRegions().containsKey(regionname)) {return;}
		//now set flags
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					final ProtectedRegion rg = rm.getRegionExact(regionname);
					if (rg != null)
					{

						for (@SuppressWarnings("rawtypes") Flag flag : config.autoflags.keySet())
						{
							rg.setFlag(flag, config.autoflags.get(flag));
						}
					}
					try {
						rm.save();
					} catch (Exception e) {}
				}
			},20);
	}
	
}
