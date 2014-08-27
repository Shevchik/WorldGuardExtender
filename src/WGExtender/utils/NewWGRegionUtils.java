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

import java.util.HashMap;
import java.util.HashSet;

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

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class NewWGRegionUtils implements WGRegionUtilsInterface {

	@Override
	public boolean isInWGRegion(Location l) {
		try {
			return getARS(l).size() > 0;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean isInTheSameRegion(Location l1, Location l2) {
		try {
			return getARS(l1).getRegions().equals(getARS(l2).getRegions());
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public boolean canBuild(Player player, Location l) {
		try {
			WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
			return getARS(l).testState(wg.wrapPlayer(player, true), DefaultFlag.BUILD);
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public boolean isFlagAllows(Player player, Block block, StateFlag flag) {
		try {
			WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
			LocalPlayer localPlayer = wg.wrapPlayer(player, true);
			ApplicableRegionSet ars = getARS(block.getLocation());
			if (flag instanceof BlockInteractRestrictFlag) {
				String whitelistValue = ars.queryValue(localPlayer, BlockInteractRestrictWhitelistFlag.getInstance());
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
			return (ars.testState(localPlayer, flag));
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public boolean isFlagAllows(Player player, Entity entity, StateFlag flag) {
		try {
			WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
			ApplicableRegionSet ars = getARS(entity.getLocation());
			if (flag instanceof EntityInteractRestrictFlag) {
				String whitelistValue = ars.queryValue(wg.wrapPlayer(player, true), EntityInteractRestrictWhitelistFlag.getInstance());
				if (whitelistValue != null) {
					HashSet<EntityType> whitelistData = EntityInteractRestrictWhitelistFlag.parseWhitelist(whitelistValue);
					EntityType entityType = entity.getType();
					if (whitelistData.contains(entityType)) {
						return true;
					}
				}
			}
			return (ars.testState(wg.wrapPlayer(player, true), flag));
		} catch (Exception e) {
		}
		return true;
	}

	private ApplicableRegionSet getARS(Location l) {
		return WGExtender.getInstance().getWorldGuard().getRegionContainer().createQuery().getApplicableRegions(l);
	}

}
