package com.zenika.zencontact.resource;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.base.Optional;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.domain.blob.PhotoService;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by maeln on 28/02/17.
 */
@Component
@RestxResource
public class UserObjectifyResource {
	private final static int ALL_CONTACTS = 1;

	private final static Logger LOG = Logger.getLogger("CACHE");
	private final static UserDaoObjectify inst = UserDaoObjectify.instance();

	private MemcacheService cache = MemcacheServiceFactory.getMemcacheService();


	@GET("/v2/users")
	@PermitAll
	/**
	 * Get the users defined in a arraylist : only mock objects
	 */
	public Iterable<User> getAllUsers() {
		List<User> contacts = (List<User>) cache.get(ALL_CONTACTS);
		if(contacts != null) {
			LOG.warning("Cache HIT.");
			return contacts;
		} else {
			LOG.warning("Cache MISS.");
			contacts = inst.getAll();
			cache.put(ALL_CONTACTS, contacts);
		}
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
		cache.delete(ALL_CONTACTS);
		inst.delete(id);
	}

	@POST("/v2/users")
	@PermitAll
	public User storeUser(final User user) {
		cache.delete(ALL_CONTACTS);
		return inst.get(inst.save(user));
	}

}
