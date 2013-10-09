package com.easoncxz.lxrm.backend;

/**
 * A data abstraction class.
 * <p>
 * "Final" because of un-handled situations with potential problems with
 * inheriting from a class using a builder.
 * <p>
 * 
 * @deprecated {@link NameParser} involves fake implementation.
 */
public final class Contact {

	// what to do with un-initialized fields?
	private String firstName;
	private String lastName;
	private String middleName;
	private String formattedName;
	private String primaryPhone;
	private String primaryEmail;
	private long id = -1;

	/**
	 * "Final" due to same reason as {@link Contact} being "final".
	 */
	public static final class Builder {

		private final String inputtedName;
		private final String primaryPhone;
		private final String primaryEmail;
		private long id;

		public Builder(String inputtedName, String primaryPhone,
				String primaryEmail) {
			this.inputtedName = inputtedName;
			this.primaryEmail = primaryEmail;
			this.primaryPhone = primaryPhone;
		}

		/**
		 * Does nothing.
		 */
		public Builder secondaryEmail(String secondaryEmail) {
			return this;
		}
		
		public Builder id(long id){
			this.id = id;
			return this;
		}

		public Contact build() {
			return new Contact(this);
		}

	}

	/**
	 * Involves fake implementation.
	 * 
	 * @deprecated
	 */
	private class NameParser {

		// the below fields needs refactoring
		private String firstName;
		private String lastName;

		private NameParser(String inputtedName) {
			// fake implementation here
			this.firstName = inputtedName;
			this.lastName = "Gotcha";
		}

		/**
		 * Used by nesting class.
		 */
		@SuppressWarnings("unused")
		private String firstName() {
			return this.firstName;
		}

		/**
		 * Used by nesting class.
		 */
		@SuppressWarnings("unused")
		private String lastName() {
			return this.lastName;
		}

		/**
		 * Used by nesting class.
		 * 
		 * @deprecated fake implementation.
		 */
		private String middleName() {
			return "gotcha_middle_name";
		}

	}

	private Contact(Builder builder) {
		this.id = builder.id;
		this.primaryEmail = builder.primaryEmail;
		this.primaryPhone = builder.primaryPhone;
		NameParser np = new NameParser(builder.inputtedName);
		this.firstName = np.firstName;
		this.lastName = np.lastName;
		this.middleName = np.middleName();
		this.formattedName = this.firstName + " " + this.middleName + " "
				+ this.lastName;
	}

	// below: generated getters

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public long getId() {
		return id;
	}

}
