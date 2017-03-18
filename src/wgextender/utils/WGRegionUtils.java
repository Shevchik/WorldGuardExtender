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

package wgextender.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import wgextender.WGExtender;

import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.permission.RegionPermissionModel;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class WGRegionUtils  {

	public static final RegionQuery REGION_QUERY = WGExtender.getWorldGuard().getRegionContainer().createQuery();

	public static boolean canBypassProtection(Player player) {
		return new RegionPermissionModel(WGExtender.getWorldGuard(), player).mayIgnoreRegionProtection(player.getWorld());
	}

	public static boolean isInWGRegion(Location location) {
		return getARS(location).size() > 0;
	}

	public static boolean isInTheSameRegionOrWild(Location location1, Location location2) {
		return getARS(location1).getRegions().equals(getARS(location2).getRegions());
	}

	public static boolean isInTheSameRegion(Location location1, Location location2) {
		ApplicableRegionSet ars1 = getARS(location1);
		ApplicableRegionSet ars2 = getARS(location2);
		return ars1.size() > 0 && ars1.getRegions().equals(ars2.getRegions());
	}

	public static boolean canBuild(Player player, Location location) {
		return isFlagAllows(player, location, DefaultFlag.BUILD);
	}

	public static boolean isFlagAllows(Player player, Location location, StateFlag flag) {
		return REGION_QUERY.testState(location, player, flag);
	}

	public static boolean isFlagTrue(Location location, BooleanFlag flag) {
		Boolean bool = REGION_QUERY.queryValue(location, (Player) null, flag);
		return bool != null && bool;
	}

	private static ApplicableRegionSet getARS(Location l) {
		return REGION_QUERY.getApplicableRegions(l);
	}

}
