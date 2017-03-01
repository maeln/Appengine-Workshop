package com.zenika.zencontact.persistence;

import com.google.appengine.api.datastore.*;
import com.zenika.zencontact.domain.User;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maeln on 28/02/17.
 */
public class UserDaoDataStore implements UserDao {
	private static UserDaoDataStore INSTANCE = new UserDaoDataStore();
	public static UserDaoDataStore instance() {
		return INSTANCE;
	}

	private UserDaoDataStore() {};

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@Override
	public long save(User contact) {
		Entity e = new Entity("User");
		if(contact.id != null) {
			Key k = KeyFactory.createKey("User", contact.id);
			try {
				e = datastore.get(k);
			} catch (EntityNotFoundException e1) {}
		}

		e.setProperty("firstname", contact.firstName);
		e.setProperty("lastname", contact.lastName);
		e.setProperty("email", contact.email);
		e.setProperty("birthdate", contact.birthdate);
		e.setProperty("notes", contact.notes);
		if(contact.birthdate != null) {
			e.setProperty("birthdate", contact.birthdate);
		}
		Key key = datastore.put(e);
		return key.getId();
	}

	@Override
	public void delete(Long id) {
		Key k = KeyFactory.createKey("User", id);
		datastore.delete(k);
	}

	@Override
	public User get(Long id) {
		Entity e = null;
		try {
			e = datastore.get(KeyFactory.createKey("User", id));
		} catch (EntityNotFoundException e1) {}
		return User.create()
				.id(id)
				.firstName((String) e.getProperty("firstname"))
				.lastName((String) e.getProperty("lastname"))
				.email((String) e.getProperty("email"))
				.birthdate((Date) e.getProperty("birthdate"))
				.notes((String) e.getProperty("notes"));
	}

	@Override
	public List<User> getAll() {
		List<User> contacts = new LinkedList<>();
		Query q = new Query("User")
				.addProjection(new PropertyProjection("firstname", String.class))
				.addProjection(new PropertyProjection("lastname", String.class))
				.addProjection(new PropertyProjection("email", String.class))
				.addProjection(new PropertyProjection("notes", String.class));
		PreparedQuery pq = datastore.prepare(q);
		for(Entity e : pq.asIterable()) {
			contacts.add(User.create()
					.id(e.getKey().getId())
					.firstName((String) e.getProperty("firstname"))
					.lastName((String) e.getProperty("lastName"))
					.email((String) e.getProperty("email"))
					.notes((String) e.getProperty("notes")));
		}

		return contacts;
	}
}
