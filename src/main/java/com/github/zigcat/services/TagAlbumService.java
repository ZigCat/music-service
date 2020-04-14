package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.TagAlbumController;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.Tag;
import com.github.zigcat.ormlite.models.TagAlbum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagAlbumService {
    private static Logger l = LoggerFactory.getLogger(TagService.class);

    public TagAlbumService() {
    }

    public List<TagAlbum> listAll() throws SQLException {
        return TagAlbumController.taDao.queryForAll();
    }

    public TagAlbum getById(int id) throws SQLException {
        l.info("@@@\tgetting tagAlbum by id");
        for(TagAlbum ta: listAll()){
            l.info("Iterating over "+ta.toString());
            if(ta.getId() == id){
                l.info("@@@\tgetting "+ta.toString());
                return ta;
            }
        }
        l.info("nothing got");
        return null;
    }

    public ArrayList<Integer> getByAlbum(Album album) throws SQLException {
        l.info("@@@\tgetting tags by album "+album);
        ArrayList<Integer> tagList = new ArrayList<>();
        for(TagAlbum ta: listAll()){
            l.info("Iterating over "+ta.toString());
            if(ta.getAlbum().getId() == album.getId()){
                l.info("@@@\tadd "+ta.getTag());
                tagList.add(ta.getTag().getId());
            }
        }
        l.info("@@@\tgetting list");
        return tagList;
    }

    public ArrayList<Album> getByTag(Tag t) throws SQLException {
        l.info("@@@\tgetting album by tag");
        ArrayList<Album> albums = new ArrayList<>();
        for(TagAlbum ta: listAll()){
            l.info("Iterating over "+ta.toString());
            if(ta.getTag().getId() == t.getId()){
                l.info("@@@\tadd "+ta.getAlbum().toString());
                albums.add(ta.getAlbum());
            }
        }
        l.info("@@@\tgetting list");
        return albums;
    }
}
