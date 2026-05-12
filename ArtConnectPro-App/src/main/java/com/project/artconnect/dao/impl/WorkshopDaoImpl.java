package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import java.util.List;
import java.util.Optional;

public class WorkshopDaoImpl implements WorkshopDao {
    private final JdbcWorkshopDao jdbcDao = new JdbcWorkshopDao();

    @Override
    public List<Workshop> findAll() {
        return jdbcDao.findAll();
    }

    @Override
    public Optional<Workshop> findById(Long id) {
        return jdbcDao.findById(id);
    }
}