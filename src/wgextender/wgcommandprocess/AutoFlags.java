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

package wgextender.wgcommandprocess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;

import wgextender.Config;
import wgextender.WGExtender;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AutoFlags {

	protected static boolean hasRegion(final World world, final String regionname) {
		return getRegion(world, regionname) != null;
	}

	protected static ProtectedRegion getRegion(final World world, final String regionname) {
		final RegionManager rm = WGExtender.getInstance().getWorldGuard().getRegionManager(world);
		if (rm == null) {
			return null;
		}
		return rm.getRegion(regionname);
	}

	protected static void setFlagsForRegion(final World world, final Config config, final String regionname) {
		final ProtectedRegion rg = getRegion(world, regionname);
		if (rg != null) {
			for (Entry<Flag<?>, String> entry : config.autoflags.entrySet()) {
				try {
					setFlag(rg, entry.getKey(), entry.getValue());
				} catch (InvalidFlagFormat | CommandException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final HashSet<Character> valueFlags = new HashSet<Character>(Arrays.asList(new Character[] {'g'}));
	public static <T> void setFlag(ProtectedRegion region, Flag<T> flag, String value) throws CommandException, InvalidFlagFormat {
		CommandContext ccontext = new CommandContext("rg "+value, valueFlags);
		region.setFlag(flag, flag.parseInput(WGExtender.getInstance().getWorldGuard(), Bukkit.getConsoleSender(), ccontext.getRemainingString(0)));
		if (ccontext.hasFlag('g')) {
			String group = ccontext.getFlag('g');
			RegionGroupFlag groupFlag = flag.getRegionGroupFlag();
			if (groupFlag == null) {
				throw new CommandException("Region flag '" + flag.getName() + "' does not have a group flag!");
			}
			try {
				RegionGroup groupValue = groupFlag.parseInput(WGExtender.getInstance().getWorldGuard(), Bukkit.getConsoleSender(), group);
				if (groupValue == groupFlag.getDefault()) {
					region.setFlag(groupFlag, null);
				} else {
					region.setFlag(groupFlag, groupValue);
				}
			} catch (InvalidFlagFormat e) {
				throw new CommandException(e.getMessage());
			}
		}
	}

}
