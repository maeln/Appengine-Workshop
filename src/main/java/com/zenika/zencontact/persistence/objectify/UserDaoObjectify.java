package com.zenika.zencontact.persistence.objectify;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.domain.blob.PhotoService;
import com.zenika.zencontact.persistence.UserDao;

import java.util.List;

/**
 * Created by maeln on 28/02/17.
 */
public class UserDaoObjectify implements UserDao {
	private static UserDaoObjectify INSTANCE = new UserDaoObjectify();
	public static UserDaoObjectify instance() {
		ObjectifyService.factory().register(User.class);
		return INSTANCE;
	}

	private UserDaoObjectify() {};

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@Override
	public long save(User contact) {
		return ObjectifyService.ofy().save().entity(contact).now().getId();
	}

	@Override
	public void delete(Long id) {
		PhotoService.getInstance().deleteOldBlob(id);
		ObjectifyService.ofy().delete().key(Key.create(User.class, id));
	}

	@Override
	public User get(Long id) {
		return ObjectifyService.ofy().load().key(Key.create(User.class, id)).now();
	}

	@Override
	public List<User> getAll() {
		return ObjectifyService.ofy().load().type(User.class).list();
	}

	public BlobKey fetchOldBlob(long id) {
		return this.get(id).photoKey;
	}
}
