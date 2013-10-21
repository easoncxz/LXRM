package com.easoncxz.lxrm.models;

/**
 * This class is made public only so that it can be used as a return type as
 * public methods of Contact.
 * <p>
 * static so that it can be used by the Builder.
 * 
 * @deprecated there are bugs in how names are parsed.
 */
public class Name {

	private String firstName;
	private String lastName;
	private String middleNames = "";

	/**
	 * This public constructor is provided for Activities to update contact
	 * details.
	 */
	public Name(String inputtedName) {
		String[] parts = inputtedName.split("\\s+");
		if (!(parts.length >= 0)) {
			throw new RuntimeException(
					"Error with the contact name. See Contact$Name#Name.");
		}
		if (parts.length == 0) {
			firstName = "";
			lastName = "";
		} else if (parts.length == 1) {
			firstName = parts[0];
			lastName = "";
		} else if (parts.length == 2) {
			firstName = parts[0];
			lastName = parts[1];
		} else {
			if (!(parts.length > 2)) {
				throw new RuntimeException(
						"Error with the contact name. See Contact$Name#Name.");
			}
			firstName = parts[0];
			lastName = parts[parts.length - 1];
			StringBuilder b = new StringBuilder();
			for (int i = 1; i < parts.length - 1; i++) {
				b.append(parts[i] + " ");
			}
			middleNames = b.toString();
		}
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

	public String middleNames() {
		return middleNames;
	}

	public String formattedName() {
		return firstName + " " + middleNames + lastName;
	}

}