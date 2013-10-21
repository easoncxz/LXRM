package com.easoncxz.lxrm.models;

/**
 * This class is made public only so that it can be used as a return type as
 * public methods of Contact.
 * <p>
 * static so that it can be used by the Builder.
 */
public class Email {

	private long id = -1;
	private String type;
	private String address;

	/**
	 * This public constructor is provided for Activities to update contact
	 * details.
	 */
	public Email(long id, String type, String address) {
		this.id = id;
		this.type = type;
		this.address = address;
	}

	public long id() {
		return id;
	}

	public String type() {
		return type;
	}

	public String number() {
		return address;
	}

}