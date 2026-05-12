package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtistDao.
 * TODO: Students must implement this using JDBC and SQL.
 */
public class JdbcArtistDao implements ArtistDao {

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist ORDER BY name";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Artist artist = new Artist();
                // On récupère l'ID pour chercher les disciplines après
                int artistId = resultSet.getInt("id_artist");

                artist.setName(resultSet.getString("name"));
                artist.setBio(resultSet.getString("bio"));
                artist.setBirthYear(resultSet.getInt("birthYear"));
                artist.setContactEmail(resultSet.getString("contactEmail"));
                artist.setPhone(resultSet.getString("phone"));
                artist.setCity(resultSet.getString("city"));
                artist.setWebsite(resultSet.getString("website"));
                artist.setSocialMedia(resultSet.getString("socialMedia"));
                artist.setActive(resultSet.getBoolean("isActive"));

                // Récupération des disciplines
                artist.setDisciplines(findDisciplinesForArtist(artistId));

                artists.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    // Nouvelle méthode utilitaire pour charger les disciplines
    private List<Discipline> findDisciplinesForArtist(int artistId) {
        List<Discipline> disciplines = new ArrayList<>();
        // Requête de jointure entre la table de liaison et la table Discipline
        String sql = """
        SELECT d.name FROM Discipline d
        JOIN Artist_Discipline ad ON d.id_discipline = ad.id_discipline
        WHERE ad.id_artist = ?
    """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    disciplines.add(new Discipline(rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            // Si la table n'existe pas encore, on évite de faire planter tout le findAll
            System.err.println("Erreur disciplines: " + e.getMessage());
        }
        return disciplines;
    }

    @Override
    public void save(Artist artist) {
        // TODO: Implement INSERT INTO artist(...) VALUES(...)
        String sql = """
            INSERT INTO Artist
            (id_artist, name, birthYear, bio, contactEmail, phone, city, website, socialMedia, isActive)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, getNextId());
            statement.setString(2, artist.getName());
            statement.setObject(3, artist.getBirthYear());
            statement.setString(4, artist.getBio());
            statement.setString(5, artist.getContactEmail());
            statement.setString(6, artist.getPhone());
            statement.setString(7, artist.getCity());
            statement.setString(8, artist.getWebsite());
            statement.setString(9, artist.getSocialMedia());
            statement.setBoolean(10, artist.isActive());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artist artist) {
        // TODO: Implement UPDATE artist SET ... WHERE name = ?
        String sql = """
            UPDATE Artist
            SET name = ?, birthYear = ?, bio = ?, phone = ?, city = ?, website = ?, socialMedia = ?, isActive = ?
            WHERE contactEmail = ?
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, artist.getName());
            statement.setObject(2, artist.getBirthYear());
            statement.setString(3, artist.getBio());
            statement.setString(4, artist.getPhone());
            statement.setString(5, artist.getCity());
            statement.setString(6, artist.getWebsite());
            statement.setString(7, artist.getSocialMedia());
            statement.setBoolean(8, artist.isActive());
            statement.setString(9, artist.getContactEmail());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String artistName) {
        // TODO: Implement DELETE FROM artist WHERE name = ?
        String sql = "DELETE FROM Artist WHERE name = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, artistName);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artist> findByCity(String city) {
        // TODO: Implement SELECT * FROM artist WHERE city = ?
        List<Artist> artists = new ArrayList<>();

        String sql = "SELECT * FROM Artist WHERE city = ? ORDER BY name";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, city);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Artist artist = new Artist();

                    artist.setName(resultSet.getString("name"));
                    artist.setBio(resultSet.getString("bio"));
                    artist.setBirthYear(resultSet.getInt("birthYear"));
                    artist.setContactEmail(resultSet.getString("contactEmail"));
                    artist.setPhone(resultSet.getString("phone"));
                    artist.setCity(resultSet.getString("city"));
                    artist.setWebsite(resultSet.getString("website"));
                    artist.setSocialMedia(resultSet.getString("socialMedia"));
                    artist.setActive(resultSet.getBoolean("isActive"));

                    artists.add(artist);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    private int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_artist), 0) + 1 FROM Artist";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}
