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

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import WGExtender.Config;
import WGExtender.WGExtender;

public class AutoFlags {

	protected static void setFlagsForRegion(WGExtender main, final Config config, final WorldGuardPlugin wg, World world, final String regionname)
	{
		final RegionManager rm = wg.getRegionManager(world);
		//ignore if rm is null
		if (rm == null) {return;}	
		//ignore setting flags for region if it exists before the command is handled by worldedit
		if (rm.hasRegion(regionname)) {return;}
		//now schedule a task that will set flags after 1 second
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run()
			{
				try {
					final ProtectedRegion rg = rm.getRegionExact(regionname);
					if (rg != null)
					{
						for (Entry<Flag<?>, String> entry : config.autoflags.entrySet())
						{
							Flag flag = entry.getKey();
							rg.setFlag(flag, flag.parseInput(wg, Bukkit.getConsoleSender(), entry.getValue()));
						}
						rm.save();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},20);
	}
	
}
