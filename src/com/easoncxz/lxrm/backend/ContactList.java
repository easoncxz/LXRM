package com.easoncxz.lxrm.backend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public class ContactList {

	public ContactList() {
		// blank. documentation purpose.
	}

	@SuppressWarnings("deprecation")
	private List<Contact> list = new ArrayList<Contact>();

	public int size() {
		return list.size();
	}
	
	public void add(Contact contact) {
		list.add(contact);
	}

	/**
	 * @param position
	 * @return a Contact object
	 */
	@SuppressWarnings("deprecation")
	public Contact get(int position) {
		return list.get(position);
	}

}
