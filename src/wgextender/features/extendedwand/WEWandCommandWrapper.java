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

package wgextender.features.extendedwand;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wgextender.Config;
import wgextender.utils.CommandUtils;

public class WEWandCommandWrapper extends Command {

	public static void inject(Config config) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		WEWandCommandWrapper wrapper = new WEWandCommandWrapper(config, CommandUtils.getCommands().get("/wand"));
		CommandUtils.replaceComamnd(wrapper.originalcommand, wrapper);
	}

	public static void uninject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {
		WEWandCommandWrapper wrapper = (WEWandCommandWrapper) CommandUtils.getCommands().get("/wand");
		CommandUtils.replaceComamnd(wrapper, wrapper.originalcommand);
	}

	private Config config;
	private Command originalcommand;

	private WEWandCommandWrapper(Config config, Command originalcommand) {
		super(originalcommand.getName(), originalcommand.getDescription(), originalcommand.getUsage(), originalcommand.getAliases());
		this.config = config;
		this.originalcommand = originalcommand;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!config.extendedwewand) {
			return originalcommand.execute(sender, label, args);
		}
		if (sender instanceof Player) {
			((Player) sender).getInventory().addItem(WEWand.getWand());
			sender.sendMessage(ChatColor.LIGHT_PURPLE+"Выдана вещь для выделения территории");
		}
		return true;
	}

}
