package com.zenika.zencontact.resource;

import com.google.common.base.Optional;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserDaoDataStore;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

/**
 * Created by maeln on 28/02/17.
 */
@Component
@RestxResource
public class UserDataStoreResource {
	private static UserDaoDataStore inst = UserDaoDataStore.instance();

	@GET("/v1/users")
	@PermitAll
	/**
	 * Get the users defined in a arraylist : only mock objects
	 */
	public Iterable<User> getAllUsers() {
		return inst.getAll();
	}

	@GET("/v1/users/{id}")
	@PermitAll
	public Optional<User> getUser(final Long id) {
		return Optional.fromNullable(inst.get(id));
	}

	@PUT("/v1/users/{id}")
	@PermitAll
	public Optional<User> updateUser(final Long id, final User user) {
		return Optional.fromNullable(inst.get(inst.save(user.id(id))));
	}

	@DELETE("/v1/users/{id}")
	@PermitAll
	public void deleteUser(final Long id) {
		inst.delete(id);
	}

	@POST("/v1/users")
	@PermitAll
	public User storeUser(final User user) {
		return inst.get(inst.save(user));
	}

}
