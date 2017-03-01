package com.zenika.zencontact.email;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by maeln on 01/03/17.
 */
public class MailHandler extends HttpServlet {
	private EmailService service = EmailService.instance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service.logEmail(req);
	}
}
