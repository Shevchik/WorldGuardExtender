package WGExtender.flags;

import com.sk89q.worldguard.protection.flags.StateFlag;

public class BlockInteractRestrictFlag extends StateFlag {

	public static BlockInteractRestrictFlag instance = null;

	public static void injectFlag() {
		instance = new BlockInteractRestrictFlag();
		FlagInjector.injectFlag(instance);
    }

	public BlockInteractRestrictFlag() {
		super("block-interact", true);
	}
	
}
