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

import java.math.BigInteger;

import org.bukkit.entity.Player;

import WGExtender.Config;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class BlockLimits {

	protected static boolean allowClaim(Config config, WorldEditPlugin we, WorldGuardPlugin wg, Player player) {
		if (player.hasPermission("worldguard.region.unlimited")) {
			return true;
		}
		Selection psel = we.getSelection(player);
		String[] pgroups = wg.getGroups(player);
		if (psel == null) {
			return true;
		}
		if (pgroups.length == 0) {
			return true;
		}
		int maxblocks = 0;
		for (String pgroup : pgroups) {
			pgroup = pgroup.toLowerCase();
			if (config.blocklimits.containsKey(pgroup)) {
				maxblocks = Math.max(maxblocks, config.blocklimits.get(pgroup));
			}
		}
		Vector min = psel.getNativeMinimumPoint();
		Vector max = psel.getNativeMaximumPoint();
		BigInteger size = BigInteger.ONE;
		size = size.multiply(BigInteger.valueOf(max.getBlockX() - min.getBlockX()));
		size = size.multiply(BigInteger.valueOf(max.getBlockZ() - min.getBlockZ()));
		size = size.multiply(BigInteger.valueOf(max.getBlockY() - min.getBlockY()));
		if (size.compareTo(BigInteger.valueOf(maxblocks)) > 0) {
			return false;
		}
		return true;
	}

}
