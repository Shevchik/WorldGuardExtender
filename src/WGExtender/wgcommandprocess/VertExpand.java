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

package WGExtender.wgcommandprocess;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

public class VertExpand {

	protected static void expand(WorldEditPlugin we, Player player)
	{
		if (we.getSelection(player) == null)
		{
			return;
		}
		else
		{
			try {
				LocalPlayer localplayer = we.wrapPlayer(player);
				LocalSession session = we.getSession(player);
				Region region = session.getSelection(localplayer.getWorld());
				region.expand(
						new Vector(0, (localplayer.getWorld().getMaxY() + 1), 0),
						new Vector(0, -(localplayer.getWorld().getMaxY() + 1), 0)
						);
				session.getRegionSelector(localplayer.getWorld()).learnChanges();;
                session.getRegionSelector(localplayer.getWorld()).explainRegionAdjust(localplayer, session);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}
	
}
