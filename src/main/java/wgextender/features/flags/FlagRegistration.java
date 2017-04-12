package wgextender.features.flags;

import java.util.Map;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.flags.registry.UnknownFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import wgextender.WGExtender;
import wgextender.utils.ReflectionUtils;

public class FlagRegistration {

	@SuppressWarnings("unchecked")
	public static void registerFlag(Flag<?> flag) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		//manually insert flag into the registry
		FlagRegistry registry = WGExtender.getWorldGuard().getFlagRegistry();
		Map<String, Flag<?>> flagMap = ReflectionUtils.getField(registry, "flags");
		Flag<?> prevFlag = flagMap.put(flag.getName().toLowerCase(), flag);
		//change flag instance in every loaded region if had old one
		if (prevFlag != null) {
			for (RegionManager rm : WGExtender.getWorldGuard().getRegionContainer().getLoaded()) {
				for (ProtectedRegion region : rm.getRegions().values()) {
					Map<Flag<?>, Object> flags = region.getFlags();
					Object prevValue = flags.remove(prevFlag);
					if (prevValue != null) {
						//unknown flag will store marshaled value as value directly, so we can try to unmarshal it
						if (prevFlag instanceof UnknownFlag) {
							try {
								Object unmarshalled = flag.unmarshal(prevValue);
								if (unmarshalled != null) {
									flags.put(flag, unmarshalled);
								}
							} catch (Throwable t) {
							}
						}
						//before reload instance probably, try to marshal value first to see if it is compatible
						else {
							try {
								((Flag<Object>) flag).marshal(prevValue);
								flags.put(flag, prevValue);
							} catch (Throwable t) {
							}
						}
						region.setDirty(true);
					}
				}
			}
		}
	}

}
