package com.easoncxz.lxrm.app;

/**
 * Note: This class is perhaps not following the "factory pattern" strictly.
 */
public class DataStoreFactory {

	private static FakeDataStore fake;
	
	public static DataStore getDataStore() {
		if (fake == null) {
			fake = new FakeDataStore();
		}
		return fake;
	}
	
}
