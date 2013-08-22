package WGExtender.regionprotect;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import WGExtender.Config;
import WGExtender.Main;

public class FireSpread implements Listener {

	private Main main;
	private Config config;

	
	public FireSpread(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onBlockIgnite(BlockIgniteEvent e)
	{
		if (e.getCause() == IgniteCause.SPREAD)
		{
			if (config.blockfirespreadtoregion)
			{//check to region
				if (!allowFireSpreadToRegion(e.getIgnitingBlock(),e.getBlock()))
				{
					e.setCancelled(true);
				}
			}
			if (config.blockfirespreadinregion)
			{//check in region
				if (!allowFireSpreadInRegion(e.getIgnitingBlock(),e.getBlock()))
				{
					e.setCancelled(true);
				}
			}
		}
	}
	
	private boolean allowFireSpreadToRegion(Block from, Block to)
	{
		if (WGRPUtils.isInWGRegion(main.wg, to))
		{
			//block spread from unclaimed area
			if (!WGRPUtils.isInWGRegion(main.wg, from))
			{
				return false;
			}
			else
			//block spread from not the same regions
			{
				if (!WGRPUtils.isInTheSameRegion(main.wg, from,to))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean allowFireSpreadInRegion(Block from, Block to)
	{
		if (WGRPUtils.isInWGRegion(main.wg, to))
		{
			if (WGRPUtils.isInTheSameRegion(main.wg, from, to))
			{
				return false;
			}
		}
		return true;
	}
	
}
