package com.easoncxz.lxrm.backend;

import java.util.ArrayList;
import java.util.List;

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
	 * Half for scaffolding purpose. Could be used as the key for avatar/notes
	 * later.
	 */
	public static final String KEY_TAG = "useless_key";

	/**
	 * This class is made public only so that it can be used as a return type as
	 * public methods of Contact.
	 * <p>
	 * static so that it can be used by the Builder.
	 * 
	 * @deprecated there are bugs in how names are parsed.
	 */
	public static class Name {

		private String firstName;
		private String lastName;
		private String middleNames = "";

		/**
		 * This public constructor is provided for Activities to update contact
		 * details.
		 */
		public Name(String inputtedName) {
			String[] parts = inputtedName.split("\\s+");
			assert parts.length >= 0;
			if (parts.length == 0) {
				firstName = "";
				lastName = "";
			} else if (parts.length == 1) {
				firstName = parts[0];
				lastName = "";
			} else if (parts.length == 2) {
				firstName = parts[0];
				lastName = parts[1];
			} else {
				assert parts.length > 2;
				firstName = parts[0];
				lastName = parts[parts.length - 1];
				StringBuilder b = new StringBuilder();
				for (int i = 1; i < parts.length - 1; i++) {
					b.append(parts[i] + " ");
				}
				middleNames = b.toString();
			}
		}

		public String firstName() {
			return firstName;
		}

		public String lastName() {
			return lastName;
		}

		public String middleNames() {
			return middleNames;
		}

		public String formattedName() {
			return firstName + " " + middleNames + lastName;
		}

	}

	/**
	 * This class is made public only so that it can be used as a return type as
	 * public methods of Contact.
	 * <p>
	 * static so that it can be used by the Builder.
	 */
	public static class Phone {

		private long id = -1;
		private String type;
		private String number;

		/**
		 * This public constructor is provided for Activities to update contact
		 * details.
		 */
		public Phone(long id, String type, String number) {
			this.id = id;
			this.type = type;
			this.number = number;
		}

		public long id() {
			return id;
		}

		public String type() {
			return type;
		}

		public String number() {
			return number;
		}

	}

	/**
	 * This class is made public only so that it can be used as a return type as
	 * public methods of Contact.
	 * <p>
	 * static so that it can be used by the Builder.
	 */
	public static class Email {

		private long id = -1;
		private String type;
		private String address;

		/**
		 * This public constructor is provided for Activities to update contact
		 * details.
		 */
		public Email(long id, String type, String address) {
			this.id = id;
			this.type = type;
			this.address = address;
		}

		public long id() {
			return id;
		}

		public String type() {
			return type;
		}

		public String number() {
			return address;
		}

	}

	/**
	 * @deprecated not implemented.
	 */
	public static class Avatar {
		// TODO
	}

	/**
	 * Important field. Being used everywhere in the application. Initialized to
	 * -1 in the Builder; -1 indicates a new contact yet to be stored.
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

		// private List<Phone> phones = new ArrayList<Phone>();
		// private List<Email> emails = new ArrayList<Email>();

		public Builder(String inputtedName) {
			this.name = new Name(inputtedName);
		}

		// public Builder addPhones(List<Phone> phones) {
		// this.phones.addAll(phones);
		// return this;
		// }
		//
		// public Builder addEmails(List<Email> emails) {
		// this.emails.addAll(emails);
		// return this;
		// }

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
		// this.phones = builder.phones; // could be empty; wouldn't be null
		// this.emails = builder.emails; // could be empty; wouldn't be null
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
		assert phone.id() < this.phones.size() : "The ID of your Phone object is larger than it should be.";
		List<Phone> oldList = this.phones;
		List<Phone> newList;
		if (phone.id() == -1) {
			newList = oldList;
			newList.add(phone);
		} else {
			newList = new ArrayList<Phone>();
			for (Phone p : oldList) {
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
		assert email.id() < this.emails.size() : "The ID of your Email object is larger than it should be.";
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
