package com.github.zigcat.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class PaginationService {
    public PaginationService() {
    }

    public <T> List<T> pagitation(Dao<T, Integer> dao, long page, long pageSize) throws SQLException {
        QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.offset((page * pageSize)-1).limit(pageSize);
        return dao.query(queryBuilder.prepare());
    }
}
