package wgextender.features.flags;

import com.sk89q.worldguard.protection.flags.StateFlag;

public class ChorusFruitUseFlag extends StateFlag {

	private static ChorusFruitUseFlag instance;

	public static ChorusFruitUseFlag getInstance() {
		return instance;
	}

	public static void assignInstance() {
		instance = new ChorusFruitUseFlag();
	}

	protected ChorusFruitUseFlag() {
		super("chorus-fruit-use", true);
	}

}
