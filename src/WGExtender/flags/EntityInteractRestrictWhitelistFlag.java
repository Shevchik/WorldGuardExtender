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

import java.util.HashSet;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

public class EntityInteractRestrictWhitelistFlag extends Flag<String> {

	private static EntityInteractRestrictWhitelistFlag instance = null;
	public static EntityInteractRestrictWhitelistFlag getInstance() {
		return instance;
	}

	public static void injectFlag() {
		instance = new EntityInteractRestrictWhitelistFlag();
		FlagInjector.injectFlag(instance);
	}

	public static void uninjectFlag() {
		if (instance != null) {
			FlagInjector.uninjectFlag(instance);
			instance = null;
		}
	}

	public EntityInteractRestrictWhitelistFlag() {
		super("entity-interact-whitelist");
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
	public static HashSet<EntityType> parseWhitelist(String value) {
		HashSet<EntityType> set = new HashSet<EntityType>();
		String[] entityTypeNames = splitWhiteSpace.split(value);
		for (String entityTypeName : entityTypeNames) {
			EntityType entityType = EntityType.fromName(entityTypeName);
			if (entityType != null) {
				set.add(entityType);
			}
		}
		return set;
	}

}
