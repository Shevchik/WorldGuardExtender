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

package WGExtender.wgcommandprocess.vertexpand;

import org.bukkit.entity.Player;

import WGExtender.WGExtender;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.regions.Region;

public class WeAPIVertExpand implements VertExpandInterface {

	@Override
	public boolean expand(Player player) {
		LocalPlayer weplayer = WGExtender.getInstance().getWorldEdit().wrapPlayer(player);
		LocalSession session = WGExtender.getInstance().getWorldEdit().getSession(player);
        try {
			Region region = session.getSelection(weplayer.getWorld());
			region.expand(
				new Vector(0, (weplayer.getWorld().getMaxY() + 1), 0),
				new Vector(0, -(weplayer.getWorld().getMaxY() + 1), 0)
			);
            session.getRegionSelector(weplayer.getWorld()).learnChanges();
            session.getRegionSelector(weplayer.getWorld()).explainRegionAdjust(weplayer, session);
            return true;
		} catch (Exception e) {
		}
        return false;
	}

}
