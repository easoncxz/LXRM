package com.easoncxz.lxrm.backend;

/**
 * An abstraction layer for accessing data storage. Could be considered as an
 * ADT.
 */
public abstract class DataStore {

	/**
	 * @return a list of all contacts stored in this data store.
	 */
	public abstract ContactList get();

	/**
	 * Store the given Contact object into this data store. Where to store
	 * depends on the id field of that Contact object.
	 * 
	 * @param contact
	 * @return true only if transaction succeeded.
	 */
	public abstract boolean put(Contact contact);

}
