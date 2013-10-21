package com.easoncxz.lxrm.storage;

import android.content.Context;

import com.easoncxz.lxrm.models.Contact;

/**
 * Note: This class is perhaps not following the "factory pattern" strictly.
 */
public class DataStoreFactory {

	private static FakeDataStore fake;
	private static DBDataStore real;

	@SuppressWarnings("deprecation")
	public static DataStore getDataStore(Context context) {
		if (fake == null) {
			fake = new FakeDataStore(context);
		}
		if (real == null) {
			real = new DBDataStore(context);
			real.put((new Contact.Builder("John Doe").id(-1)).build());
		}
		return real;
	}

}
