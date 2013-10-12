package com.easoncxz.lxrm.backend;

import android.content.Context;

/**
 * Note: This class is perhaps not following the "factory pattern" strictly.
 */
public class DataStoreFactory {

	private static FakeDataStore fake;
	private static DBDataStore real;
	
	public static DataStore getDataStore(Context context) {
		if (fake == null) {
			fake = new FakeDataStore(context);
		}
		if (real == null) {
			real = new DBDataStore(context);
		}
		return fake;
	}
	
}
