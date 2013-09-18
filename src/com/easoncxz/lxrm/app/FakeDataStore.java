package com.easoncxz.lxrm.app;

import android.content.Context;

public class FakeDataStore extends DataStore {

	@SuppressWarnings("deprecation")
	@Override
	public ContactList getContactsList(Context context) {
		ContactList l = new ContactList(context);
		l.addContact(new Contact.Builder("John Doe", "111", "ai@h.fo").build());
		l.addContact(new Contact.Builder("John 2 Doe", "222", "ai@h.fo")
				.build());
		l.addContact(new Contact.Builder("John 3 Doe", "333", "ai@h.fo")
				.build());
		l.addContact(new Contact.Builder("John 4 Doe", "444", "ai@h.fo")
				.build());
		return l;
	}

}
