package WGExtender.regionprotect.regionbased;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import WGExtender.Config;
import WGExtender.WGExtender;
import WGExtender.utils.WGRegionUtils;

public class Pistons implements Listener {
	
	private WGExtender main;
	private Config config;
	
	
	public Pistons(WGExtender main, Config config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onExtend(BlockPistonExtendEvent e)
	{
		if (!config.blockpistonmoveblock) {return;}
		
		Location pistonlocation = e.getBlock().getLocation();
		Iterator<Block> bit = e.getBlocks().iterator();
		Block mblock = null;
		while (bit.hasNext())
		{
			mblock = bit.next();
			if (!WGRegionUtils.isInTheSameRegion(main.wg, pistonlocation, mblock.getLocation()))
			{
				e.setCancelled(true);
				break;
			}
		}
		if (mblock != null)
		{
			mblock = mblock.getRelative(e.getDirection());
			if (!WGRegionUtils.isInTheSameRegion(main.wg, pistonlocation, mblock.getLocation()))
			{
				e.setCancelled(true);
			}	
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onRetract(BlockPistonRetractEvent e)
	{
		if (!config.blockpistonmoveblock) {return;}

		if (e.isSticky())
		{
			Location pistonlocation = e.getBlock().getLocation();
			Location retractblocklocation = e.getRetractLocation();
			if (!WGRegionUtils.isInTheSameRegion(main.wg, pistonlocation, retractblocklocation))
			{
				e.setCancelled(true);
			}
		}
	}

}
