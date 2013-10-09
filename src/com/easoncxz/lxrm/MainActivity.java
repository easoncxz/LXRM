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

	private ListView l;
	private DataStore ds;
	private ContactList cl;

	/**
	 * Private method that adapts to different ways of filling in the ListView
	 * on this screen. Expect this method to interact with other classes.
	 */
	private void populateListView(ContactListAdapter cla) {
		// first register adapter for the ListView:
		l.setAdapter(cla);

		// then registers event handlers for the ListView:
		l.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(view.getContext(), ViewOneContact.class);
				Bundle extras = new Bundle();
				
				
				extras.putLong("id", id);
				
				i.putExtras(extras);
				MainActivity.this.startActivity(i, extras);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		l = (ListView) findViewById(R.id.list_of_contacts);
		
		ds = DataStoreFactory.getDataStore();
		cl = ds.get();
		ContactListAdapter cla = new ContactListAdapter(this);
		populateListView(cla);
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
			Intent i = new Intent(this, EditOneContact.class);
			startActivity(i);
			return true;
		case R.id.action_search_contact_list:
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
