package com.easoncxz.lxrm.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An ADT.
 */
public class ContactList extends BaseAdapter {

	@SuppressWarnings("deprecation")
	private List<Contact> list = new ArrayList<Contact>();
	private Context context;

	public ContactList(Context context) {
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	void addContact(Contact c) {
		this.list.add(c);
	}

	@Override
	public int getCount() {
		// return 0;
		return list.size();
	}

	/**
	 * @return a {@link Contact} object.
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// return null;
		return list.get(position);
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getItemId(int position) {
		// return 0;
		return list.get(position).getId();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// return null;

		// TextView tv = new TextView(parent.getContext());

		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// (int) LayoutParams.WRAP_CONTENT,
		// (int) LayoutParams.WRAP_CONTENT);
		// tv.setLayoutParams(params);
		// tv.setText(list.get(position).getFormattedName());

		// tv.setPadding(16, 16, 16, 16);
		// tv.setText(((Contact) this.getItem(position)).getFormattedName());
		// return tv;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(android.R.layout.simple_list_item_2, null);
		TextView t1 = (TextView) v.findViewById(android.R.id.text1);
		TextView t2 = (TextView) v.findViewById(android.R.id.text2);
		t1.setText(this.list.get(position).getFormattedName().toString());
		t2.setText(this.list.get(position).getPrimaryPhone().toString());
		return v;
	}
}
