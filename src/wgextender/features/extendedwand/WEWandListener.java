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

package wgextender.features.extendedwand;

import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class WEWandListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		Entity edamager = event.getDamager();
		if (edamager instanceof org.bukkit.entity.Player) {
			ItemStack item = ((Player) edamager).getInventory().getItemInMainHand();
			if (WEWand.isWand(item)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Iterator<ItemStack> dropsit = event.getDrops().iterator();
		while (dropsit.hasNext()) {
			ItemStack item = dropsit.next();
			if (WEWand.isWand(item)) {
				dropsit.remove();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onItemDrop(PlayerDropItemEvent event) {
		Item drop = event.getItemDrop();
		if (WEWand.isWand(drop.getItemStack())) {
			drop.remove();
		}
	}

}
