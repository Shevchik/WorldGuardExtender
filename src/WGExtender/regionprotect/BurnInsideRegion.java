package WGExtender.regionprotect;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import WGExtender.Config;
import WGExtender.Main;

public class BurnInsideRegion implements Listener {

	private Main main;
	private Config config;

	
	public BurnInsideRegion(Main main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onBlockBurn(BlockBurnEvent e)
	{
		if (!config.blockblockburninregion) {return;}
		
		if (WGRPUtils.isInWGRegion(main.wg, e.getBlock()))
		{
			e.setCancelled(true);
		}
	}
	
}
