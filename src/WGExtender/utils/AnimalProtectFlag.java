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

package WGExtender.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class AnimalProtectFlag extends StateFlag {
	
	public static AnimalProtectFlag instance = new AnimalProtectFlag();
	
	public static void injectFlag() {
        try {
            Field field = DefaultFlag.class.getDeclaredField("flagsList");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);

            List<Flag<?>> flags = new ArrayList<Flag<?>>(Arrays.asList(DefaultFlag.getFlags()));
            flags.add(instance);
            field.set(null, flags.toArray(new Flag[flags.size()]));
            
            WorldGuardPlugin.class.cast(Bukkit.getPluginManager().getPlugin("WorldGuard")).getGlobalRegionManager().preload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public AnimalProtectFlag() {
		super("damage-animals", true);
	}

}
