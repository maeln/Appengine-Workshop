package com.zenika.zencontact.resource;

import com.google.common.base.Optional;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.domain.blob.PhotoService;
import com.zenika.zencontact.persistence.UserDaoDataStore;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

/**
 * Created by maeln on 28/02/17.
 */
@Component
@RestxResource
public class UserObjectifyResource {
	private static UserDaoObjectify inst = UserDaoObjectify.instance();

	@GET("/v2/users")
	@PermitAll
	/**
	 * Get the users defined in a arraylist : only mock objects
	 */
	public Iterable<User> getAllUsers() {
		return inst.getAll();
	}

	@GET("/v2/users/{id}")
	@PermitAll
	public Optional<User> getUser(final Long id) {
		Optional<User> user = Optional.fromNullable(inst.get(id));
		PhotoService.getInstance().prepareDownloadURL(user.get());
		PhotoService.getInstance().prepareUploadURL(user.get());
		return user;
	}

	@PUT("/v2/users/{id}")
	@PermitAll
	public Optional<User> updateUser(final Long id, final User user) {
		return Optional.fromNullable(inst.get(inst.save(user.id(id))));
	}

	@DELETE("/v2/users/{id}")
	@PermitAll
	public void deleteUser(final Long id) {
		inst.delete(id);
	}

	@POST("/v2/users")
	@PermitAll
	public User storeUser(final User user) {
		return inst.get(inst.save(user));
	}

}
