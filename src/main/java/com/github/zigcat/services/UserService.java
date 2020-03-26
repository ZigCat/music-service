package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.UserController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.models.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    public List<User> listAll() throws SQLException {
        return UserController.userDao.queryForAll();
    }

    public User getById(int id) throws SQLException {
        for (User u : UserController.userDao.queryForAll()) {
            if (u.getId() == id) {
                return u;
            }
        }
        throw new CustomException(Security.badRequestMessage);
    }
}
