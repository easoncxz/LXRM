package com.easoncxz.lxrm.backend;

/**
 * Note: This class is perhaps not following the "factory pattern" strictly.
 */
public class DataStoreFactory {

	private static FakeDataStore fake;
	private static DBDataStore real;
	
	public static DataStore getDataStore() {
		if (fake == null) {
			fake = new FakeDataStore();
		}
		if (real == null) {
			real = new DBDataStore();
		}
		return fake;
	}
	
}
