package com.easoncxz.lxrm.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

@SuppressWarnings("deprecation")
public class ContactList {

	public ContactList() {
		// blank
	}

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

	public void sort() {
		Collections.sort(this.list, contactFirstPhoneComparator);
	}

	private static Comparator<Contact> contactFirstPhoneComparator = new Comparator<Contact>() {
		@Override
		public int compare(Contact lhs, Contact rhs) {
			List<Phone> lpl = lhs.getPhones();
			List<Phone> rpl = rhs.getPhones();
			if (lpl.size() > rpl.size()) {
				return -1;
			} else if (lpl.size() < rpl.size()) {
				return 1;
			} else if (lpl.size() == 0 & rpl.size() == 0) {
				return 0;
			} else {
				Phone lp = lpl.get(0);
				Phone rp = rpl.get(0);
				// long l = Long.valueOf(lp.number());
				// long r = Long.valueOf(rp.number());
				String l = lp.number();
				String r = rp.number();
				return l.compareTo(r);
			}
		}
	};

}
