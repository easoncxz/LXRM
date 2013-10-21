package com.easoncxz.lxrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.easoncxz.lxrm.backend.Contact;
import com.easoncxz.lxrm.backend.DataStore;
import com.easoncxz.lxrm.backend.DataStoreFactory;

@SuppressWarnings("deprecation")
public class EditOneContact extends Activity {

	public static final String RESULT_WANTED_TO_VIEW = "I_want_to_view_this_contact";

	private Contact c;
	private DataStore ds;

	/**
	 * @deprecated unfinished.
	 */
	private void updateUIElements(Contact contact) {
		if (contact == null) {
			// do nothing, or
			// throw new RuntimeException("Contact shouldn't be null");
		} else {
			TextView v = (TextView) findViewById(R.id.personName);
			v.setText(contact.getName().formattedName());
			// TODO emails & phones too.
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);
		// Show the Up button in the action bar.
		setupActionBar();
		ds = DataStoreFactory.getDataStore(this);

		Bundle extras = getIntent().getExtras();
		// might be null - when creating new contact

		if (extras != null) {
			// we are editing an existing contact.

			long id = extras.getLong(Contact.KEY_ID, -1);
			// That key should always be in the extras! The -1 should never be
			// needed!

			this.c = ds.get(id);
		} else {
			Log.d("EditOneContact",
					"We know that the extras passed to us are null");
			// we are editing a new contact.
		}

		updateUIElements(this.c);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_save:
			Contact c = (new Contact.Builder(
					((TextView) findViewById(R.id.personName)).getText()
							.toString())).build();
			// TODO create the contact object properly: include multiple Phones,
			// Emails

			long id = ds.put(c);
			// id == c.getId() != -1 ? c.getId() : the_new_id_from_the_ds

			Intent result = new Intent();
			Bundle b = new Bundle();
			b.putLong(Contact.KEY_ID, id);
			result.putExtras(b);
			setResult(RESULT_OK, result);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
