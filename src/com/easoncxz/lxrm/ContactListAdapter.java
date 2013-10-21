package com.easoncxz.lxrm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easoncxz.lxrm.backend.Contact;
import com.easoncxz.lxrm.backend.ContactList;

/**
 * This is a wrapper around the {@link ContactList} class.
 * <p>
 * This class interacts with UI elements.
 */
@SuppressWarnings("deprecation")
public class ContactListAdapter extends BaseAdapter {
	// will later implement SectionIndexer

	private ContactList contactList;
	private Context context;

	public ContactListAdapter() {
		super();
		// throw an error due to no context provided?
	}

	public ContactListAdapter(Context context, ContactList model) {
		this.context = context;
		this.contactList = model;
	}

	@Override
	public int getCount() {
		return contactList.size();
	}

	/**
	 * @return a {@link Contact} object.
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return contactList.get(position);
	}

	/**
	 * @deprecated due to Contact.
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return contactList.get(position).getId();
	}

	/**
	 * @deprecated
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(android.R.layout.simple_list_item_1, null);
		TextView t = (TextView) v.findViewById(android.R.id.text1);
		t.setText(((Contact) contactList.get(position)).getName()
				.formattedName().toString());
		return v;
	}

}
