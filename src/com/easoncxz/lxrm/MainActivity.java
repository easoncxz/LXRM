package com.easoncxz.lxrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.easoncxz.lxrm.app.ContactList;
import com.easoncxz.lxrm.app.DataStore;
import com.easoncxz.lxrm.app.DataStoreFactory;

public class MainActivity extends Activity {

	private ListView l = null;

	/**
	 * Private method that adapts to different ways of filling in the ListView
	 * on this screen. Expect this method to interact with other classes.
	 */
	private void populateListView() {
		// first register adapter for the ListView:

		// ArrayAdapter<String> aa = new ArrayAdapter<String>(MainActivity.this,
		// android.R.layout.simple_list_item_1, new String[] { "1", "2",
		// "3" });
		// l.setAdapter(aa);

		DataStore ds = DataStoreFactory.getDataStore();
		ContactList cl = ds.getContactsList(this);
		l.setAdapter(cl);

		// then registers event handlers for the ListView:
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						MainActivity.this,
						"id: " + Long.toString(id) + "\npos: "
								+ Integer.toString(position),
						Toast.LENGTH_SHORT).show();
				// TODO
			}
		};
		l.setOnItemClickListener(listener);
	}

	private void viewOneContact(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent();
		i.setClass(MainActivity.this, ViewOneContact.class);
		Bundle extras = new Bundle();
		extras.putLong("id", id);
		i.putExtras(extras);
		startActivity(i, extras);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		l = (ListView) findViewById(R.id.list_of_contacts);
		populateListView();
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
		case R.id.action_add_contact:
			// Toast.makeText(this, "add contact btn clicked",
			// Toast.LENGTH_SHORT).show();
			Intent i = new Intent();
			i.setClass(this, NewContact.class);
			startActivity(i);
			return true;
		case R.id.action_sort_contact_list:
			Toast.makeText(this, "sort list btn clicked", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.action_settings:
			Toast.makeText(this, "settings btn clicked", Toast.LENGTH_SHORT)
					.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
