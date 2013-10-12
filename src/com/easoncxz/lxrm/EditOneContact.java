package com.easoncxz.lxrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.easoncxz.lxrm.backend.Contact;

@SuppressWarnings("deprecation")
public class EditOneContact extends Activity {

	public static final String RESULT_WANTED_TO_VIEW = "I_want_to_view_this_contact";

	private long id = -1;

	/**
	 * @deprecated
	 */
	private void fillFields(Bundle extras) {
		if (extras == null) {
			Toast.makeText(this, "null bundle of extras", Toast.LENGTH_SHORT)
					.show();
		} else {
			TextView v = (TextView) findViewById(R.id.personName);
			v.setText(Long.toString(extras.getLong("id")));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);
		// Show the Up button in the action bar.
		setupActionBar();
		Bundle extras = getIntent().getExtras();
		this.fillFields(extras);

		id = extras.getLong(Contact.KEY_ID);
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
			Intent result = new Intent();
			Bundle b = new Bundle();
			b.putLong(Contact.KEY_ID, id);
			result.putExtras(b);
			setResult(RESULT_OK, result);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
