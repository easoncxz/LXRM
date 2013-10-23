package com.easoncxz.lxrm.storage;

import android.content.Context;

import com.easoncxz.lxrm.exceptions.ContactNotFoundException;
import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.ContactList;

/**
 * An simple interface for accessing storage provided to the logic layer.
 */
public abstract class DataStore {

	Context context;

	public DataStore(Context context) {
		this.context = context;
	}

	/**
	 * @return a list of all contacts stored in this data store.
	 */
	public abstract ContactList getAll();

	/**
	 * Retrieve a Contact object stored in this data store, according to id.
	 * <p>
	 * Exceptions about not finding a contact with the given id (or rather,
	 * finding multiple contacts) is yet to be dealt with.
	 * 
	 * @param id
	 * @return <i>the</i> contact in this data store with the given id.
	 * @throws ContactNotFoundException 
	 */
	@SuppressWarnings("deprecation")
	public abstract Contact get(long id) throws ContactNotFoundException;

	/**
	 * Store the given Contact object into this data store. Where to store
	 * depends on the id field of that Contact object - this means this method
	 * overwrites existing stored contacts. It is the DataStore's responsibility
	 * to ensure the uniqueness of (Contact, id) correlation.
	 * <p>
	 * To store a new contact, pass in a Contact object with an id field of -1.
	 * <p>
	 * Exceptions about the provided contact not having a reasonable id is yet
	 * to be dealt with.
	 * 
	 * @param contact
	 * @return the id of where the given contact is put.
	 */
	@SuppressWarnings("deprecation")
	public abstract long put(Contact contact);

}
