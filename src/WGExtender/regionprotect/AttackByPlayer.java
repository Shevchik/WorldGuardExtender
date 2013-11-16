package WGExtender.regionprotect;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import WGExtender.Config;
import WGExtender.Main;

public class AttackByPlayer implements Listener {
	
	private Main main;
	private Config config;
	public AttackByPlayer(Main main, Config config) {
		this.main = main;
		this.config = config;
	}

	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=true)
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		if (!config.blockentitydamagebyplayer) {return;}
		
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
				if (!WGRPUtils.isOwnerOrMember(main.wg, damagerplayer, entity.getLocation()))
				{
					e.setCancelled(true);
				}
			}
		}
	}
	
	

}
