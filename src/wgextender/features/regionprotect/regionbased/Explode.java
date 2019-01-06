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
import java.util.function.Predicate;

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
import org.bukkit.event.entity.EntityDamageEvent;
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
		if (!config.checkExplosionBlockDamage) {
			return;
		}
		Player source = findExplosionSource(event.getEntity());
		Predicate<Location> shouldProtectBlockPredicate = null;
		if (source != null) {
			boolean canBypass = WGRegionUtils.canBypassProtection(source);
			shouldProtectBlockPredicate = location -> !canBypass && !WGRegionUtils.canBuild(source, location);
		} else {
			shouldProtectBlockPredicate = WGRegionUtils::isInWGRegion;
		}
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
			if (shouldProtectBlockPredicate.test(it.next().getLocation())) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockExplode(BlockExplodeEvent event) {
		if (!config.checkExplosionBlockDamage) {
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
	public void onEntityDamageByExplosion(EntityDamageEvent e) {
		if (!config.checkExplosionEntityDamage) {
			return;
		}
		if ((e.getCause() == DamageCause.BLOCK_EXPLOSION) || (e.getCause() == DamageCause.ENTITY_EXPLOSION)) {
			Location locaiton = e.getEntity().getLocation();
			if (WGRegionUtils.isInWGRegion(locaiton)) {
				if (e instanceof EntityDamageByEntityEvent) {
					Player source = findExplosionSource(((EntityDamageByEntityEvent) e).getDamager());
					if ((source == null) || (!WGRegionUtils.canBypassProtection(source) && !WGRegionUtils.canBuild(source, locaiton))) {
						e.setCancelled(true);
					}
				} else {
					e.setCancelled(true);
				}
			}

		}
	}

	protected static Player findExplosionSource(Entity exploded) {
		if (exploded instanceof TNTPrimed) {
			Entity source = ((TNTPrimed) exploded).getSource();
			if (source instanceof Player) {
				return (Player) source;
			}
		}
		//TODO: explosion source for creeper (last damager or target?)?
		return null;
	}

}
