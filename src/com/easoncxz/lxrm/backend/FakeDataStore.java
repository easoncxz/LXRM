package com.easoncxz.lxrm.backend;

public class FakeDataStore extends DataStore {

	@SuppressWarnings("deprecation")
	@Override
	public ContactList get() {
		ContactList l = new ContactList();
		l.add(new Contact.Builder("John Doe", "111", "ai@h.fo").build());
		l.add(new Contact.Builder("John 2 Doe", "222", "ai@h.fo").build());
		l.add(new Contact.Builder("John 3 Doe", "333", "ai@h.fo").build());
		l.add(new Contact.Builder("John 4 Doe", "444", "ai@h.fo").build());
		return l;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean put(Contact contact) {
		// Nah, doesn't do anything. This is a fake class.
		return false;
	}

}
