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

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;

import WGExtender.Config;
import WGExtender.WGExtender;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AutoFlags {

	protected static void setFlagsForRegion(final Config config, World world, final String regionname) {
		final WorldGuardPlugin wg = WGExtender.getInstance().getWorldGuard();
		final RegionManager rm = wg.getRegionManager(world);
		if (rm == null) {
			return;
		}
		if (rm.hasRegion(regionname)) {
			return;
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(WGExtender.getInstance(), new Runnable() {
			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() {
				final ProtectedRegion rg = rm.getRegion(regionname);
				if (rg != null) {
					for (Entry<Flag<?>, String> entry : config.autoflags.entrySet()) {
						try {
							Flag flag = entry.getKey();
							rg.setFlag(flag, flag.parseInput(wg, Bukkit.getConsoleSender(), entry.getValue()));
						} catch (InvalidFlagFormat e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, 20);
	}

}
