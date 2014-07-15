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
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.flags.BlockInteractRestrictWhitelistFlag;
import WGExtender.flags.EntityInteractRestrictFlag;
import WGExtender.flags.EntityInteractRestrictWhitelistFlag;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class WGRegionUtils {

	public static boolean canBypassProtection(Player p) {
		if (p.hasPermission("worldguard.*")) {
			return true;
		}
		if (p.hasPermission("worldguard.region.*")) {
			return true;
		}
		if (p.hasPermission("worldguard.region.bypass.*")) {
			return true;
		}
		if (p.hasPermission("worldguard.region.bypass." + p.getWorld().getName())) {
			return true;
		}
		return false;
	}

	public static boolean isInWGRegion(WorldGuardPlugin wg, Location l) {
		try {
			return wg.getRegionManager(l.getWorld()).getApplicableRegions(l).size() > 0;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isInTheSameRegion(WorldGuardPlugin wg, Location l1, Location l2) {
		try {
			List<String> ari1 = wg.getRegionManager(l1.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(l1));
			List<String> ari2 = wg.getRegionManager(l2.getWorld()).getApplicableRegionsIDs(BukkitUtil.toVector(l2));
			return ari1.equals(ari2);
		} catch (Exception e) {
		}
		return true;
	}

	public static boolean canBuild(WorldGuardPlugin wg, Player player, Location l) {
		try {
			return wg.getRegionManager(l.getWorld()).getApplicableRegions(l).canBuild(wg.wrapPlayer(player));
		} catch (Exception e) {
		}
		return false;
	}

	private static Pattern splitWhiteSpace = Pattern.compile("\\s+");
	private static Pattern splitVertLine = Pattern.compile("[|]");
	private static Pattern splitColon = Pattern.compile("[:]");
	public static boolean isFlagAllows(WorldGuardPlugin wg, Player player, Block block, StateFlag flag) {
		try {
			ApplicableRegionSet ars = wg.getRegionManager(block.getLocation().getWorld()).getApplicableRegions(block.getLocation());
			if (flag instanceof BlockInteractRestrictFlag) {
				String blockName = block.getType().toString();
				String allowed = ars.getFlag(BlockInteractRestrictWhitelistFlag.instance);
				if (allowed != null) {
					String[] allowedNames = splitWhiteSpace.split(allowed);
					for (String allowedName : allowedNames) {
						String[] allowedNameSplit = splitVertLine.split(allowedName);
						if (allowedNameSplit[0].equals(blockName)) {
							if (allowedNameSplit.length == 2) {
								String[] allowedHandNames = splitColon.split(allowedNameSplit[1]);
								for (String allowedHandName : allowedHandNames) {
									if (allowedHandName.equalsIgnoreCase(player.getItemInHand().getType().toString())) {
										return true;
									}
								}
							} else {
								return true;
							}
						}
					}
				}
			}
			return (ars.allows(flag, wg.wrapPlayer(player)));
		} catch (Exception e) {
		}
		return true;
	}

	public static boolean isFlagAllows(WorldGuardPlugin wg, Player player, Entity entity, StateFlag flag) {
		try {
			ApplicableRegionSet ars = wg.getRegionManager(entity.getLocation().getWorld()).getApplicableRegions(entity.getLocation());
			if (flag instanceof EntityInteractRestrictFlag) {
				String entityName = entity.getType().getName();
				String allowedNames = ars.getFlag(EntityInteractRestrictWhitelistFlag.instance);
				if (allowedNames != null) {
					String[] allowedNamesSplit = allowedNames.split("\\s+");
					for (String allowedName : allowedNamesSplit) {
						if (allowedName.equals(entityName)) {
							return true;
						}
					}
				}
			}
			return (ars.allows(flag, wg.wrapPlayer(player)));
		} catch (Exception e) {
		}
		return true;
	}

}
