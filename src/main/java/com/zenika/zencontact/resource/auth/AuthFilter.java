package com.zenika.zencontact.resource.auth;

import com.google.common.base.Optional;
import restx.*;
import restx.factory.Component;
import restx.http.HttpStatus;

import java.io.IOException;

/**
 * Created by maeln on 01/03/17.
 */
@Component
public class AuthFilter implements RestxFilter {
	private AppEngineAuthHandler authHandler;

	public AuthFilter(AppEngineAuthHandler authHandler) {
		this.authHandler = authHandler;
	}

	@Override
	public Optional<RestxHandlerMatch> match(RestxRequest req) {
		return Optional.of(new RestxHandlerMatch(new StdRestxRequestMatch(
				"*users*", req.getRestxPath()), authHandler));
	}

	@Override
	public String toString() {
		return "AuthFilter";
	}

	@Component
	public static class AppEngineAuthHandler implements RestxHandler {
		@Override
		public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
			if(AuthentificationService.getInstance().getUser() != null
					&& AuthentificationService.getInstance().getUsername() != null) {
				resp.setHeader("Username", AuthentificationService.getInstance().getUsername());
				resp.setHeader("Logout", AuthentificationService.getInstance().getLogoutURL("/#/clear"));
			}
			if(!req.getRestxPath().endsWith("users")
					&& AuthentificationService.getInstance().getUser() == null) {
				resp.setHeader("Location", AuthentificationService.getInstance().getLoginURL("/#/edit/" + req.getRestxPath().split("/")[3]));
				resp.setHeader("Logout", AuthentificationService.getInstance().getLogoutURL("/#/clear"));
				resp.setStatus(HttpStatus.UNAUTHORIZED);
				return;
			}
			if (req.getHttpMethod() == "DELETE"
					&& !AuthentificationService.getInstance().isAdmin()) {
				resp.setStatus(HttpStatus.FORBIDDEN);
				return;
			}
			ctx.nextHandlerMatch().handle(req, resp, ctx);
		}
	}

}
