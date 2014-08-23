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

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

import WGExtender.Config;
import WGExtender.utils.WGRegionUtils;

public class EntityExplode implements Listener {

	private Config config;

	public EntityExplode(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent e) {
		if (!config.blockentityexplosionblockdamage) {
			return;
		}
		Iterator<Block> it = e.blockList().iterator();
		while (it.hasNext()) {
			if (WGRegionUtils.isInWGRegion(it.next().getLocation())) {
				it.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByExplosion(EntityDamageByEntityEvent e) {
		if ((e.getCause() == DamageCause.BLOCK_EXPLOSION) || (e.getCause() == DamageCause.ENTITY_EXPLOSION)) {
			if (!(e.getEntity() instanceof Player)) {
				if (WGRegionUtils.isInWGRegion(e.getEntity().getLocation())) {
					e.setCancelled(true);
				}
			}
		}
	}

}
