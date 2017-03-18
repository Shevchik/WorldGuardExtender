package wgextender.features.flags;

import com.sk89q.worldguard.protection.flags.BooleanFlag;

public class OldPVPNoBowFlag extends BooleanFlag {

	private static OldPVPNoBowFlag instance;

	public static OldPVPNoBowFlag getInstance() {
		return instance;
	}

	public static void assignInstance() {
		instance = new OldPVPNoBowFlag();
	}

	public OldPVPNoBowFlag() {
		super("oldpvp-nobow");
	}

}
