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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class Pistons implements Listener {

	private Config config;

	public Pistons(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onExtend(BlockPistonExtendEvent event) {
		if (!config.blockpistonmoveblock) {
			return;
		}
		Location pistonlocation = event.getBlock().getLocation();
		for (Block block : event.getBlocks()) {
			if (
				!WGRegionUtils.isInTheSameRegionOrWild(pistonlocation, block.getLocation()) ||
				!WGRegionUtils.isInTheSameRegionOrWild(pistonlocation, block.getRelative(event.getDirection()).getLocation())
			) {
				event.setCancelled(true);
				break;
			}
		}
	}

	private boolean isSlimeRetractAvailable = true;

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onRetract(BlockPistonRetractEvent event) {
		if (!config.blockpistonmoveblock) {
			return;
		}
		Location pistonlocation = event.getBlock().getLocation();
		if (event.isSticky()) {
			if (isSlimeRetractAvailable) {
				try {
					for (Block block : event.getBlocks()) {
						if (
							!WGRegionUtils.isInTheSameRegionOrWild(pistonlocation, block.getLocation()) ||
							!WGRegionUtils.isInTheSameRegionOrWild(pistonlocation, block.getRelative(event.getDirection()).getLocation())
						) {
							event.setCancelled(true);
							break;
						}
					}
					return;
				} catch (NoSuchMethodError e) {
					isSlimeRetractAvailable = false;
				}
			}
			if (!WGRegionUtils.isInTheSameRegionOrWild(event.getBlock().getLocation(), event.getRetractLocation())) {
				event.setCancelled(true);
			}
		}
	}

}
