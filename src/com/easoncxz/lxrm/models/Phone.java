package com.easoncxz.lxrm.models;

/**
 * This class is made public only so that it can be used as a return type as
 * public methods of Contact.
 * <p>
 * static so that it can be used by the Builder.
 */
public class Phone {

	private long id = -1;
	private String type;
	private String number;

	/**
	 * This public constructor is provided for Activities to update contact
	 * details.
	 */
	public Phone(long id, String type, String number) {
		this.id = id;
		this.type = type;
		this.number = number;
	}

	public long id() {
		return id;
	}

	public String type() {
		return type;
	}

	public String number() {
		return number;
	}

}