package com.easoncxz.lxrm.storage;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.ContactList;
import com.easoncxz.lxrm.models.Email;
import com.easoncxz.lxrm.models.Name;
import com.easoncxz.lxrm.models.Phone;

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
		}

	}

	private SQLiteOpenHelper h;

	public DBDataStore(Context context) {
		super(context);
		this.h = new Helper(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public long put(Contact contact) {
		Log.d("DBDataStore", "this is DBDataStore#put");
		SQLiteDatabase db = h.getWritableDatabase();

		ContentValues contactValues = new ContentValues();
		long id = contact.getId();

		Name name = contact.getName();
		List<Phone> phones = contact.getPhones();
		List<Email> emails = contact.getEmails();
		ContentValues phoneValues = new ContentValues();
		ContentValues emailValues = new ContentValues();
		contactValues.put(Helper.COLUMN_PERSON_NAME, name.formattedName());

		if (id == -1) {

			id = db.insert(Helper.TABLE_CONTACTS, null, contactValues);
		} else {
			contactValues.put(Helper.COLUMN_ID, id);
			db.update(Helper.TABLE_CONTACTS, contactValues, Helper.COLUMN_ID + " == ?",
					new String[] { Long.toString(id) });
		}
		db.close();
		return id;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Contact get(long id) {
		Log.d("DBDataStore", "this is DBDataStore#get");
		SQLiteDatabase db = h.getReadableDatabase();
		// ContentValues cv = new ContentValues();
		// cv.put(Helper.COLUMN_ID, id);
		Cursor cursor = db.query(Helper.TABLE_CONTACTS,
				new String[] { Helper.COLUMN_ID }, Helper.COLUMN_ID + "== ?",
				new String[] { Long.toString(id) }, null, null, null, null);
		Contact contact = null; // sometimes will be returned while being null.
		if (cursor.getCount() > 1) {
			throw new RuntimeException(
					"Error!! Found more than 1 contact with the same ID?!");
		} else if (cursor.getCount() < 1) {
			throw new RuntimeException("No contact with that id found");
			// TODO change this to a checked exception.
		} else if (cursor.getCount() == 1) {
			contact = (new Contact.Builder(Long.toString(id))).id(id).build();
		}
		return contact;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ContactList getAll() {
		Log.d("DBDataStore#getAll", "entering method");
		SQLiteDatabase db = h.getReadableDatabase();
		Cursor rows = db.query(Helper.TABLE_CONTACTS, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_PERSON_NAME }, null, null,
				null, null, null);
		ContactList cl = new ContactList();
		if (rows.moveToFirst()) {
			do {
				long id = rows.getLong(0);
				Contact c = (new Contact.Builder(rows.getString(1)
						+ " BTW the id is: " + Long.toString(id))).id(id)
						.build();
				cl.add(c);
			} while (rows.moveToNext());
		}
		db.close();
		return cl;
	}

}
