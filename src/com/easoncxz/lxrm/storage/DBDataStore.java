package com.easoncxz.lxrm.storage;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.easoncxz.lxrm.exceptions.ContactNotFoundException;
import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.ContactList;
import com.easoncxz.lxrm.models.Email;
import com.easoncxz.lxrm.models.Name;
import com.easoncxz.lxrm.models.Phone;

@SuppressWarnings("deprecation")
public class DBDataStore extends DataStore {

	private class Helper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "DBDeMerde";
		private static final int DATABASE_VERSION = 1;

		// some column names are shared between different tables.
		private static final String TABLE_CONTACTS = "contacts";
		private static final String TABLE_PHONES = "phones";
		private static final String TABLE_EMAILS = "emails";
		private static final String COLUMN_ID = "_id";
		private static final String COLUMN_OWNER_ID = "owner_id";
		private static final String COLUMN_PERSON_NAME = "name";
		private static final String COLUMN_TYPE = "type";
		private static final String COLUMN_PHONE_NUMBER = "number";
		private static final String COLUMN_EMAIL_ADDRESS = "address";

		private static final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE "
				+ TABLE_CONTACTS + " (" + COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PERSON_NAME
				+ " TEXT NOT NULL); ";
		private static final String SQL_CREATE_PHONES_TABLE = "CREATE TABLE "
				+ TABLE_PHONES + " (" + COLUMN_ID
				+ "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_OWNER_ID
				+ " INTEGER NOT NULL, " + COLUMN_TYPE + " TEXT NOT NULL, "
				+ COLUMN_PHONE_NUMBER + " TEXT NOT NULL); ";
		private static final String SQL_CREATE_EMAILS_TABLE = "CREATE TABLE "
				+ TABLE_EMAILS + " (" + COLUMN_ID
				+ "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_OWNER_ID
				+ " INTEGER NOT NULL, " + COLUMN_TYPE + " TEXT NOT NULL, "
				+ COLUMN_EMAIL_ADDRESS + " TEXT NOT NULL); ";
		private static final String SQL_CREATE = SQL_CREATE_CONTACTS_TABLE
				+ SQL_CREATE_PHONES_TABLE + SQL_CREATE_EMAILS_TABLE;

		public Helper(Context context) {
			// see:
			// http://developer.android.com/guide/topics/data/data-storage.html#db
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// don't know what to do here.
			// db.execSQL("DROP TABLE IF EXISTS " + Helper.TABLE_CONTACTS +
			// ";");
			// db.execSQL("DROP TABLE IF EXISTS " + Helper.TABLE_PHONES + ";");
			// db.execSQL("DROP TABLE IF EXISTS " + Helper.TABLE_EMAILS + ";");
			// this.onCreate(db);
		}

	}

	private SQLiteOpenHelper h;

	public DBDataStore(Context context) {
		super(context);
		this.h = new Helper(context);
	}

	/**
	 * @see DataStore#put(Contact)
	 * @param contact
	 * @return
	 */
	@Override
	public long put(Contact contact) {
		Log.d("DBDataStore", "this is DBDataStore#put");
		SQLiteDatabase db = h.getWritableDatabase();
		long contactId = contact.getId();
		if (contactId == -1) {
			contactId = this.createNewContact(contact, db);
		} else {
			this.updateExistingContact(contact, db);
		}
		db.close();
		return contactId;
	}

	/**
	 * This method is used by {@link DBDataStore#put(Contact)}
	 * 
	 * @param contact
	 * @param db
	 */
	private long createNewContact(Contact contact, SQLiteDatabase db) {
		// We are creating a new contact.
		// Into all tables, we should be inserting.
		long contactId = contact.getId();
		if (contactId != -1) {
			throw new RuntimeException(
					"Attempting to create new contact, but the contact received by the DB doesn't seem new.");
		}

		Name name = contact.getName();
		{
			ContentValues cv = new ContentValues();
			cv.put(Helper.COLUMN_PERSON_NAME, name.formattedName());
			// Storing `Name.formattedName` to be parsed as inputtedName
			// later.
			contactId = db.insert(Helper.TABLE_CONTACTS, null, cv);
			// Notice that id is being defined here.
			// This block must be before the phone & emails blocks.
		}

		List<Phone> phones = contact.getPhones();
		for (Phone p : phones) {
			ContentValues cv = new ContentValues();
			cv.put(Helper.COLUMN_OWNER_ID, contactId);
			cv.put(Helper.COLUMN_TYPE, p.type());
			cv.put(Helper.COLUMN_PHONE_NUMBER, p.number());
			db.insert(Helper.TABLE_PHONES, null, cv);
		}

		List<Email> emails = contact.getEmails();
		for (Email e : emails) {
			ContentValues cv = new ContentValues();
			cv.put(Helper.COLUMN_OWNER_ID, contactId);
			cv.put(Helper.COLUMN_TYPE, e.type());
			cv.put(Helper.COLUMN_EMAIL_ADDRESS, e.address());
			db.insert(Helper.TABLE_EMAILS, null, cv);
		}

		return contactId;
	}

	/**
	 * This method is used by {@link DBDataStore#put(Contact)}.
	 * 
	 * @param contact
	 * @param db
	 */
	private void updateExistingContact(Contact contact, SQLiteDatabase db) {
		// We are editing an existing contact.
		// However, it is still possible that new Phone/Email entries are
		// created.
		long contactId = contact.getId();
		{
			Name name = contact.getName();
			ContentValues cv = new ContentValues();
			cv.put(Helper.COLUMN_PERSON_NAME, name.formattedName());
			// Storing `Name.formattedName` to be parsed as inputtedName
			// later.
			db.update(Helper.TABLE_CONTACTS, cv, Helper.COLUMN_ID + " == ?",
					new String[] { Long.toString(contactId) });
		}

		this.diffPhones(contact, db);

		this.diffEmails(contact, db);

	}

	/**
	 * This method is used by
	 * {@link DBDataStore#updateExistingContact(Contact, SQLiteDatabase)}
	 * 
	 * @param contact
	 * @param db
	 * @param contactId
	 */
	private void diffPhones(Contact contact, SQLiteDatabase db) {
		long contactId = contact.getId();
		List<Phone> oldPhones = this.getOldPhoneList(contactId, db);
		List<Phone> newPhones = contact.getPhones();

		// create new (if any)
		for (Phone newPhone : newPhones) {
			if (newPhone.id() == -1) {
				this.createNewPhone(newPhone, contactId, db);
			}
		}

		// update and delete (if any)
		for (Phone oldPhone : oldPhones) {
			boolean thisPhoneShouldBeDeleted = true;
			for (Phone newPhone : newPhones) {
				if (newPhone.id() == oldPhone.id()) {
					this.updateExistingPhone(newPhone, contactId,
							oldPhone.id(), db);
					thisPhoneShouldBeDeleted = false;
					break;
				}
			}
			if (thisPhoneShouldBeDeleted) {
				this.deleteExistingPhone(oldPhone.id(), db);
			}
		}

	}

	private void diffEmails(Contact contact, SQLiteDatabase db) {
		long contactId = contact.getId();
		List<Email> oldEmails = this.getOldEmailList(contactId, db);
		List<Email> newEmails = contact.getEmails();

		// create new (if any)
		for (Email newEmail : newEmails) {
			if (newEmail.id() == -1) {
				this.createNewEmail(newEmail, contactId, db);
			}
		}

		// update and delete (if any)
		for (Email oldEmail : oldEmails) {
			boolean thisEmailShouldBeDeleted = true;
			for (Email newEmail : newEmails) {
				if (newEmail.id() == oldEmail.id()) {
					this.updateExistingEmail(newEmail, contactId,
							oldEmail.id(), db);
					thisEmailShouldBeDeleted = false;
					break;
				}
			}
			if (thisEmailShouldBeDeleted) {
				this.deleteExistingEmail(oldEmail.id(), db);
			}
		}
	}

	/**
	 * This method is used by
	 * {@link DBDataStore#diffPhones(Contact, SQLiteDatabase, long)}
	 * 
	 * @param db
	 * @param contactId
	 * @return
	 */
	private List<Phone> getOldPhoneList(long contactId, SQLiteDatabase db) {
		List<Phone> oldPhones = new ArrayList<Phone>();
		Cursor c = db.query(Helper.TABLE_PHONES, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_OWNER_ID, Helper.COLUMN_TYPE,
				Helper.COLUMN_PHONE_NUMBER }, Helper.COLUMN_OWNER_ID + " == ?",
				new String[] { Long.toString(contactId) }, null, null, null,
				null);
		if (c.moveToFirst()) {
			do {
				long phoneId = c.getLong(c
						.getColumnIndex(Helper.COLUMN_OWNER_ID));
				String type = c.getString(c.getColumnIndex(Helper.COLUMN_TYPE));
				String number = c.getString(c
						.getColumnIndex(Helper.COLUMN_PHONE_NUMBER));
				oldPhones.add(new Phone(phoneId, type, number));
			} while (c.moveToNext());
		}
		return oldPhones;
	}

	private List<Email> getOldEmailList(long contactId, SQLiteDatabase db) {
		List<Email> oldEmails = new ArrayList<Email>();
		Cursor c = db.query(Helper.TABLE_EMAILS, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_OWNER_ID, Helper.COLUMN_TYPE,
				Helper.COLUMN_EMAIL_ADDRESS },
				Helper.COLUMN_OWNER_ID + " == ?",
				new String[] { Long.toString(contactId) }, null, null, null,
				null);
		if (c.moveToFirst()) {
			do {
				long emailId = c.getLong(c
						.getColumnIndex(Helper.COLUMN_OWNER_ID));
				String type = c.getString(c.getColumnIndex(Helper.COLUMN_TYPE));
				String address = c.getString(c
						.getColumnIndex(Helper.COLUMN_EMAIL_ADDRESS));
				oldEmails.add(new Email(emailId, type, address));
			} while (c.moveToNext());
		}
		return oldEmails;
	}

	/**
	 * This method is used by
	 * {@link DBDataStore#diffPhones(Contact, SQLiteDatabase, long)}
	 * 
	 * @param newPhone
	 */
	private long createNewPhone(Phone newPhone, long contactId,
			SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(Helper.COLUMN_ID, newPhone.id());
		cv.put(Helper.COLUMN_OWNER_ID, contactId);
		cv.put(Helper.COLUMN_TYPE, newPhone.type());
		cv.put(Helper.COLUMN_PHONE_NUMBER, newPhone.number());
		return db.insert(Helper.TABLE_PHONES, null, cv);
	}

	private long createNewEmail(Email newEmail, long contactId,
			SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(Helper.COLUMN_ID, newEmail.id());
		cv.put(Helper.COLUMN_OWNER_ID, contactId);
		cv.put(Helper.COLUMN_TYPE, newEmail.type());
		cv.put(Helper.COLUMN_EMAIL_ADDRESS, newEmail.address());
		return db.insert(Helper.TABLE_EMAILS, null, cv);
	}

	/**
	 * This method is used by
	 * {@link DBDataStore#diffPhones(Contact, SQLiteDatabase, long)}
	 * 
	 * @param newPhone
	 * @param oldPhone
	 * @return
	 */
	private int updateExistingPhone(Phone newPhone, long contactId,
			long oldPhoneId, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(Helper.COLUMN_ID, oldPhoneId);
		cv.put(Helper.COLUMN_OWNER_ID, contactId);
		cv.put(Helper.COLUMN_TYPE, newPhone.type());
		cv.put(Helper.COLUMN_PHONE_NUMBER, newPhone.number());
		return db.update(Helper.TABLE_PHONES, cv, Helper.COLUMN_ID + " == ?",
				new String[] { Long.toString(oldPhoneId) });
	}

	private long updateExistingEmail(Email newEmail, long contactId,
			long oldEmailId, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(Helper.COLUMN_ID, oldEmailId);
		cv.put(Helper.COLUMN_OWNER_ID, contactId);
		cv.put(Helper.COLUMN_TYPE, newEmail.type());
		cv.put(Helper.COLUMN_EMAIL_ADDRESS, newEmail.address());
		return db.update(Helper.TABLE_EMAILS, cv, Helper.COLUMN_ID + " == ?",
				new String[] { Long.toString(oldEmailId) });
	}

	/**
	 * This method is used by
	 * {@link DBDataStore#diffPhones(Contact, SQLiteDatabase, long)}
	 * 
	 * @param oldPhone
	 * @return
	 */
	private int deleteExistingPhone(long oldId, SQLiteDatabase db) {
		return db.delete(Helper.TABLE_PHONES, Helper.COLUMN_ID + " == ?",
				new String[] { Long.toString(oldId) });
	}

	private int deleteExistingEmail(long oldId, SQLiteDatabase db) {
		return db.delete(Helper.TABLE_EMAILS, Helper.COLUMN_ID + " == ?",
				new String[] { Long.toString(oldId) });
	}

	@Override
	public Contact get(long id) throws ContactNotFoundException {
		Log.d("DBDataStore", "this is DBDataStore#get");
		SQLiteDatabase db = h.getReadableDatabase();
		Cursor cursor = db.query(Helper.TABLE_CONTACTS, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_PERSON_NAME }, Helper.COLUMN_ID
				+ "== ?", new String[] { Long.toString(id) }, null, null, null,
				null);
		Contact contact;
		if (cursor.getCount() > 1) {
			throw new RuntimeException(
					"Error!! Found more than 1 contact with the same ID?!");
		} else if (cursor.getCount() < 1) {
			throw new ContactNotFoundException("No contact with that id found");
		} else {
			// We know that: cursor.getCount() == 1
			contact = this.buildContact(cursor.getString(cursor
					.getColumnIndex(Helper.COLUMN_PERSON_NAME)), id, db);
		}
		return contact;
	}

	private Contact buildContact(String name, long id, SQLiteDatabase db) {
		Contact contact = (new Contact.Builder(name)).id(id).build();
		for (Phone p : this.getOldPhoneList(contact.getId(), db)) {
			contact.putPhone(p);
		}
		for (Email e : this.getOldEmailList(contact.getId(), db)) {
			contact.putEmail(e);
		}
		return contact;
	}

	@Override
	public ContactList getAll() {
		Log.d("DBDataStore#getAll", "entering method");
		SQLiteDatabase db = h.getReadableDatabase();
		Cursor cursor = db.query(Helper.TABLE_CONTACTS, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_PERSON_NAME }, null, null,
				null, null, null);
		ContactList cl = new ContactList();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor
						.getColumnIndex(Helper.COLUMN_PERSON_NAME));
				long id = cursor.getLong(cursor
						.getColumnIndex(Helper.COLUMN_ID));
				Contact c = this.buildContact(name, id, db);
				cl.add(c);
			} while (cursor.moveToNext());
		}
		db.close();
		return cl;
	}

	@Override
	public boolean delete(long id) throws ContactNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}
}
