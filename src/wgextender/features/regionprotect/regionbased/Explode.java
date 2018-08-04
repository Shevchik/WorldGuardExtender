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

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

public class Explode implements Listener {

	protected final Config config;
	public Explode(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (!config.blockentityexplosionblockdamage) {
			return;
		}
		if (event.getEntity() instanceof TNTPrimed) {
			Entity source = ((TNTPrimed) event.getEntity()).getSource();
			if (source instanceof Player) {
				Player igniter = (Player) source;
				boolean canBypass = WGRegionUtils.canBypassProtection(igniter);
				Iterator<Block> it = event.blockList().iterator();
				while (it.hasNext()) {
					Location location = it.next().getLocation();
					if (!canBypass && !WGRegionUtils.canBuild(igniter, location)) {
						it.remove();
					}
				}
				return;
			}
		}
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (WGRegionUtils.isInWGRegion(it.next().getLocation())) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockExplode(BlockExplodeEvent event) {
		if (!config.blockentityexplosionblockdamage) {
			return;
		}
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (WGRegionUtils.isInWGRegion(it.next().getLocation())) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityDamageByExplosion(EntityDamageByEntityEvent e) {
		if (!config.blockentityexplosionblockdamage) {
			return;
		}
		if ((e.getCause() == DamageCause.BLOCK_EXPLOSION) || (e.getCause() == DamageCause.ENTITY_EXPLOSION)) {
			if (WGRegionUtils.isInWGRegion(e.getEntity().getLocation())) {
				e.setCancelled(true);
			}
		}
	}

}
