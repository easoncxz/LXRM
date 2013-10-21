package com.easoncxz.lxrm.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDataStore extends DataStore {

	private class Helper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "DBDeMerde";
		private static final int DATABASE_VERSION = 1;

		private static final String TABLE_CONTACTS = "contacts";
		private static final String COLUMN_ID = "_id";
		private static final String COLUMN_TAG = "useless_column";
		// private static final String COLUMN_PERSON_NAME = "name";

		private static final String SQL_CREATE = "CREATE TABLE "
				+ TABLE_CONTACTS + " (" + COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TAG
				+ " TEXT);";

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

		@Override
		public SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}
	}

	private SQLiteOpenHelper h;

	public DBDataStore(Context context) {
		super(context);
		h = new Helper(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public long put(Contact contact) {
		Log.d("DBDataStore", "this is DBDataStore#put");
		SQLiteDatabase db = h.getWritableDatabase();
		ContentValues cv = new ContentValues();
		long id = contact.getId();

		cv.put(Helper.COLUMN_TAG, contact.getName().formattedName());
		if (id == -1) {
			id = db.insert(Helper.TABLE_CONTACTS, null, cv);
		} else {
			cv.put(Helper.COLUMN_ID, id);
			db.update(Helper.TABLE_CONTACTS, cv, Helper.COLUMN_ID + " == ?",
					new String[] { Long.toString(id) });
		}
		db.close();
		return id;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Contact get(long id) {
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
		SQLiteDatabase db = h.getWritableDatabase();
		Cursor rows = db.query(Helper.TABLE_CONTACTS, new String[] {
				Helper.COLUMN_ID, Helper.COLUMN_TAG }, null, null, null, null,
				null);
		ContactList cl = new ContactList();
		if (rows.moveToFirst()) {
			do {
				long id = rows.getLong(0);
				Contact c = (new Contact.Builder(rows.getString(1)
						+ " BTW the id is: " + Long.toString(id))).id(id)
						.build();
				Log.d("DBDataStore#getAll", "got id: " + Long.toString(id));
				cl.add(c);
			} while (rows.moveToNext());
		}
		db.close();
		return cl;
	}

}
