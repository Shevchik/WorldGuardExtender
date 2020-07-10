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

package wgextender.features.claimcommand;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.World;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.commands.region.RegionCommands;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import wgextender.Config;
import wgextender.utils.WGRegionUtils;

@SuppressWarnings("deprecation")
public class AutoFlags {

	protected static boolean hasRegion(final World world, final String regionname) {
		return getRegion(world, regionname) != null;
	}

	protected static ProtectedRegion getRegion(final World world, final String regionname) {
		final RegionManager rm = WGRegionUtils.getRegionManager(world);
		if (rm == null) {
			return null;
		}
		return rm.getRegion(regionname);
	}

	protected static void setFlagsForRegion(Actor actor, final World world, final Config config, final String regionname) {
		final ProtectedRegion rg = getRegion(world, regionname);
		if (rg != null) {
			for (Entry<Flag<?>, String> entry : config.claimAutoFlags.entrySet()) {
				try {
					setFlag(actor, world, rg, entry.getKey(), entry.getValue());
				} catch (CommandException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static final RegionCommands regionCommands = new RegionCommands(WorldGuard.getInstance());
	protected static final Set<Character> flagCommandValueFlags = getFlagCommandValueFlags();
	public static <T> void setFlag(Actor actor, World world, ProtectedRegion region, Flag<T> flag, String value) throws CommandException {
		CommandContext ccontext = new CommandContext(String.format("flag %s -w %s %s %s", region.getId(), world.getName(), flag.getName(), value), flagCommandValueFlags);
		regionCommands.flag(ccontext, actor);
	}


	protected static Set<Character> getFlagCommandValueFlags() {
		try {
			Method method = RegionCommands.class.getMethod("flag", CommandContext.class, Actor.class);
			Command annotation = method.getAnnotation(Command.class);
			char[] flags = annotation.flags().toCharArray();
			Set<Character> valueFlags = new HashSet<>();
			for (int i = 0; i < flags.length; ++i) {
				if ((flags.length > (i + 1)) && (flags[i + 1] == ':')) {
					valueFlags.add(flags[i]);
					++i;
				}
			}
			return valueFlags;
		} catch (Throwable t) {
			t.printStackTrace();
			return Collections.emptySet();
		}
	}

}
