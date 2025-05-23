package com.app.photoservice.service;

import java.io.InputStream;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PhotoStorageService {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	public String savePhoto(InputStream photoStream, String filename, String contentType) {
		ObjectId id = gridFsTemplate.store(photoStream, filename, contentType);
		return id.toString();
	}

	public GridFsResource getPhoto(String fileId) {
		return Optional.ofNullable(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId))))
				.map(gridFsTemplate::getResource).orElseThrow(() -> new RuntimeException("File not found"));
	}
	
	 public void deletePhoto(String fileId) {
	        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
	    }

}
