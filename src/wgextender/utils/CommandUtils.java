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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class CommandUtils {

	@SuppressWarnings("unchecked")
	public static Map<String, Command> getCommands() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CommandMap commandMap = getCommandMap();
		Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
		knownCommandsField.setAccessible(true);
		return (Map<String, Command>) knownCommandsField.get(commandMap);
	}

	public static void unregisterCommand(Plugin owningplugin, Command command) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		command.unregister(getCommandMap());
		Map<String, Command> commands = getCommands();
		for (String name : getCommandNames(owningplugin, command)) {
			if (commands.get(name) == command) {
				commands.remove(name);
			}
		}
	}

	public static void registerCommand(Plugin owningplugin, Command command) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<String, Command> commands = getCommands();
		for (String name : getCommandNames(owningplugin, command)) {
			if (!commands.containsKey(name)) {
				commands.put(name, command);
			}
		}
		command.register(getCommandMap());
	}

	private static List<String> getCommandNames(Plugin owningplugin, Command command) {
		String pluginname = owningplugin.getName().toLowerCase();
		ArrayList<String> names = new ArrayList<String>();
		names.add(command.getName());
		names.add(pluginname+":"+command.getName());
		for (String alias : command.getAliases()) {
			names.add(alias);
			names.add(pluginname+":"+alias);
		}
		return names;
	}

	private static CommandMap getCommandMap() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PluginManager pm = Bukkit.getPluginManager();
		Field commandMapField = pm.getClass().getDeclaredField("commandMap");
		commandMapField.setAccessible(true);
		return (CommandMap) commandMapField.get(pm);
	}

}
