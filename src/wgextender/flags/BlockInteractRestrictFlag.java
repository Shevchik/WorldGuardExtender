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

package wgextender.flags;

import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class BlockInteractRestrictFlag extends StateFlag {

	private static BlockInteractRestrictFlag instance = null;
	public static BlockInteractRestrictFlag getInstance() {
		return instance;
	}

	public static void injectFlag() {
		instance = new BlockInteractRestrictFlag();
		FlagInjector.injectFlag(instance);
	}

	public static void uninjectFlag() {
		if (instance != null) {
			FlagInjector.uninjectFlag(instance);
			instance = null;
		}
	}

	public BlockInteractRestrictFlag() {
		super("block-interact", true, RegionGroup.NON_MEMBERS);
	}

}
