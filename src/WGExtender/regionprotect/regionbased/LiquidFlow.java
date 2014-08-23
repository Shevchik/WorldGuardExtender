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
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;

import WGExtender.Config;
import WGExtender.utils.WGRegionUtils;

public class LiquidFlow implements Listener {

	private Config config;

	public LiquidFlow(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onLiquidFlow(BlockFromToEvent e) {
		Block b = e.getBlock();
		if ((b.getType() == Material.LAVA) || (b.getType() == Material.STATIONARY_LAVA)) {
			if (config.blocklavaflow) {
				if (!WGRegionUtils.isInTheSameRegion(b.getLocation(), e.getToBlock().getLocation())) {
					e.setCancelled(true);
				}
			}
		} else if ((b.getType() == Material.WATER) || (b.getType() == Material.STATIONARY_WATER)) {
			if (config.blockwaterflow) {
				if (!WGRegionUtils.isInTheSameRegion(b.getLocation(), e.getToBlock().getLocation())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onDispenserDispense(BlockDispenseEvent e) {
		ItemStack item = e.getItem();
		Block b = e.getBlock();
		Block nextBlock = b.getRelative(DirectionalContainer.class.cast(b.getState().getData()).getFacing());
		if (item.getType() == Material.LAVA_BUCKET) {
			if (config.blocklavaflow) {
				if (!WGRegionUtils.isInTheSameRegion(b.getLocation(), nextBlock.getLocation())) {
					e.setCancelled(true);
				}
			}
		} else if (item.getType() == Material.WATER_BUCKET) {
			if (config.blockwaterflow) {
				if (!WGRegionUtils.isInTheSameRegion(b.getLocation(), nextBlock.getLocation())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
