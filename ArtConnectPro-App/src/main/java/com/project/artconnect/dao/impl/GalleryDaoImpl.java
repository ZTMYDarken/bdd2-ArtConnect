package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcGalleryDao;
import java.util.List;
import java.util.Optional;

public class GalleryDaoImpl implements GalleryDao {
    private final JdbcGalleryDao jdbcDao = new JdbcGalleryDao();

    @Override
    public List<Gallery> findAll() {
        return jdbcDao.findAll();
    }

    @Override
    public Optional<Gallery> findById(Long id) {
        return jdbcDao.findById(id);
    }
}