package com.zenika.zencontact.domain.blob;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoService {

	private static final Logger LOG = Logger.getLogger(PhotoService.class
			.getName());
	private static PhotoService INSTANCE = new PhotoService();
	private UserDaoObjectify contactsDao = UserDaoObjectify.instance();
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	public static PhotoService getInstance() {
		return INSTANCE;
	}

	public void deleteOldBlob(Long id) {
		BlobKey blobKey = contactsDao.fetchOldBlob(id);
		if (blobKey != null) {
			blobstoreService.delete(blobKey);
		}
	}

	public void prepareUploadURL(User contact) {
		String uploadURL = blobstoreService.createUploadUrl("/api/v2/users/"
				+ contact.id + "/photo");
		LOG.warning("upload URL  : " + uploadURL);
		contact.uploadURL(uploadURL);
	}

	public void prepareDownloadURL(User contact) {
		BlobKey photoKey = contact.photoKey;
		if (photoKey != null) {
			String url = "/api/v2/users/" + contact.id + "/photo/"
					+ photoKey.getKeyString();
			contact.downloadURL(url);
		}
	}

	public void updatePhoto(Long id, HttpServletRequest req) {
		Map<String, List<BlobKey>> uploads = blobstoreService.getUploads(req);
		if (!uploads.keySet().isEmpty()) {
			// delete old photo from BlobStore to save disk space
			deleteOldBlob(id);
			// update photo BlobKey in Contact entity
			Iterator<String> names = uploads.keySet().iterator();
			String name = names.next();
			List<BlobKey> keys = uploads.get(name);
			User contact = UserDaoObjectify.instance().get(id)
					.photoKey(keys.get(0));
			UserDaoObjectify.instance().save(contact);
		}
	}

	public void serve(BlobKey blobKey, HttpServletResponse resp)
			throws IOException {
		BlobInfoFactory blobInfoFactory = new BlobInfoFactory(
				DatastoreServiceFactory.getDatastoreService());
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
		LOG.log(Level.INFO, "Serving " + blobInfo.getFilename());
		resp.setHeader("Content-Disposition", "attachment; filename="
				+ blobInfo.getFilename());
		blobstoreService.serve(blobKey, resp);
	}
}