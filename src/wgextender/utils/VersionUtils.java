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

import org.bukkit.Bukkit;

public class VersionUtils {
    private static boolean isMC19(){
        return Bukkit.getBukkitVersion().contains("1.9");
    }

    private static boolean isMC18(){
        return Bukkit.getBukkitVersion().contains("1.8");
    }

    private static boolean isMC183(){
        return Bukkit.getBukkitVersion().contains("1.8.3");
    }

    private static boolean isMC17(){
        return Bukkit.getBukkitVersion().contains("1.7");
    }

    public static boolean isMC19OrNewer(){
        return isMC19() || !isMC18() && !isMC17();
    }

    public static boolean isMC18OrNewer(){
        return isMC18() || !isMC17();
    }

    public static boolean isMC183OrNewer(){
        return isMC183() || !isMC18() && !isMC17();
    }
}
