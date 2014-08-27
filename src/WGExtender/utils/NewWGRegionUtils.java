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

import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import WGExtender.WGExtender;
import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.flags.BlockInteractRestrictWhitelistFlag;
import WGExtender.flags.EntityInteractRestrictFlag;
import WGExtender.flags.EntityInteractRestrictWhitelistFlag;

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

	private Pattern splitWhiteSpace = Pattern.compile("\\s+");
	private Pattern splitVertLine = Pattern.compile("[|]");
	private Pattern splitColon = Pattern.compile("[:]");

	@Override
	public boolean isFlagAllows(Player player, Block block, StateFlag flag) {
		try {
			WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
			ApplicableRegionSet ars = getARS(block.getLocation());
			if (flag instanceof BlockInteractRestrictFlag) {
				String blockName = block.getType().toString();
				String allowed = ars.queryValue(wg.wrapPlayer(player, true), BlockInteractRestrictWhitelistFlag.instance);
				if (allowed != null) {
					String[] allowedNames = splitWhiteSpace.split(allowed);
					for (String allowedName : allowedNames) {
						String[] allowedNameSplit = splitVertLine.split(allowedName);
						if (allowedNameSplit[0].equals(blockName)) {
							if (allowedNameSplit.length == 2) {
								String[] allowedHandNames = splitColon.split(allowedNameSplit[1]);
								for (String allowedHandName : allowedHandNames) {
									if (allowedHandName.equals(player.getItemInHand().getType().toString())) {
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
			return (ars.testState(wg.wrapPlayer(player, true), flag));
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
				String entityName = entity.getType().getName();
				String allowedNames = ars.queryValue(wg.wrapPlayer(player, true), EntityInteractRestrictWhitelistFlag.instance);
				if (allowedNames != null) {
					String[] allowedNamesSplit = splitWhiteSpace.split(allowedNames);
					for (String allowedName : allowedNamesSplit) {
						if (allowedName.equals(entityName)) {
							return true;
						}
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
