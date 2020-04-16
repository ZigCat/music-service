package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.TagAlbumController;
import com.github.zigcat.ormlite.controllers.TagController;
import com.github.zigcat.ormlite.controllers.UserMusicController;
import com.github.zigcat.ormlite.models.Category;
import com.github.zigcat.ormlite.models.Music;
import com.github.zigcat.ormlite.models.Tag;
import com.github.zigcat.ormlite.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagService {

    private static Logger l = LoggerFactory.getLogger(TagService.class);

    public TagService() {
    }

    public List<Tag> listAll() throws SQLException {
        l.info("getting list of tags");
        return TagController.tagDao.queryForAll();
    }

    public Tag getById(int id) throws SQLException {
        l.info("@@@\tgetting tag by id");
        for(Tag t: listAll()){
            l.info("Iterating over "+t.toString());
            if(t.getId() == id){
                l.info("@@@\tgetting "+t.toString());
                return t;
            }
        }
        l.info("nothing got");
        return null;
    }

    public ArrayList<Tag> getByCategory(Category category) throws SQLException {
        l.info("@@@\tgetting tag by category");
        ArrayList<Tag> tags = new ArrayList<>();
        for(Tag t: TagController.tagService.listAll()){
            if(t.getCategory().getId() == category.getId()){
                l.info("@@@\tadding tag "+t.toString());
                tags.add(t);
            }
        }
        return tags;
    }

    public Tag mostPopular(ArrayList<Tag> tagList, User user) throws SQLException {
        l.info("@@@\tgetting most popular tag in list");
        ArrayList<Integer> tags = new ArrayList<>();
        int max, maxVar = 0;
        for(Tag i: tagList){
            if(!tags.contains(i.getId())){
                tags.add(i.getId());
            }
        }
        int[] nums = new int[tags.size()];
        for(int i=0;i<nums.length;i++){
            nums[i] = 0;
        }
        for(Tag t: tagList) {
            nums[tags.indexOf(t.getId())] += 1;
        }
        l.info("@@@\tchoosing tag");
        max = nums[0];
        for(int i=0;i<nums.length;i++){
            for (int num : nums) {
                if (nums[i] > num && nums[i] > maxVar) {
                    maxVar = nums[i];
                    max = tags.get(i);
                    l.info("max = "+max);
                } else if(nums[i] < num && num > maxVar){
                    maxVar = num;
                    max = tags.get(i);
                    l.info("max = "+max);
                }
            }
        }
        l.info("@@@\tgetting search tag by id "+max);
        return TagController.tagService.getById(max);
    }
}
