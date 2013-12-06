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

package WGExtender.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.flags.BlockInteractRestrictWhitelistFlag;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class WGRegionUtils {


	public static boolean isInWGRegion(WorldGuardPlugin wg, Location l)
	{
		try {
			return wg.getRegionManager(l.getWorld()).getApplicableRegions(l).size() > 0;
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled
		}
		return false;
	}
	
	public static boolean isInTheSameRegion(WorldGuardPlugin wg, Location l1, Location l2)
	{
		try {
			//plain equals doesn't want to work here :(
			List<String> ari1 = wg.getRegionManager(l1.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(l1));
			List<String> ari2 = wg.getRegionManager(l2.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(l2));
			return ari1.equals(ari2);
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled
		}
		return true;
	}
	
	
	public static boolean isOwnerOrMember(WorldGuardPlugin wg, Player p, Location l)
	{
		try {
			ApplicableRegionSet ars = wg.getRegionManager(l.getWorld()).getApplicableRegions(l);
			return ars.isOwnerOfAll(wg.wrapPlayer(p)) || ars.isMemberOfAll(wg.wrapPlayer(p));
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled
		}
		return false;
	}
	
	public static boolean isFlagAllows(WorldGuardPlugin wg, Player player, Location location, StateFlag flag)
	{
		try {
			ApplicableRegionSet ars = wg.getRegionManager(location.getWorld()).getApplicableRegions(location);

			if (flag instanceof BlockInteractRestrictFlag)
			{
				String whitelisted = ars.getFlag(BlockInteractRestrictWhitelistFlag.instance);
				if (whitelisted != null && whitelisted.contains(location.getBlock().getType().toString()))
				{
					return true;
				}
			}
			return (ars.allows(flag, wg.wrapPlayer(player)));
		} catch (Exception e) {
			//if we caught an exception here it means that regions for world are disabled or flag failed to initialize
		}
		return true;
	}


}
