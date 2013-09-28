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

import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import WGExtender.Config;

public class AutoFlags {

	@SuppressWarnings("unchecked")
	protected static void setFlagsForRegion(Config config, WorldGuardPlugin wg, World world, String regionname)
	{
		try {
			RegionManager rm = wg.getRegionManager(world);
			ProtectedRegion rg = rm.getRegionExact(regionname);
			
			if (rg != null)
			{
				for (@SuppressWarnings("rawtypes") Flag flag : config.autoflags.keySet())
				{
					rg.setFlag(flag, config.autoflags.get(flag));
				}
			}
			rm.save();
		} catch (Exception e) {}
	}
	
}
