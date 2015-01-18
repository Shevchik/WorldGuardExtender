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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import wgextender.WGExtender;
import wgextender.flags.BlockInteractRestrictFlag;
import wgextender.flags.BlockInteractRestrictWhitelistFlag;
import wgextender.flags.EntityInteractRestrictFlag;
import wgextender.flags.EntityInteractRestrictWhitelistFlag;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class OldWGRegionUtils implements WGRegionUtilsInterface {

	@Override
	public boolean isInWGRegion(Location location) {
		try {
			RegionManager rm = WGExtender.getInstance().getWorldGuard().getRegionManager(location.getWorld());
			return !rm.getApplicableRegionsIDs(BukkitUtil.toVector(location)).isEmpty();
		} catch (Throwable e) {
		}
		return false;
	}

	@Override
	public boolean isInTheSameRegion(Location location1, Location location2) {
		try {
			RegionManager rm = WGExtender.getInstance().getWorldGuard().getRegionManager(location1.getWorld());
			List<String> ari1 = rm.getApplicableRegionsIDs(BukkitUtil.toVector(location1));
			List<String> ari2 = rm.getApplicableRegionsIDs(BukkitUtil.toVector(location2));
			return ari1.equals(ari2);
		} catch (Throwable e) {
		}
		return false;
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		try {
			Object ars = getARS(location);
			LocalPlayer localPlayer = WGExtender.getInstance().getWorldGuard().wrapPlayer(player);
			Method canBuildMethod = ars.getClass().getMethod("canBuild", LocalPlayer.class);
			canBuildMethod.setAccessible(true);
			return (boolean) canBuildMethod.invoke(ars, localPlayer);
		} catch (Throwable e) {
			WGExtender.log(Level.SEVERE, "Unable to check canBuild");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isFlagAllows(Player player, Block block, StateFlag flag) {
		try {
			LocalPlayer localPlayer = WGExtender.getInstance().getWorldGuard().wrapPlayer(player);
			Object ars = getARS(block.getLocation());
			if (flag instanceof BlockInteractRestrictFlag) {
				Method getFlagMethod = ars.getClass().getMethod("getFlag", Flag.class);
				getFlagMethod.setAccessible(true);
				String whitelistValue = (String) getFlagMethod.invoke(ars, BlockInteractRestrictWhitelistFlag.getInstance());
				if (whitelistValue != null) {
					HashMap<Material, HashSet<Material>> whitelistData = BlockInteractRestrictWhitelistFlag.parseWhitelist(whitelistValue);
					Material blockMaterial = block.getType();
					Material handMaterial = player.getItemInHand() == null ? Material.AIR : player.getItemInHand().getType();
					if (whitelistData.containsKey(blockMaterial)) {
						HashSet<Material> allowedHandMaterials = whitelistData.get(blockMaterial);
						if (allowedHandMaterials.size() == 0) {
							return true;
						}
						if (allowedHandMaterials.contains(handMaterial)) {
							return true;
						}
					}
				}
			}
			return (boolean) MethodsCache.getAllowsMethod(ars).invoke(ars, BlockInteractRestrictFlag.getInstance(), localPlayer);
		} catch (Throwable e) {
			WGExtender.log(Level.SEVERE, "Unable to check isFlagAllows");
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean isFlagAllows(Player player, Entity entity, StateFlag flag) {
		try {
			LocalPlayer localPlayer = WGExtender.getInstance().getWorldGuard().wrapPlayer(player);
			Object ars = getARS(entity.getLocation());
			if (flag instanceof EntityInteractRestrictFlag) {
				Method getFlagMethod = ars.getClass().getMethod("getFlag", Flag.class);
				getFlagMethod.setAccessible(true);
				String whitelistValue = (String) getFlagMethod.invoke(ars, EntityInteractRestrictWhitelistFlag.getInstance());
				if (whitelistValue != null) {
					HashSet<EntityType> whitelistData = EntityInteractRestrictWhitelistFlag.parseWhitelist(whitelistValue);
					EntityType entityType = entity.getType();
					if (whitelistData.contains(entityType)) {
						return true;
					}
				}
			}
			return (boolean) MethodsCache.getAllowsMethod(ars).invoke(ars, EntityInteractRestrictFlag.getInstance(), localPlayer);
		} catch (Throwable e) {
			WGExtender.log(Level.SEVERE, "Unable to check isFlagAllows");
			e.printStackTrace();
		}
		return true;
	}

	private Object getARS(Location location) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
		RegionManager rm = wg.getRegionManager(location.getWorld());
		Vector wevect = BukkitUtil.toVector(location);
		return MethodsCache.getGetARSMethod(rm).invoke(rm, wevect);
	}


	private static class MethodsCache {

		private static Method cachedAllowsMethod = null;
		public static Method getAllowsMethod(Object ars) {
			if (cachedAllowsMethod == null) {
				for (Method method : ars.getClass().getMethods()) {
					if (method.getName().equals("allows") && method.getParameterTypes().length == 2) {
						cachedAllowsMethod = method;
						cachedAllowsMethod.setAccessible(true);
						return cachedAllowsMethod;
					}
				}
			}
			return cachedAllowsMethod;
		}

		private static Method cachedGetARSMethod = null;
		public static Method getGetARSMethod(Object rm) throws NoSuchMethodException, SecurityException {
			if (cachedGetARSMethod == null) {
				cachedAllowsMethod = rm.getClass().getMethod("getApplicableRegions", Vector.class);
				cachedAllowsMethod.setAccessible(true);
				return cachedAllowsMethod;
			}
			return cachedAllowsMethod;
		}

	}

}
