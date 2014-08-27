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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import WGExtender.WGExtender;
import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.flags.BlockInteractRestrictWhitelistFlag;
import WGExtender.flags.EntityInteractRestrictFlag;
import WGExtender.flags.EntityInteractRestrictWhitelistFlag;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class OldWGRegionUtils implements WGRegionUtilsInterface {

	@Override
	public boolean isInWGRegion(Location l) {
		try {
			Object ars = getARS(l);
			Method sizeMethod = ars.getClass().getDeclaredMethod("size");
			sizeMethod.setAccessible(true);
			return (int) sizeMethod.invoke(ars) > 0;
		} catch (Exception e) {
			WGExtender.log(Level.SEVERE, "Unable to check isInWGRegion");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isInTheSameRegion(Location l1, Location l2) {
		try {
			Object ars1 = getARS(l1);
			Object ars2 = getARS(l2);
			return ars1.equals(ars2);
		} catch (Exception e) {
			WGExtender.log(Level.SEVERE, "Unable to check isInTheSameRegion");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean canBuild(Player player, Location l) {
		try {
			Object ars = getARS(l);
			LocalPlayer localPlayer = WGExtender.getInstance().getWorldGuard().wrapPlayer(player);
			Method canBuildMethod = ars.getClass().getMethod("canBuild", LocalPlayer.class);
			canBuildMethod.setAccessible(true);
			return (boolean) canBuildMethod.invoke(ars, localPlayer);
		} catch (Exception e) {
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
			Method allowsMethod = getAllowsMethod(ars);
			allowsMethod.setAccessible(true);
			return (boolean) allowsMethod.invoke(ars, BlockInteractRestrictFlag.getInstance(), localPlayer);
		} catch (Exception e) {
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
				String whitelistValue = (String) getFlagMethod.invoke(EntityInteractRestrictWhitelistFlag.getInstance());
				if (whitelistValue != null) {
					HashSet<EntityType> whitelistData = EntityInteractRestrictWhitelistFlag.parseWhitelist(whitelistValue);
					EntityType entityType = entity.getType();
					if (whitelistData.contains(entityType)) {
						return true;
					}
				}
			}
			Method allowsMethod = getAllowsMethod(ars);
			allowsMethod.setAccessible(true);
			return (boolean) allowsMethod.invoke(ars, EntityInteractRestrictFlag.getInstance(), localPlayer);
		} catch (Exception e) {
			WGExtender.log(Level.SEVERE, "Unable to check isFlagAllows");
			e.printStackTrace();
		}
		return true;
	}

	private Object getARS(Location l) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
		RegionManager rm = wg.getRegionManager(l.getWorld());
		Vector wevect = BukkitUtil.toVector(l);
		Method getARSMethod = rm.getClass().getMethod("getApplicableRegions", Vector.class);
		getARSMethod.setAccessible(true);
		return getARSMethod.invoke(rm, wevect);
	}

	private Method getAllowsMethod(Object ars) {
		for (Method method : ars.getClass().getMethods()) {
			if (method.getName().equals("allows") && method.getParameterTypes().length == 2) {
				return method;
			}
		}
		return null;
	}

}
