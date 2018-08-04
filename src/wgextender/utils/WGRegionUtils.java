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
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.permission.RegionPermissionModel;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WGRegionUtils  {

	public static final RegionQuery REGION_QUERY = getRegionContainer().createQuery();

	public static RegionContainer getRegionContainer() {
		return WorldGuard.getInstance().getPlatform().getRegionContainer();
	}

	public static RegionManager getRegionManager(World world) {
		return getRegionContainer().get(BukkitAdapter.adapt(world));
	}

	public static boolean canBypassProtection(Player player) {
		return new RegionPermissionModel(WorldGuardPlugin.inst().wrapPlayer(player)).mayIgnoreRegionProtection(BukkitAdapter.adapt(player.getWorld()));
	}

	public static boolean isInWGRegion(Location location) {
		return getRegionsAt(location).size() > 0;
	}

	public static boolean isInTheSameRegionOrWild(Location location1, Location location2) {
		return getRegionsAt(location1).getRegions().equals(getRegionsAt(location2).getRegions());
	}

	public static boolean isInTheSameRegion(Location location1, Location location2) {
		ApplicableRegionSet ars1 = getRegionsAt(location1);
		ApplicableRegionSet ars2 = getRegionsAt(location2);
		return (ars1.size() > 0) && ars1.getRegions().equals(ars2.getRegions());
	}

	public static boolean canBuild(Player player, Location location) {
		return isFlagAllows(player, location, Flags.BUILD);
	}

	public static boolean isFlagAllows(Player player, Location location, StateFlag flag) {
		return REGION_QUERY.testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), flag);
	}

	public static boolean isFlagTrue(Location location, BooleanFlag flag) {
		Boolean bool = REGION_QUERY.queryValue(BukkitAdapter.adapt(location), null, flag);
		return (bool != null) && bool;
	}

	public static ApplicableRegionSet getRegionsAt(Location location) {
		return REGION_QUERY.getApplicableRegions(BukkitAdapter.adapt(location));
	}

}
