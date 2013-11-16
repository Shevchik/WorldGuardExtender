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

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import WGExtender.commands.Commands;
import WGExtender.regionprotect.AttackByPlayer;
import WGExtender.regionprotect.BlockBurn;
import WGExtender.regionprotect.EntityExplode;
import WGExtender.regionprotect.FireSpread;
import WGExtender.regionprotect.IgniteByPlayer;
import WGExtender.regionprotect.LiquidFlow;
import WGExtender.wgcommandprocess.RestrictCommandProcess;
import WGExtender.wgcommandprocess.WGCommandProcess;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin {
	
	private Config config;
	private WGCommandProcess cmdprocess;
	private RestrictCommandProcess rcmdprocess;
	private Commands commands;
	private LiquidFlow lflow;
	private IgniteByPlayer ignitebp;
	private FireSpread fspread;
	private BlockBurn bburn;
	private EntityExplode eexplode;

	
	public WorldEditPlugin we = null;
	public WorldGuardPlugin wg = null;
	
	@Override
	public void onEnable()
	{
		config = new Config();
		config.loadConfig();
		commands = new Commands(this,config);
		getCommand("wgex").setExecutor(commands);
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		cmdprocess = new WGCommandProcess(this, config);
		getServer().getPluginManager().registerEvents(cmdprocess, this);
		rcmdprocess = new RestrictCommandProcess(this, config);
		getServer().getPluginManager().registerEvents(rcmdprocess, this);
		lflow = new LiquidFlow(this,config);
		getServer().getPluginManager().registerEvents(lflow, this);
		ignitebp = new IgniteByPlayer(this,config);
		getServer().getPluginManager().registerEvents(ignitebp, this);
		fspread = new FireSpread(this,config);
		getServer().getPluginManager().registerEvents(fspread, this);
		bburn = new BlockBurn(this,config);
		getServer().getPluginManager().registerEvents(bburn, this);
		eexplode = new EntityExplode(this,config);
		getServer().getPluginManager().registerEvents(eexplode, this);
	}
	
	@Override
	public void onDisable()
	{
		config = null;
		cmdprocess = null;
		rcmdprocess = null;
		lflow = null;
		ignitebp = null;
		fspread = null;
		bburn = null;
		eexplode = null;
		commands = null;
		HandlerList.unregisterAll(this);
		we = null;
		wg = null;
	}

	
}
