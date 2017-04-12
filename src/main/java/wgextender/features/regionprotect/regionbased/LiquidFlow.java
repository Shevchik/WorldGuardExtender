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

package wgextender.features.regionprotect.regionbased;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class LiquidFlow implements Listener {

	private Config config;

	public LiquidFlow(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onLiquidFlow(BlockFromToEvent event) {
		Block b = event.getBlock();
		switch (b.getType()) {
			case LAVA:
			case STATIONARY_LAVA: {
				if (config.blocklavaflow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
				break;
			}
			case WATER:
			case STATIONARY_WATER: {
				if (config.blockwaterflow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
				break;
			}
			default: {
				if (config.blockotherliquidflow) {
					check(b.getLocation(), event.getToBlock().getLocation(), event);
				}
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onDispenserDispense(BlockDispenseEvent event) {
		ItemStack item = event.getItem();
		Block dispenser = event.getBlock();
		MaterialData mdata = dispenser.getState().getData();
		if (mdata instanceof DirectionalContainer) {
			Block nextBlock = dispenser.getRelative(((DirectionalContainer) mdata).getFacing());
			switch (item.getType()) {
				case LAVA_BUCKET: {
					if (config.blocklavaflow) {
						check(dispenser.getLocation(), nextBlock.getLocation(), event);
					}
					break;
				}
				case WATER_BUCKET: {
					if (config.blockwaterflow) {
						check(dispenser.getLocation(), nextBlock.getLocation(), event);
					}
					break;
				}
				default: {
					if (config.blockotherliquidflow) {
						check(dispenser.getLocation(), nextBlock.getLocation(), event);
					}
					break;
				}
			}
		}
	}

	private void check(Location source, Location to, Cancellable event) {
		if (!WGRegionUtils.isInTheSameRegionOrWild(source, to)) {
			event.setCancelled(true);
		}
	}

}
