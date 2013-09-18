package com.easoncxz.lxrm.app;

import android.content.Context;

/**
 * An abstraction layer for accessing data storage. Could be considered as an
 * ADT.
 */
public abstract class DataStore {

	public abstract ContactList getContactsList(Context context);
	
}
