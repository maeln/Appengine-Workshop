package com.zenika.zencontact.resource;


import com.zenika.zencontact.email.Email;
import com.zenika.zencontact.email.EmailService;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

/**
 * Created by valentin on 01/03/2017.
 */
@Component
@RestxResource
public class EmailResource {
	private EmailService service = EmailService.instance();

	@POST("/v2/email")
	@PermitAll
	public void sendEmail(Email email) {
		service.sendEmail(email);
	}
}