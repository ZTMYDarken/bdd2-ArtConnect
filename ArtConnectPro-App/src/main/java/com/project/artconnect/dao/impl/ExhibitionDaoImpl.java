package com.project.artconnect.dao.impl;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import java.util.List;

public class ExhibitionDaoImpl {
    private final JdbcExhibitionDao jdbcDao = new JdbcExhibitionDao();

    public List<Exhibition> findAll() {
        return jdbcDao.findAll();
    }
}