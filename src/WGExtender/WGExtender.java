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

package WGExtender;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import WGExtender.commands.Commands;
import WGExtender.flags.AnimalProtectFlag;
import WGExtender.flags.BlockInteractRestrictFlag;
import WGExtender.flags.BlockInteractRestrictWhitelistFlag;
import WGExtender.regionprotect.flagbased.AttackByPlayer;
import WGExtender.regionprotect.flagbased.PlayerInteractBlocks;
import WGExtender.regionprotect.ownormembased.IgniteByPlayer;
import WGExtender.regionprotect.regionbased.BlockBurn;
import WGExtender.regionprotect.regionbased.EntityExplode;
import WGExtender.regionprotect.regionbased.FireSpread;
import WGExtender.regionprotect.regionbased.LiquidFlow;
import WGExtender.regionprotect.regionbased.Pistons;
import WGExtender.wgcommandprocess.RestrictCommandProcess;
import WGExtender.wgcommandprocess.WGCommandProcess;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGExtender extends JavaPlugin {

	private static Logger log;

	private Config config;
	private WGCommandProcess cmdprocess;
	private RestrictCommandProcess rcmdprocess;
	private Commands commands;
	private LiquidFlow lflow;
	private IgniteByPlayer ignitebp;
	private FireSpread fspread;
	private BlockBurn bburn;
	private EntityExplode eexplode;
	private AttackByPlayer attackbp;
	private PlayerInteractBlocks pinteractb;
	private Pistons pistons;

	public WorldEditPlugin we = null;
	public WorldGuardPlugin wg = null;

	@Override
	public void onEnable() {
		log = this.getLogger();
		AnimalProtectFlag.injectFlag();
		BlockInteractRestrictFlag.injectFlag();
		BlockInteractRestrictWhitelistFlag.injectFlag();
		config = new Config(this);
		config.loadConfig();
		commands = new Commands(this, config);
		getCommand("wgex").setExecutor(commands);
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin(
				"WorldGuard");
		cmdprocess = new WGCommandProcess(this, config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
		rcmdprocess = new RestrictCommandProcess(this, config);
		getServer().getPluginManager().registerEvents(rcmdprocess, this);
		lflow = new LiquidFlow(this, config);
		getServer().getPluginManager().registerEvents(lflow, this);
		ignitebp = new IgniteByPlayer(this, config);
		getServer().getPluginManager().registerEvents(ignitebp, this);
		fspread = new FireSpread(this, config);
		getServer().getPluginManager().registerEvents(fspread, this);
		bburn = new BlockBurn(this, config);
		getServer().getPluginManager().registerEvents(bburn, this);
		eexplode = new EntityExplode(this, config);
		getServer().getPluginManager().registerEvents(eexplode, this);
		attackbp = new AttackByPlayer(this);
		getServer().getPluginManager().registerEvents(attackbp, this);
		pinteractb = new PlayerInteractBlocks(this);
		getServer().getPluginManager().registerEvents(pinteractb, this);
		pistons = new Pistons(this, config);
		getServer().getPluginManager().registerEvents(pistons, this);
	}

	@Override
	public void onDisable() {
		AnimalProtectFlag.uninjectFlag();
		BlockInteractRestrictFlag.uninjectFlag();
		BlockInteractRestrictWhitelistFlag.uninjectFlag();
		config = null;
		cmdprocess = null;
		rcmdprocess = null;
		lflow = null;
		ignitebp = null;
		fspread = null;
		bburn = null;
		eexplode = null;
		commands = null;
		attackbp = null;
		we = null;
		wg = null;
	}

	public static void log(Level level, String message) {
		if (log != null) {
			log.log(Level.SEVERE, message);
		}
	}
}
