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

package WGExtender.regionprotect.regionbased;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

import WGExtender.Config;
import WGExtender.WGExtender;
import WGExtender.utils.WGRegionUtils;

public class FireSpread implements Listener {

	private WGExtender main;
	private Config config;

	public FireSpread(WGExtender main, Config config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockIgniteBySpread(BlockSpreadEvent e) {
		if (e.getNewState().getType() == Material.FIRE) {
			if (config.blockfirespreadtoregion) {
				if (!WGRegionUtils.isInTheSameRegion(main.getWorldGuard(), e.getSource().getLocation(), e.getBlock().getLocation())) {
					e.setCancelled(true);
				}
			}
			if (config.blockfirespreadinregion) {
				if (WGRegionUtils.isInWGRegion(main.getWorldGuard(), e.getSource().getLocation()) && WGRegionUtils.isInTheSameRegion(main.getWorldGuard(), e.getSource().getLocation(), e.getBlock().getLocation())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
