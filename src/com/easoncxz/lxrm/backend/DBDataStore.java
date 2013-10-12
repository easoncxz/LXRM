package com.easoncxz.lxrm.backend;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDataStore extends DataStore {

	private class Helper extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "DBDeMerde";
		private static final int DATABASE_VERSION = 1;

		private static final String TABLE_NAME_CONTACTS = "contacts";
		private static final String SQL_CREATE = "";

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

	@Override
	public ContactList getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long put(Contact contact) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public Contact get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
