package wgextender.features.flags;

import com.sk89q.worldguard.protection.flags.BooleanFlag;

public class OldPVPAttackSpeed extends BooleanFlag {

	private static OldPVPAttackSpeed instance;

	public static OldPVPAttackSpeed getInstance() {
		return instance;
	}

	public static void assignInstance() {
		instance = new OldPVPAttackSpeed();
	}

	public OldPVPAttackSpeed() {
		super("oldpvp-attackspeed");
	}

}
