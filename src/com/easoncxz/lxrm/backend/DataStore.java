package com.easoncxz.lxrm.backend;

/**
 * An abstraction layer for accessing data storage.
 */
public abstract class DataStore {

	/**
	 * @return a list of all contacts stored in this data store.
	 */
	public abstract ContactList getAll();

	/**
	 * Retrieve a Contact object stored in this data store, according to id. It
	 * is the DataStore's responsibility to ensure the uniqueness of (Contact,
	 * id) correlation.
	 * 
	 * @param id
	 * @return <i>the</i> contact in this data store with the given id.
	 */
	@SuppressWarnings("deprecation")
	public abstract Contact get(long id);

	/**
	 * Store the given Contact object into this data store. Where to store
	 * depends on the id field of that Contact object - this means this method
	 * overwrites existing stored contacts.
	 * 
	 * @param contact
	 * @return the id of where the given contact is put.
	 */
	@SuppressWarnings("deprecation")
	public abstract long put(Contact contact);

}
