package com.zenika.zencontact.resource.auth;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Created by maeln on 01/03/17.
 */
public class AuthentificationService {
	private static AuthentificationService INSTANCE = new AuthentificationService();
	private UserService userService = UserServiceFactory.getUserService();

	public static AuthentificationService getInstance() {
		return INSTANCE;
	}

	public boolean isAuthenticated() {
		return userService.isUserLoggedIn();
	}

	public String getLoginURL(String url) {
		return userService.createLoginURL(url);
	}

	public String getLogoutURL(String url) {
		return userService.createLogoutURL(userService.createLoginURL(url));
	}

	public String getUsername() {
		return userService.getCurrentUser().getNickname();
	}

	public boolean isAdmin() {
		return userService.isUserAdmin();
	}

	public User getUser() {
		return userService.getCurrentUser();
	}
}
