package wgextender.features.regionprotect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import com.sk89q.worldguard.bukkit.ConfigurationManager;
import com.sk89q.worldguard.bukkit.WorldConfiguration;

import wgextender.WGExtender;

public abstract class WGOverrideListener implements Listener {

	private final ArrayList<Tuple<HandlerList, RegisteredListener>> overridenEvents = new ArrayList<>();

	public void inject() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for (Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(EventHandler.class)) {
				Class<?> eventClass = method.getParameterTypes()[0];
				HandlerList hl = (HandlerList) eventClass.getMethod("getHandlerList").invoke(null);
				for (RegisteredListener listener : new ArrayList<RegisteredListener>(Arrays.asList(hl.getRegisteredListeners()))) {
					if (listener.getListener().getClass() == getClassToReplace()) {
						overridenEvents.add(new Tuple<HandlerList, RegisteredListener>(hl, listener));
						hl.unregister(listener);
					}
				}
			}
		}
		Bukkit.getPluginManager().registerEvents(this, WGExtender.getInstance());
	}

	public void uninject() {
		HandlerList.unregisterAll(this);
		for (Tuple<HandlerList, RegisteredListener> tuple : overridenEvents) {
			tuple.getO1().register(tuple.getO2());
		}
		overridenEvents.clear();
	}

	private static class Tuple<T1, T2> {
		private T1 o1;
		private T2 o2;
		public Tuple(T1 t1, T2 t2) {
			this.o1 = t1;
			this.o2 = t2;
		}
		public T1 getO1() {
			return o1;
		}
		public T2 getO2() {
			return o2;
		}
	}

	protected abstract Class<? extends Listener> getClassToReplace();

    protected ConfigurationManager getConfig() {
        return WGExtender.getWorldGuard().getGlobalStateManager();
    }

    protected WorldConfiguration getWorldConfig(World world) {
        return WGExtender.getWorldGuard().getGlobalStateManager().get(world);
    }

    protected WorldConfiguration getWorldConfig(Player player) {
        return getWorldConfig(player.getWorld());
    }

    protected boolean isRegionSupportEnabled(World world) {
    	return getWorldConfig(world).useRegions;
    }

}
