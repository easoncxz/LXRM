package com.easoncxz.lxrm.backend;

import android.content.Context;

public class FakeDataStore extends DataStore {

	public FakeDataStore(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ContactList getAll() {
		ContactList l = new ContactList();
		l.add(new Contact.Builder("John Doe", "111", "ai@h.fo").id(14).build());
		l.add(new Contact.Builder("John 2 Doe", "222", "ai@h.fo").id(20).build());
		l.add(new Contact.Builder("John 3 Doe", "333", "ai@h.fo").id(43).build());
		l.add(new Contact.Builder("John 4 Doe", "444", "ai@h.fo").id(99).build());
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
		return new Contact.Builder("John 4 Doe", "444", "ai@h.fo").id(99).build();
	}

}
