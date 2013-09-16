package com.easoncxz.lxrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
