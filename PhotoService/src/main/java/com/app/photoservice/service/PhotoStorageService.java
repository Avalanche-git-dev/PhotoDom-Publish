package com.app.photoservice.service;

import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;


@Service
public class PhotoStorageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String savePhoto(InputStream photoStream, String filename, String contentType) {
        ObjectId id = gridFsTemplate.store(photoStream, filename, contentType);
        return id.toString();
    }

    public GridFsResource getPhoto(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file != null) {
            return gridFsTemplate.getResource(file);
        }
        throw new RuntimeException("File not found");
    }
    
    
//    public Mono<String> savePhoto(InputStream photoStream, String filename, String contentType) {
//        return Mono.fromCallable(() -> {
//            ObjectId id = gridFsTemplate.store(photoStream, filename, contentType);
//            return id.toString();
//        });
//    }

}

