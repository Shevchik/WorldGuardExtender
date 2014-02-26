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

package WGExtender.regionprotect.flagbased;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import WGExtender.WGExtender;
import WGExtender.flags.EntityInteractRestrictFlag;
import WGExtender.utils.WGRegionUtils;

public class PlayerInteractEntities implements Listener {

	private WGExtender main;

	public PlayerInteractEntities(WGExtender main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (!WGRegionUtils.canBypassProtection(player)) {
			if (!WGRegionUtils.isFlagAllows(main.getWorldGuard(), player, entity, EntityInteractRestrictFlag.instance)) {
				event.setCancelled(true);
			}
		}
	}

}
