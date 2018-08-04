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

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldConfiguration;
import com.sk89q.worldguard.config.ConfigurationManager;

public class WEUtils {

	public static WorldEditPlugin getWorldEditPlugin() {
		return JavaPlugin.getPlugin(WorldEditPlugin.class);
	}

    public static ConfigurationManager getConfig() {
        return WorldGuard.getInstance().getPlatform().getGlobalStateManager();
    }

    public static BukkitWorldConfiguration getWorldConfig(World world) {
        return (BukkitWorldConfiguration) WorldGuard.getInstance().getPlatform().getGlobalStateManager().get(BukkitAdapter.adapt(world));
    }

    public static BukkitWorldConfiguration getWorldConfig(Player player) {
        return getWorldConfig(player.getWorld());
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
				new Vector(0, (weworld.getMaxY() + 1), 0),
				new Vector(0, -(weworld.getMaxY() + 1), 0)
			);
            session.getRegionSelector(weworld).learnChanges();
            return true;
		} catch (Throwable e) {
		}
        return false;
	}

}
