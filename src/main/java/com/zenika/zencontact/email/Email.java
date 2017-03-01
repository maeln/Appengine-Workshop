package com.zenika.zencontact.email;

/**
 * Created by maeln on 01/03/17.
 */
public class Email {
	public String subject;
	public String body;
	public String to;
	public String toName;

	@Override
	public String toString() {
		return "Email{" +
				"subject='" + subject + '\'' +
				", body='" + body + '\'' +
				", to='" + to + '\'' +
				", toName='" + toName + '\'' +
				'}';
	}
}
