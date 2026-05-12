package com.project.artconnect.dao.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.persistence.JdbcArtistDao;
import java.util.List;

public class ArtistDaoImpl implements ArtistDao {

    private final JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();

    @Override
    public List<Artist> findAll() {
        return jdbcArtistDao.findAll();
    }

    @Override
    public void save(Artist artist) {
        jdbcArtistDao.save(artist);
    }

    @Override
    public void update(Artist artist) {
        jdbcArtistDao.update(artist);
    }

    @Override
    public void delete(String artistName) {
        jdbcArtistDao.delete(artistName);
    }

    @Override
    public List<Artist> findByCity(String city) {
        return jdbcArtistDao.findByCity(city);
    }
}