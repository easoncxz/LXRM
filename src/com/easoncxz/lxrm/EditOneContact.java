package com.easoncxz.lxrm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
	private LinearLayout phoneVerticalLinearLayout;
	private LinearLayout emailVerticalLinearLayout;
	private Button addPhoneButton;
	private Button addEmailButton;
	private List<LinearLayout> phoneRows = new ArrayList<LinearLayout>();
	private List<LinearLayout> emailRows = new ArrayList<LinearLayout>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("EditOneContact#onCreate", "entered onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_one_contact);
		setupActionBar();
		initializeUiElementsHandles();

		ds = DataStoreFactory.getDataStore(this);

		Bundle extras = getIntent().getExtras();
		this.c = this.getContactFromExtras(extras);

		if (this.c == null) {
			this.prepareUiForNewContact();
		} else {
			this.prepareUiForExistingContact(this.c);
		}
		Log.v("EditOneContact#onCreate", "exitting onCreate()");
	}

	private void prepareUiForExistingContact(Contact c) {
		// We are editing an existing contact.
		// The old information about the contact needs to be displayed.
		Name name = c.getName();
		List<Phone> phones = c.getPhones();
		List<Email> emails = c.getEmails();

		nameField.setText(name.formattedName());

		for (Phone p : phones) {
			LinearLayout row = (LinearLayout) inflater.inflate(
					R.layout.phone_field, null);
			row.setTag(Integer.valueOf(Long.toString(p.id())));
			EditText tf = (EditText) row.findViewById(R.id.phone_type_field);
			EditText nf = (EditText) row.findViewById(R.id.phone_number_field);
			Log.v("EditOneContact#prepareUiForExistingContact",
					"type of this retrieved phone: " + p.type());
			Log.v("EditOneContact#prepareUiForExistingContact",
					"number of this retrieved phone: " + p.number());
			tf.setText(p.type());
			nf.setText(p.number());
			phoneVerticalLinearLayout.addView(row);
			phoneRows.add(row);
		}
		for (LinearLayout row : phoneRows) {
			Log.v("EditOneContact#prepareUiForExistingContact",
					"hashCode() of this rows: " + row.hashCode());
		}

		for (Email e : emails) {
			LinearLayout row = (LinearLayout) inflater.inflate(
					R.layout.email_field, emailVerticalLinearLayout);
			EditText tf = (EditText) row.findViewById(R.id.email_type_field);
			EditText af = (EditText) row.findViewById(R.id.email_address_field);
			tf.setText(e.type());
			af.setText(e.address());
			emailRows.add(row);
			// TODO
		}
	}

	private void prepareUiForNewContact() {
		// We are creating new contact.
		// No need to fill in any text boxes.
		Toast.makeText(this, "Creating new contact", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Simply assigns to the object fields which are handles to UI elements.
	 */
	private void initializeUiElementsHandles() {
		inflater = getLayoutInflater();
		nameField = (TextView) findViewById(R.id.person_name_field);
		phoneVerticalLinearLayout = (LinearLayout) findViewById(R.id.phone_fields_layout);
		emailVerticalLinearLayout = (LinearLayout) findViewById(R.id.email_fields_layout);
		addPhoneButton = (Button) findViewById(R.id.add_phone_button);
		addEmailButton = (Button) findViewById(R.id.add_email_button);
		addPhoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout row = (LinearLayout) inflater.inflate(
						R.layout.phone_field, null);
				row.setTag(-1);
				// to be interpreted as a contact id; indicating a new contact
				phoneRows.add(row);
				phoneVerticalLinearLayout.addView(row);
			}
		});
		addEmailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout row = (LinearLayout) inflater.inflate(
						R.layout.email_field, null);
				row.setTag(-1);
				// to be interpreted as a contact id; indicating a new contact
				emailRows.add(row);
				emailVerticalLinearLayout.addView(row);
			}
		});
	}

	private Contact getContactFromExtras(Bundle extras) {
		Contact c;
		if (extras != null) {
			// we are editing an existing contact.
			long id = extras.getLong(Contact.KEY_ID, -1);
			// That key should always be in the extras! The -1 should never be
			// needed!
			try {
				c = ds.get(id);
			} catch (ContactNotFoundException e) {
				// this should never happen
				throw new RuntimeException(e);
			}
		} else {
			// we are editing a new contact.
			Log.v("EditOneContact",
					"We know that the extras passed to us are null");
			c = null;
		}
		return c;
	}

	/**
	 * Set up the {@link android.app.ActionBar}. Show the Up button in the
	 * action bar.
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
			// Grab the content in the UI elements,
			// create a Contact object,
			// then store it in the DataStore.
			Log.v("EditOneContact#onOptionsItemSelected", "nameField==null? - "
					+ (nameField == null));
			Name name = new Name(nameField.getText().toString());
			Log.v("EditOneContact#onOptionsItemSelected",
					"We are now trying to save this contact: ("
							+ (this.c == null ? "null" : this.c.getId()) + ") "
							+ name.formattedName());
			if (this.c == null) {
				// We should save a new contact.
				this.c = (new Contact.Builder(name.formattedName())).build();

			} else {
				// We should update an existing contact.
				this.c.putName(name);
			}
			// Now we have a Contact object which has a correct id.

			Log.d("EditOneContact#onOptionsItemSelected", "We can see "
					+ phoneRows.size() + " rows of EditText here.");
			for (LinearLayout row : phoneRows) {
				Phone p = this.createPhoneFromRow(row);
				this.c.putPhone(p);
			}
			for (LinearLayout row : emailRows) {
				Email e = this.createEmailFromRow(row);
				this.c.putEmail(e);
			}

			Log.v("EditOneContact#onOptionsItemSelected",
					"we're about to call put(). the id of the contact that is to be put: "
							+ Long.toString(this.c.getId()));
			long id = ds.put(c);
			Log.v("EditOneContact#onOptionsItemSelected",
					"id returned from ds#put(Contact): " + Long.toString(id));

			Intent result = new Intent();
			Bundle b = new Bundle();
			b.putLong(Contact.KEY_ID, id);
			result.putExtras(b);
			setResult(RESULT_OK, result);
			Log.v("EditOneContact#onOptionsItemSelected", "to call finish()");
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private Email createEmailFromRow(LinearLayout row) {
		long rowId = Long.valueOf(Integer.toString((Integer) row.getTag()));
		EditText tf = (EditText) row.findViewById(R.id.email_type_field);
		EditText af = (EditText) row.findViewById(R.id.email_address_field);
		Email e = new Email(rowId, tf.getText().toString(), af.getText()
				.toString());
		return e;
	}

	private Phone createPhoneFromRow(LinearLayout row) {
		// For some reason this method is really buggy.
		// Pay more attention to which LinearLayout "row" actually is.
		long rowId = Long.valueOf(Integer.toString((Integer) row.getTag()));
		Log.v("EditOneContact#onOptionsItemSelected",
				"the LinearLayout we are looking at: #" + row.hashCode() + " ("
						+ rowId + ")\n" + "This 'row' has: "
						+ row.getChildCount() + " children views");
		// row.setBackgroundColor(Color.YELLOW);
		EditText tf = (EditText) row.findViewById(R.id.phone_type_field);
		// EditText tf = (EditText) row.getChildAt(0);
		EditText nf = (EditText) row.findViewById(R.id.phone_number_field);
		// EditText nf = (EditText) row.getChildAt(1);
		Log.v("EditOneContact#onOptionsItemSelected",
				"The EditText we are looking at: " + tf.hashCode() + ": "
						+ nf.hashCode());
		String tt = tf.getText().toString();
		String nt = nf.getText().toString();
		Log.v("EditOneContact#onOptionsItemSelected", "The parsed user input: "
				+ tt + ": " + nt + " (at row no. " + rowId + ")");
		Phone p = new Phone(rowId, tt, nt);
		return p;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.v("EditOneContact#onConfigurationChanged",
				"entered onConfigurationChanged()");
	}
}
