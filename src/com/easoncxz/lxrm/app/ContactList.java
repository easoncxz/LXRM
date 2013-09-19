package com.easoncxz.lxrm.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easoncxz.lxrm.R;

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
		return list.size();
	}

	/**
	 * @return a {@link Contact} object.
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	/**
	 * @deprecated
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	/**
	 * @deprecated
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
