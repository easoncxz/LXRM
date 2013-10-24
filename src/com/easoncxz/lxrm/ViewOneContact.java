package com.easoncxz.lxrm;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
public class ViewOneContact extends Activity {

	public static final String RESULT_WANTED_TO_EDIT = "I want to edit this c";

	private Contact c;
	private DataStore ds;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @deprecated
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("ViewOneContact#onCreate", "entered onCreate()");
		setContentView(R.layout.activity_view_one_contact);

		setupActionBar();

		ds = DataStoreFactory.getDataStore(this);
		LinearLayout phonesLayout = (LinearLayout) findViewById(R.id.phone_labels_layout);
		LinearLayout emailsLayout = (LinearLayout) findViewById(R.id.email_labels_layout);
		TextView nameField = (TextView) findViewById(R.id.person_name_label);
		Bundle extras = getIntent().getExtras();
		LayoutInflater inflater = getLayoutInflater();

		this.c = this.createContactFromExtras(extras, ds);

		this.updateNameLabel(this.c.getName(), nameField);
		this.inflatePhoneLabels(this.c.getPhones(), phonesLayout, inflater);
		this.inflateEmailLabels(this.c.getEmails(), emailsLayout, inflater);

	}

	private void updateNameLabel(Name name, TextView nameLabel) {
		nameLabel.setText(name.formattedName());
	}

	private void inflatePhoneLabels(List<Phone> phones,
			LinearLayout phonesLayout, LayoutInflater inflater) {
		for (Phone p : phones) {
			LinearLayout row = (LinearLayout) inflater.inflate(
					R.layout.item_phone_label, null);
			TextView type = (TextView) row.findViewById(R.id.phone_type_label);
			TextView number = (TextView) row
					.findViewById(R.id.phone_number_label);
			type.setText(p.type());
			number.setText(p.number());
			phonesLayout.addView(row);
		}
	}

	private void inflateEmailLabels(List<Email> emails,
			LinearLayout emailsLayout, LayoutInflater inflater) {
		for (Email e : emails) {
			LinearLayout row = (LinearLayout) inflater.inflate(
					R.layout.item_phone_label, null);
			TextView type = (TextView) row.findViewById(R.id.phone_type_label);
			TextView number = (TextView) row
					.findViewById(R.id.phone_number_label);
			type.setText(e.type());
			number.setText(e.address());
			emailsLayout.addView(row);
		}
	}

	private Contact createContactFromExtras(Bundle extras, DataStore ds) {
		Contact c;
		Log.v("ViewOneContact#onCreate",
				"extras is null? - " + Boolean.toString(extras == null));
		long id = extras.getLong(Contact.KEY_ID);
		if (id != -1) {
			// Log.v("ViewOneContact#onCreate", "datastore got");
			try {
				Log.v("ViewOneContact#onCreate",
						"id from extras: " + Long.toString(id));
				c = ds.get(id);
			} catch (ContactNotFoundException e) {
				// should never happen
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Cannot view an unsaved contact");
		}
		return c;
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
			Log.v("ViewOneContact_CXZ",
					"Trying to call setResult(int, Intent).");
			setResult(RESULT_OK, result);
			finish();
			return true;
		case R.id.action_delete:
			if (this.c != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.delete_this_contact);
				builder.setMessage((ViewOneContact.this.c == null ? "This contact "
						: ("\""
								+ ViewOneContact.this.c.getName()
										.formattedName() + "\" "))
						+ getString(R.string.delete_this_contact_how));
				// builder.setIcon(R.drawable.ic_action_sort_by_size_dark);
				builder.setCancelable(true);
				builder.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									ds.delete(ViewOneContact.this.c.getId());
									finish();
								} catch (ContactNotFoundException e) {
									e.printStackTrace();
								}
								Toast.makeText(ViewOneContact.this,
										R.string.contact_deleted,
										Toast.LENGTH_SHORT).show();
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
				return true;
			} else {
				finish();
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
