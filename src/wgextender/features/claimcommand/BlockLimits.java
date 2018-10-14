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

package wgextender.features.claimcommand;

import java.math.BigInteger;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;

import wgextender.Config;
import wgextender.VaultIntegration;
import wgextender.utils.WEUtils;

public class BlockLimits {

	public ProcessedClaimInfo processClaimInfo(Config config, Player player) {
		ProcessedClaimInfo info = new ProcessedClaimInfo();
		Region psel;
		try {
			psel = WEUtils.getSelection(player);
		} catch (IncompleteRegionException e) {
			return info;
		}
		Vector min = psel.getMinimumPoint();
		Vector max = psel.getMaximumPoint();
		BigInteger size = BigInteger.ONE;
		size = size.multiply(BigInteger.valueOf(max.getBlockX()).subtract(BigInteger.valueOf(min.getBlockX())).add(BigInteger.ONE));
		size = size.multiply(BigInteger.valueOf(max.getBlockZ()).subtract(BigInteger.valueOf(min.getBlockZ())).add(BigInteger.ONE));
		size = size.multiply(BigInteger.valueOf(max.getBlockY()).subtract(BigInteger.valueOf(min.getBlockY())).add(BigInteger.ONE));
		if (size.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			info.disallow();
			info.setInfo(size, BigInteger.valueOf(-1));
			return info;
		}
		if (config.blocklimitsenabled) {
			if (player.hasPermission("worldguard.region.unlimited")) {
				return info;
			}
			String[] pgroups = VaultIntegration.getInstance().getPermissions().getPlayerGroups(player);
			if (pgroups.length == 0) {
				return info;
			}
			int maxblocks = 0;
			for (String pgroup : pgroups) {
				pgroup = pgroup.toLowerCase();
				if (config.blocklimits.containsKey(pgroup)) {
					maxblocks = Math.max(maxblocks, config.blocklimits.get(pgroup));
				}
			}
			BigInteger maxblocksi = BigInteger.valueOf(maxblocks);
			if (size.compareTo(maxblocksi) > 0) {
				info.disallow();
				info.setInfo(size, maxblocksi);
				return info;
			}
		}
		return info;
	}

	protected static class ProcessedClaimInfo {

		private boolean claimAllowed = true;
		private BigInteger size;
		private BigInteger maxsize;

		public void disallow() {
			claimAllowed = false;
		}

		public boolean isClaimAllowed() {
			return claimAllowed;
		}

		public void setInfo(BigInteger claimed, BigInteger max) {
			size = claimed;
			maxsize = max;
		}

		public String getClaimedSize() {
			return size.toString();
		}

		public String getMaxSize() {
			return maxsize.toString();
		}

	}

}
