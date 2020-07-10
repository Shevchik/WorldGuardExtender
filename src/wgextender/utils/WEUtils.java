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

package wgextender.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

public class WEUtils {

	public static WorldEditPlugin getWorldEditPlugin() {
		return JavaPlugin.getPlugin(WorldEditPlugin.class);
	}

    public static Region getSelection(Player player) throws IncompleteRegionException {
		return getWorldEditPlugin().getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
	}

	public static boolean expandVert(Player player) {
		LocalSession session = getWorldEditPlugin().getSession(player);
		com.sk89q.worldedit.world.World weworld = BukkitAdapter.adapt(player.getWorld());
        try {
			Region region = session.getSelection(weworld);
			region.expand(
				BlockVector3.at(0, (weworld.getMaxY() + 1), 0),
				BlockVector3.at(0, -(weworld.getMaxY() + 1), 0)
			);
            session.getRegionSelector(weworld).learnChanges();
            return true;
		} catch (Throwable e) {
		}
        return false;
	}

}
