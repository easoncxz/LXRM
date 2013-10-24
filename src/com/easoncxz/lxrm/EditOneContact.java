package com.easoncxz.lxrm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easoncxz.lxrm.exceptions.ContactNotFoundException;
import com.easoncxz.lxrm.models.Contact;
import com.easoncxz.lxrm.models.Email;
import com.easoncxz.lxrm.models.Name;
import com.easoncxz.lxrm.models.Phone;
import com.easoncxz.lxrm.storage.DataStore;
import com.easoncxz.lxrm.storage.DataStoreFactory;

@SuppressWarnings("deprecation")
public class EditOneContact extends Activity {

	public static final String RESULT_WANTED_TO_VIEW = "I_want_to_view_this_contact";

	private DataStore ds;
	private Contact c;

	// UI elements
	private LayoutInflater inflater;
	private TextView nameField;
	private LinearLayout phoneFieldsLayout;
	private LinearLayout emailFieldsLayout;
	private Button addPhoneButton;
	private Button addEmailButton;
	private List<LinearLayout> phoneFields = new ArrayList<LinearLayout>();
	private List<LinearLayout> emailFields = new ArrayList<LinearLayout>();

	// data model objects
	private Name name;
	private List<Phone> phones;
	private List<Email> emails;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("EditOneContact#onCreate", "entered onCreate");
		setContentView(R.layout.activity_edit_one_contact);
		// Show the Up button in the action bar.
		setupActionBar();

		ds = DataStoreFactory.getDataStore(this);
		Log.d("EditOneContact#onCreate", "got DataStore");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// we are editing an existing contact.
			long id = extras.getLong(Contact.KEY_ID, -1);
			// That key should always be in the extras! The -1 should never be
			// needed!
			try {
				this.c = ds.get(id);
			} catch (ContactNotFoundException e) {
				// this should never happen
				this.c = (new Contact.Builder("Contact not found")).build();
			}
		} else {
			// we are editing a new contact.
			Log.d("EditOneContact",
					"We know that the extras passed to us are null");
			this.c = null;
		}

		inflater = getLayoutInflater();
		nameField = (TextView) findViewById(R.id.person_name_field);
		phoneFieldsLayout = (LinearLayout) findViewById(R.id.phone_fields_layout);
		emailFieldsLayout = (LinearLayout) findViewById(R.id.email_fields_layout);
		addPhoneButton = (Button) findViewById(R.id.add_phone_button);
		addEmailButton = (Button) findViewById(R.id.add_email_button);

		if (this.c == null) {
			// We are creating new contact.
			// No need to fill in any text boxes.
			Toast.makeText(this, "Creating new contact", Toast.LENGTH_SHORT)
					.show();
		} else {
			// We are editing an existing contact.
			// The old information about the contact needs to be displayed.
			name = this.c.getName();
			phones = this.c.getPhones();
			emails = this.c.getEmails();

			nameField.setText(name.formattedName());
			for (Phone p : phones) {
				LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.phone_field, phoneFieldsLayout);
				EditText tf = (EditText) ll.findViewById(R.id.phone_type_field);
				EditText nf = (EditText) ll
						.findViewById(R.id.phone_number_field);
				tf.setText(p.type());
				nf.setText(p.number());
				phoneFields.add(ll);
			}
			for (Email e : emails) {
				LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.email_field, emailFieldsLayout);
				EditText tf = (EditText) ll.findViewById(R.id.email_type_field);
				EditText af = (EditText) ll.findViewById(R.id.email_type_field);
				tf.setText(e.type());
				af.setText(e.address());
				emailFields.add(ll);
			}
			addPhoneButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					inflater.inflate(R.layout.phone_field, phoneFieldsLayout);
				}
			});
			addEmailButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					inflater.inflate(R.layout.email_field, emailFieldsLayout);
				}
			});

			// TODO emails & phones too.
		}
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
		getMenuInflater().inflate(R.menu.edit_one_contact, menu);
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
			Contact c;
			if (this.c != null) {
				c = this.c;
				Name name = new Name(
						((TextView) findViewById(R.id.person_name_field))
								.getText().toString());
				c.putName(name);
			} else {
				c = (new Contact.Builder(
						((TextView) findViewById(R.id.person_name_field))
								.getText().toString())).build();
			}
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d("EditOneContact#onConfigurationChanged",
				"entered onConfigurationChanged()");
	}
}
