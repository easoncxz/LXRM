package com.easoncxz.lxrm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.ContactList;
import com.easoncxz.lxrm.storage.DataStore;
import com.easoncxz.lxrm.storage.DataStoreFactory;

/**
 * This is the main class and Activity of the entire app.
 * <p>
 * This Activity is the app home screen. This class interacts with UI elements.
 * 
 * @author eason
 * 
 */
public class MainActivity extends Activity {

	private static final int REQUEST_EDIT = 124857; // random number
	private static final int REQUEST_VIEW = 124858; // random number

	private ListView l;
	private ContactList cl;
	private ContactListAdapter la;
	private DataStore ds;

	/**
	 * A general purpose ListView populater.
	 */
	private void populateListView(ListView v, BaseAdapter a,
			OnItemClickListener l) {
		// first register adapter for the ListView:
		v.setAdapter(a);
		// then registers event handlers for the ListView:
		v.setOnItemClickListener(l);
		LayoutInflater i = getLayoutInflater();
		View empty = i.inflate(R.layout.empty, null);
		v.setEmptyView(empty);
	}

	/**
	 * A listener class which is intended to be used by the main ListView.
	 * Accesses MainActivity's methods - coupling warning.
	 */
	private class ViewOneContactListener implements OnItemClickListener {
		@SuppressWarnings("deprecation")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(view.getContext(), ViewOneContact.class);
			Bundle extras = new Bundle();

			// get the id of the clicked item ready for the next/ activity.
			extras.putLong(Contact.KEY_ID, id);

			i.putExtras(extras);
			if (parent instanceof ListView) {
				MainActivity.this.startActivityForResult(i, REQUEST_VIEW);
			} else {
				Toast.makeText(getApplicationContext(),
						"What did you click on? I bet it's not a ListView.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("MainActivity", "onCreate()");
		setContentView(R.layout.activity_main);

		l = (ListView) findViewById(R.id.list_of_contacts);

		// WARNING: this statement could take a long time!
		ds = DataStoreFactory.getDataStore(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v("MainActivity", "onStart()");
		cl = ds.getAll(); // dynamically loads latest data
		la = new ContactListAdapter(this, cl);

		// consider giving the ViewOneContactListener a name?:
		populateListView(l, la, new ViewOneContactListener());

		// problem: this method is called every time user switches between
		// EditOneContact and ViewOneContact, which causes lag.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_contact: {
			Intent i = new Intent(this, EditOneContact.class);
			// the "extras" for Intent i is a null!
			Log.v("MainActivity",
					"#onOptionsItemSelected: creating new contact");
			startActivityForResult(i, REQUEST_EDIT);
			return true;
		}
		case R.id.action_sort:
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle(R.string.sort_this_list);
			b.setMessage(R.string.sort_this_list_how);
			b.setIcon(R.drawable.ic_action_sort_by_size_dark);
			b.setCancelable(true);
			b.setPositiveButton(android.R.string.yes,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.cl.sort();
							MainActivity.this.la.notifyDataSetChanged();
							Toast.makeText(MainActivity.this,
									R.string.list_sorted, Toast.LENGTH_SHORT)
									.show();
						}
					});
			b.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			b.create().show();
			return true;
		case R.id.action_settings:
			Toast.makeText(this, "settings btn clicked", Toast.LENGTH_SHORT)
					.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_VIEW) {
			if (resultCode == RESULT_OK) {
				long id = data.getLongExtra(Contact.KEY_ID, -1);
				Log.v("MainActivity",
						"id of contact to edit is: " + Long.toString(id));
				Intent i = new Intent(this, EditOneContact.class);
				i.putExtra(Contact.KEY_ID, id);
				Log.v("MainActivity", "Created an intent");

				startActivityForResult(i, REQUEST_EDIT);
			}
		} else if (requestCode == REQUEST_EDIT) {
			if (resultCode == RESULT_OK) {
				Intent i = new Intent(this, ViewOneContact.class);
				long id = data.getLongExtra(Contact.KEY_ID, -1);
				i.putExtra(Contact.KEY_ID, id);
				startActivityForResult(i, REQUEST_VIEW);
			}
		}

	}

}
