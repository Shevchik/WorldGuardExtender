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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;

public class CommandUtils {

	@SuppressWarnings("unchecked")
	public static Map<String, Command> getCommands() throws IllegalAccessException {
		CommandMap commandMap = getCommandMap();
		return (Map<String, Command>) ReflectionUtils.getField(commandMap.getClass(), "knownCommands").get(commandMap);
	}

	public static List<String> getCommandAliases(String commandname) {
		try {
			Map<String, Command> commands = getCommands();
			Command command = commands.get(commandname);
			if (command == null) {
				return Collections.singletonList(commandname);
			} else {
				ArrayList<String> aliases = new ArrayList<>();
				for (Entry<String, Command> entry : getCommands().entrySet()) {
					if (entry.getValue() == command) {
						aliases.add(entry.getKey());
					}
				}
				return aliases;
			}
		} catch (Throwable t) {
			return Collections.singletonList(commandname);
		}
	}

	public static void replaceComamnd(Command oldcommand, Command newcommand) throws IllegalAccessException {
		Iterator<Entry<String, Command>> iterator = getCommands().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Command> entry = iterator.next();
			if (entry.getValue() == oldcommand) {
				entry.setValue(newcommand);
			}
		}
	}

	protected static CommandMap getCommandMap() throws IllegalAccessException {
		PluginManager pm = Bukkit.getPluginManager();
		return (CommandMap) ReflectionUtils.getField(pm.getClass(), "commandMap").get(pm);
	}

}
