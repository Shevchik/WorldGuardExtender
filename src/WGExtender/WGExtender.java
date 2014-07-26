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
import WGExtender.flags.EntityInteractRestrictFlag;
import WGExtender.flags.EntityInteractRestrictWhitelistFlag;
import WGExtender.regionprotect.flagbased.AttackByPlayer;
import WGExtender.regionprotect.flagbased.PlayerInteractBlocks;
import WGExtender.regionprotect.flagbased.PlayerInteractEntities;
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
	private Commands commands;
	private WGCommandProcess cmdprocess;
	private RestrictCommandProcess rcmdprocess;
	private IgniteByPlayer ignitebp;
	private LiquidFlow lflow;
	private FireSpread fspread;
	private BlockBurn bburn;
	private EntityExplode eexplode;
	private Pistons pistons;
	private AttackByPlayer attackbp;
	private PlayerInteractBlocks pinteractb;
	private PlayerInteractEntities pinteracte;

	private WorldEditPlugin we = null;
	public WorldEditPlugin getWorldEdit() {
		return we;
	}
	private WorldGuardPlugin wg = null;
	public WorldGuardPlugin getWorldGuard() {
		return wg;
	}

	@Override
	public void onEnable() {
		log = this.getLogger();
		AnimalProtectFlag.injectFlag();
		BlockInteractRestrictFlag.injectFlag();
		BlockInteractRestrictWhitelistFlag.injectFlag();
		EntityInteractRestrictFlag.injectFlag();
		EntityInteractRestrictWhitelistFlag.injectFlag();
		config = new Config(this);
		config.loadConfig();
		commands = new Commands(this, config);
		getCommand("wgex").setExecutor(commands);
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
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
		pistons = new Pistons(this, config);
		getServer().getPluginManager().registerEvents(pistons, this);
		eexplode = new EntityExplode(this, config);
		getServer().getPluginManager().registerEvents(eexplode, this);
		attackbp = new AttackByPlayer(this, config);
		getServer().getPluginManager().registerEvents(attackbp, this);
		pinteractb = new PlayerInteractBlocks(this, config);
		getServer().getPluginManager().registerEvents(pinteractb, this);
		pinteracte = new PlayerInteractEntities(this, config);
		getServer().getPluginManager().registerEvents(pinteracte, this);
	}

	@Override
	public void onDisable() {
		AnimalProtectFlag.uninjectFlag();
		BlockInteractRestrictFlag.uninjectFlag();
		BlockInteractRestrictWhitelistFlag.uninjectFlag();
		EntityInteractRestrictFlag.uninjectFlag();
		EntityInteractRestrictWhitelistFlag.uninjectFlag();
		cmdprocess = null;
		rcmdprocess = null;
		ignitebp = null;
		lflow = null;
		fspread = null;
		bburn = null;
		eexplode = null;
		commands = null;
		attackbp = null;
		pinteractb = null;
		pinteracte = null;
		config = null;
		we = null;
		wg = null;
	}

	public static void log(Level level, String message) {
		if (log != null) {
			log.log(Level.SEVERE, message);
		}
	}

}
