package com.easoncxz.lxrm.models;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * A data abstraction class.
 * <p>
 * "Final" because of un-handled situations with potential problems with
 * inheriting from a class using a builder.
 * 
 * @deprecated unfinished
 */
public final class Contact {

	/**
	 * Important field. Being used everywhere in the application.
	 */
	public static final String KEY_ID = "id";

	/**
	 * Important field. Being used everywhere in the application. Initialized to
	 * -1 in the Builder; -1 indicates a new contact yet to be stored. Stays
	 * unchanged for each Contact object after instantiation.
	 */
	private long id;
	private Name name;
	private List<Phone> phones = new ArrayList<Phone>();
	private List<Email> emails = new ArrayList<Email>();

	/**
	 * "Final" due to same reason as {@link Contact} being "final".
	 */
	public static final class Builder {

		private long id = -1;
		private Name name;

		private List<Phone> phones = new ArrayList<Phone>();
		private List<Email> emails = new ArrayList<Email>();

		public Builder(String inputtedName) {
			this.name = new Name(inputtedName);
		}

		public Builder addPhones(List<Phone> phones) {
			this.phones.addAll(phones);
			return this;
		}

		public Builder addEmails(List<Email> emails) {
			this.emails.addAll(emails);
			return this;
		}

		/**
		 * Allows creator of the Contact object to specify an id (of not the
		 * default -1). Could be used e.g. when creating Contact objects from
		 * the DB.
		 */
		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Contact build() {
			return new Contact(this);
		}

	}

	private Contact(Builder builder) {
		this.id = builder.id;
		this.phones = builder.phones; // could be empty; wouldn't be null
		this.emails = builder.emails; // could be empty; wouldn't be null
		this.name = builder.name;
	}

	// Below: methods provided as sort of an API for other classes.
	// Deliberately giving them the dependency on Name, Phone, Email.

	public void putName(Name name) {
		this.name = name;
	}

	/**
	 * Safely puts Phone objects with whatever id.
	 */
	public void putPhone(Phone phone) {
		List<Phone> oldList = this.phones;
		Log.v("Contact#putPhone", "the Contact used to have: " + oldList.size()
				+ " phones.");
		List<Phone> newList;
		long id = phone.id();
		Log.v("Contact#putPhone", "the new phone is:\n\t(" + phone.id() + ") "
				+ phone.type() + ": " + phone.number());
		if (id == -1) {
			newList = oldList;
			newList.add(phone);
			Log.v("Contact#putPhone",
					"after making an attempt to put this phone, the contact now has: "
							+ newList.size() + " phones, which are:");
			for (Phone p : newList) {
				Log.v("Contact#putPhone", "\t(" + Long.toString(p.id()) + ") "
						+ p.type() + ": " + p.number());
			}
		} else {
			newList = new ArrayList<Phone>();
			for (Phone p : oldList) {
				Log.v("Contact#putPhone",
						"an existing phone with id: " + p.id() + " is found");
				if (p.id() == phone.id()) {
					newList.add(phone);
				} else {
					newList.add(p);
				}
			}
		}
		this.phones = newList;
	}

	/**
	 * Safely puts Email objects with whatever id.
	 */
	public void putEmail(Email email) {
		List<Email> oldList = this.emails;
		List<Email> newList;
		if (email.id() == -1) {
			// adding new email
			newList = oldList;
			newList.add(email);
		} else {
			// modifying existing email
			newList = new ArrayList<Email>();
			for (Email e : oldList) {
				if (e.id() == email.id()) {
					newList.add(email);
				} else {
					newList.add(e);
				}
			}
		}
		this.emails = newList;
	}

	// generated getters below

	public long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public List<Email> getEmails() {
		return emails;
	}

}
