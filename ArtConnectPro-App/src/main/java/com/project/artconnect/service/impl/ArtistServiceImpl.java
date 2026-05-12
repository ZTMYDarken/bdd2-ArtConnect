package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.dao.impl.ArtistDaoImpl;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArtistServiceImpl implements ArtistService {

    private final ArtistDao artistDao = new ArtistDaoImpl();

    @Override
    public List<Artist> getAllArtists() {
        return artistDao.findAll();
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        // Logique de recherche simple par nom
        return artistDao.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void createArtist(Artist artist) {
        artistDao.save(artist);
    }

    @Override
    public void updateArtist(Artist artist) {
        artistDao.update(artist);
    }

    @Override
    public void deleteArtist(String name) {
        artistDao.delete(name);
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return List.of(
                new Discipline("Painting"),
                new Discipline("Sculpture"),
                new Discipline("Photography"),
                new Discipline("Mixed Media"),
                new Discipline("Installation"),
                new Discipline("Textile Art")
        );
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        return artistDao.findAll().stream()
                .filter(artist -> {
                    boolean matchesQuery = query == null || query.isEmpty() ||
                            artist.getName().toLowerCase().contains(query.toLowerCase());

                    boolean matchesCity = city == null || city.isEmpty() ||
                            artist.getCity().equalsIgnoreCase(city);

                    // On vérifie si une des disciplines de l'artiste correspond
                    boolean matchesDiscipline = disciplineName == null ||
                            artist.getDisciplines().stream()
                                    .anyMatch(d -> d.getName().equalsIgnoreCase(disciplineName));

                    return matchesQuery && matchesCity && matchesDiscipline;
                })
                .collect(Collectors.toList());
    }
}