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

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.commands.region.RegionCommands;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.chat.BaseComponent;
import wgextender.Config;
import wgextender.utils.WGRegionUtils;

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

	protected static void setFlagsForRegion(final World world, final Config config, final String regionname) {
		final ProtectedRegion rg = getRegion(world, regionname);
		if (rg != null) {
			for (Entry<Flag<?>, String> entry : config.autoflags.entrySet()) {
				try {
					setFlag(world, rg, entry.getKey(), entry.getValue());
				} catch (CommandException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static final RegionCommands regionCommands = new RegionCommands(WorldGuardPlugin.inst());
	protected static final FakeConsoleComandSender fakeCommandSender = new FakeConsoleComandSender();
	protected static final Set<Character> flagCommandValueFlags = getFlagCommandValueFlags();
	public static <T> void setFlag(World world, ProtectedRegion region, Flag<T> flag, String value) throws CommandException {
		CommandContext ccontext = new CommandContext(String.format("flag %s -w %s %s %s", region.getId(), world.getName(), flag.getName(), value), flagCommandValueFlags);
		regionCommands.flag(ccontext, fakeCommandSender);
	}


	protected static Set<Character> getFlagCommandValueFlags() {
		try {
			Method method = RegionCommands.class.getMethod("flag", CommandContext.class, CommandSender.class);
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

	private static final class FakeConsoleComandSender implements ConsoleCommandSender {
		@Override
		public String getName() {
			return Bukkit.getConsoleSender().getName();
		}
		@Override
		public Server getServer() {
			return Bukkit.getServer();
		}
		@Override
		public void sendMessage(String arg0) {
		}
		@Override
		public void sendMessage(String[] arg0) {
		}
		@Override
		public PermissionAttachment addAttachment(Plugin arg0) {
			return null;
		}
		@Override
		public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
			return null;
		}
		@Override
		public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
			return null;
		}
		@Override
		public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
			return null;
		}
		@Override
		public Set<PermissionAttachmentInfo> getEffectivePermissions() {
			return Collections.emptySet();
		}
		@Override
		public boolean hasPermission(String arg0) {
			return true;
		}
		@Override
		public boolean hasPermission(Permission arg0) {
			return true;
		}
		@Override
		public boolean isPermissionSet(String arg0) {
			return true;
		}
		@Override
		public boolean isPermissionSet(Permission arg0) {
			return true;
		}
		@Override
		public void recalculatePermissions() {
		}
		@Override
		public void removeAttachment(PermissionAttachment arg0) {
		}
		@Override
		public boolean isOp() {
			return true;
		}
		@Override
		public void setOp(boolean arg0) {
		}
		@Override
		public void abandonConversation(Conversation arg0) {
		}
		@Override
		public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) {
		}
		@Override
		public void acceptConversationInput(String arg0) {
		}
		@Override
		public boolean beginConversation(Conversation arg0) {
			return false;
		}
		@Override
		public boolean isConversing() {
			return false;
		}
		@Override
		public void sendRawMessage(String arg0) {
		}

		@Override
		public Spigot spigot() {
			return new Spigot() {
				@Override
				public void sendMessage(BaseComponent component) {
				}

				@Override
				public void sendMessage(BaseComponent... components) {
				}
			};
		}
	}

}
