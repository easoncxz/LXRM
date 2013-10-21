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
public class ViewOneContact extends Activity {

	public static final String RESULT_WANTED_TO_EDIT = "I want to edit this c";

	private Contact c;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @deprecated
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_one_contact);

		setupActionBar();

		Bundle extras = getIntent().getExtras();
		long id = extras.getLong(Contact.KEY_ID);
		if (id != -1) {
			DataStore ds = DataStoreFactory.getDataStore(this);
			this.c = ds.get(id);
		} else {
			throw new RuntimeException("Cannot view an unsaved contact");
		}

		TextView v = (TextView) findViewById(R.id.personName);
		v.setText(this.c.getName().formattedName());
		// TODO fill in emails & phones
	}

	/**
	 * A generated method: Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_one_contact, menu);
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
		case R.id.action_edit:
			// destroys current Activity and tells MainActivity to start the
			// EditOneContact Activity.

			Intent result = new Intent();
			Bundle extras = new Bundle();
			extras.putLong(Contact.KEY_ID, this.c.getId());
			result.putExtras(extras);

			// startActivity(i, extras);
			Log.d("ViewOneContact_CXZ",
					"Trying to call setResult(int, Intent).");
			setResult(RESULT_OK, result);
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
