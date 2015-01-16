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

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

public class BlockInteractRestrictWhitelistFlag extends Flag<String> {

	private static BlockInteractRestrictWhitelistFlag instance = null;
	public static BlockInteractRestrictWhitelistFlag getInstance() {
		return instance;
	}

	public static void injectFlag() {
		instance = new BlockInteractRestrictWhitelistFlag();
		FlagInjector.injectFlag(instance);
	}

	public static void uninjectFlag() {
		if (instance != null) {
			FlagInjector.uninjectFlag(instance);
			instance = null;
		}
	}

	public BlockInteractRestrictWhitelistFlag() {
		super("block-interact-whitelist");
	}

	@Override
	public Object marshal(String set) {
		return set;
	}

	@Override
	public String parseInput(WorldGuardPlugin wg, CommandSender sender, String args) throws InvalidFlagFormat {
		return args;
	}

	@Override
	public String unmarshal(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return null;
		}
	}

	private static Pattern splitWhiteSpace = Pattern.compile("\\s+");
	private static Pattern splitVertLine = Pattern.compile("[|]");
	private static Pattern splitColon = Pattern.compile("[:]");
	public static HashMap<Material, HashSet<Material>> parseWhitelist(String value) {
		HashMap<Material, HashSet<Material>> map = new HashMap<Material, HashSet<Material>>();
		String[] whitelistEntries = splitWhiteSpace.split(value);
		for (String whitelistEntry : whitelistEntries) {
			String[] allowedDataSplit = splitVertLine.split(whitelistEntry);
			String blockMaterialName = allowedDataSplit[0];
			Material blockmaterial = Material.getMaterial(blockMaterialName);
			if (blockmaterial != null) {
				map.put(blockmaterial, new HashSet<Material>());
				if (allowedDataSplit.length == 2) {
					String whitelistHandEntries = allowedDataSplit[1];
					String[] handMaterialNames = splitColon.split(whitelistHandEntries);
					for (String handMaterialName : handMaterialNames) {
						Material handmaterial = Material.getMaterial(handMaterialName);
						if (handmaterial != null) {
							map.get(blockmaterial).add(handmaterial);
						}
					}
				}
			}
		}
		return map;
	}

}
