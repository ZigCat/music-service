package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class GroupService {
    private static Logger l = LoggerFactory.getLogger(GroupService.class);

    public GroupService() {
    }

    public List<Group> listAll() throws SQLException {
        l.info("getting list of Groups");
        return GroupController.groupDao.queryForAll();
    }

    public Group getById(int id) throws SQLException {
        l.info("@@@\tgetting group by id");
        for(Group g: GroupController.groupDao.queryForAll()){
            l.info("Iterating over "+g.toString());
            if(g.getId() == id){
                l.info("@@@\tgetting "+g.toString());
                return g;
            }
        }
        l.warn("@@@\tnothing got, check your code");
        return null;
    }
}
