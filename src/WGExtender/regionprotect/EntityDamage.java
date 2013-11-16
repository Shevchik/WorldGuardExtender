package WGExtender.regionprotect;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import WGExtender.Main;

public class EntityDamage implements Listener {
	
	private Main main;
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		Entity entity = e.getEntity();
		if (entity instanceof Animals && WGRPUtils.isInWGRegion(main.wg, entity.getLocation()))
		{
			Player damagerplayer = null;
			Entity edamager = e.getDamager();
			if (edamager instanceof Player) 
			{
				damagerplayer = (Player) edamager;
			} else 
			if (edamager instanceof Arrow)
			{
				Arrow arrow = (Arrow) edamager;
				if (arrow.getShooter() instanceof Player)
				{
					damagerplayer = (Player) arrow.getShooter();
				}
			}
			if (damagerplayer != null)
			{
				
			}
		}
	}
	
	

}
