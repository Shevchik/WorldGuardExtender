package wgextender;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultIntegration implements Listener {

	private static final VaultIntegration instance = new VaultIntegration();

	public static VaultIntegration getInstance() {
		return instance;
	}

	private Permission permissions;
	private Economy economy;
	private Chat chat;

	public Permission getPermissions() {
		return permissions;
	}

	public Economy getEconomy() {
		return economy;
	}

	public Chat getChat() {
		return chat;
	}

	public void hook() {
		Bukkit.getServer().getPluginManager().registerEvents(this, WGExtender.getInstance());
		hook0();
	}

	protected void hook0() {
		try {
			economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
		} catch (Exception e) {
			economy = null;
		}
		try {
			permissions = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
			if (!permissions.hasGroupSupport()) {
				throw new IllegalStateException();
			}
		} catch (Exception e) {
			permissions = null;
		}
		try {
			chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
		} catch (Exception e) {
			chat = null;
		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		hook0();
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		hook0();
	}

}
