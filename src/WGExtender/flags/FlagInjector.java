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

package WGExtender.flags;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import WGExtender.WGExtender;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;

public class FlagInjector {

	protected static void injectFlag(Flag<?> flagtoinject) {
		try {
			Field field = DefaultFlag.class.getDeclaredField("flagsList");
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.setAccessible(true);
			List<Flag<?>> flags = new ArrayList<Flag<?>>(Arrays.asList(DefaultFlag.getFlags()));
			flags.add(flagtoinject);
			field.set(null, flags.toArray(new Flag[flags.size()]));
			WorldGuardPlugin.class.cast(Bukkit.getPluginManager().getPlugin("WorldGuard")).getGlobalRegionManager().preload();
		} catch (Exception e) {
			WGExtender.log(Level.SEVERE, "Failed to inject flag " + flagtoinject.getName());
			e.printStackTrace();
		}
	}

	protected static void uninjectFlag(Flag<?> flagtouninject) {
		try {
			Field field = DefaultFlag.class.getDeclaredField("flagsList");
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.setAccessible(true);
			List<Flag<?>> flags = new ArrayList<Flag<?>>(Arrays.asList(DefaultFlag.getFlags()));
			Iterator<Flag<?>> flagit = flags.iterator();
			while (flagit.hasNext()) {
				Flag<?> flag = flagit.next();
				if (flag.getName().equalsIgnoreCase(flagtouninject.getName())) {
					flagit.remove();
					break;
				}
			}
			field.set(null, flags.toArray(new Flag[flags.size()]));
			WorldGuardPlugin.class.cast(Bukkit.getPluginManager().getPlugin("WorldGuard")).getGlobalRegionManager().preload();
		} catch (Exception e) {
			WGExtender.log(Level.SEVERE, "Failed to uninject flag " + flagtouninject.getName());
			e.printStackTrace();
		}
	}

}
