package com.easoncxz.lxrm.storage;

import android.content.Context;

import com.easoncxz.lxrm.exceptions.ContactNotFoundException;
import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.ContactList;

public class FakeDataStore extends DataStore {

	public FakeDataStore(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ContactList getAll() {
		ContactList l = new ContactList();
		l.add(new Contact.Builder("John Doe").id(14).build());
		l.add(new Contact.Builder("John 2 Doe").id(20).build());
		l.add(new Contact.Builder("John 3 Doe").id(43).build());
		l.add(new Contact.Builder("John 4 Doe").id(99).build());
		return l;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long put(Contact contact) {
		// Nah, doesn't do anything. This is a fake class.
		return -1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Contact get(long id) {
		// TODO Auto-generated method stub
		return new Contact.Builder("John 4 Doe").id(99).build();
	}

	@Override
	public boolean delete(long id) throws ContactNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
