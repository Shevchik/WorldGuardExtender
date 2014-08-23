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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import WGExtender.Config;
import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.utils.WGRegionUtils;

public class PlayerInteractBlocks implements Listener {

	private Config config;

	public PlayerInteractBlocks(Config config) {
		this.config = config;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!config.blockrestrictflag) {
			return;
		}
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if (!WGRegionUtils.canBypassProtection(player)) {
			if (!WGRegionUtils.isFlagAllows( player, block, BlockInteractRestrictFlag.instance)) {
				e.setCancelled(true);
			}
		}
	}

}
