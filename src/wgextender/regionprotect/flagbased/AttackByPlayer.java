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

package wgextender.regionprotect.flagbased;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import wgextender.Config;
import wgextender.flags.AnimalProtectFlag;
import wgextender.utils.WGRegionUtils;

public class AttackByPlayer implements Listener {

	private Config config;

	public AttackByPlayer(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!config.animalprotectflag) {
			return;
		}
		Entity entity = event.getEntity();
		if (((entity instanceof Animals) || (entity instanceof Villager)) && WGRegionUtils.isInWGRegion(entity.getLocation())) {
			Player damagerplayer = null;
			Entity edamager = event.getDamager();
			if (edamager instanceof Player) {
				damagerplayer = (Player) edamager;
			} else if (edamager instanceof Arrow) {
				Arrow arrow = (Arrow) edamager;
				if (arrow.getShooter() instanceof Player) {
					damagerplayer = (Player) arrow.getShooter();
				}
			}
			if (damagerplayer != null) {
				if (!WGRegionUtils.canBypassProtection(damagerplayer)) {
					if (!WGRegionUtils.isFlagAllows(damagerplayer, entity.getLocation(), AnimalProtectFlag.getInstance())) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
