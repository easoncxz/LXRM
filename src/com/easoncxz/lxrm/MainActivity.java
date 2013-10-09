package com.easoncxz.lxrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.easoncxz.lxrm.backend.ContactList;
import com.easoncxz.lxrm.backend.DataStore;
import com.easoncxz.lxrm.backend.DataStoreFactory;

public class MainActivity extends Activity {

	private ListView l;
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
	}

	/**
	 * A listener class which is intended to be used by the main ListView.
	 */
	private class ViewOneContactListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(view.getContext(), ViewOneContact.class);
			Bundle extras = new Bundle();

			// get the id of the clicked item ready for the next/ activity.
			extras.putLong("id", id);

			i.putExtras(extras);
			if (parent instanceof ListView) {
				parent.getContext().startActivity(i, extras);
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
		setContentView(R.layout.activity_main);

		l = (ListView) findViewById(R.id.list_of_contacts);
		ds = DataStoreFactory.getDataStore();
		ContactList cl = ds.get(); // dynamically loads latest data

		// consider giving the VOCL a name?:
		populateListView(l, new ContactListAdapter(this, cl),
				new ViewOneContactListener());
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
