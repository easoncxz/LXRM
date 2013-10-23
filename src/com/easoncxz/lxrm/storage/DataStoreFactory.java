package com.easoncxz.lxrm.storage;

import android.content.Context;

import com.easoncxz.lxrm.models.Contact;

/**
 * Note: This class is perhaps not following the "factory pattern" strictly.
 */
public class DataStoreFactory {

	private static DBDataStore real;

	@SuppressWarnings("deprecation")
	public static DataStore getDataStore(Context context) {
		if (real == null) {
			real = new DBDataStore(context);
		}
		return real;
	}

}
