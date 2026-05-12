package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtworkDao implements ArtworkDao {

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();

        // On demande TOUT de l'oeuvre (aw.*) ET le NOM de l'artiste (ar.name)
        String sql = """
        SELECT aw.*, ar.name as artist_name 
        FROM Artwork aw 
        LEFT JOIN Artist ar ON aw.id_artist = ar.id_artist 
        ORDER BY aw.title
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                artworks.add(mapResultSetToArtwork(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = """
            INSERT INTO Artwork 
            (id_artwork, title, creationYear, type, medium, dimensions, description, price, status, id_artist) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, getNextId());
            statement.setString(2, artwork.getTitle());
            statement.setInt(3, artwork.getCreationYear());
            statement.setString(4, artwork.getType());
            statement.setString(5, artwork.getMedium());
            statement.setString(6, artwork.getDimensions());
            statement.setString(7, artwork.getDescription());
            statement.setDouble(8, artwork.getPrice());
            statement.setString(9, artwork.getStatus().name());
            statement.setNull(10, Types.INTEGER); // Par défaut null si on ne gère pas encore l'ID de l'artiste

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = """
            UPDATE Artwork 
            SET creationYear = ?, type = ?, medium = ?, dimensions = ?, description = ?, price = ?, status = ?,
            id_artist = (SELECT id_artist FROM Artist WHERE name = ?)
            WHERE title = ?
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, artwork.getCreationYear());
            statement.setString(2, artwork.getType());
            statement.setString(3, artwork.getMedium());
            statement.setString(4, artwork.getDimensions());
            statement.setString(5, artwork.getDescription());
            statement.setDouble(6, artwork.getPrice());
            statement.setString(7, artwork.getStatus().name());

            // On envoie le NOM de l'artiste pour que la sous-requête SQL trouve l'ID
            statement.setString(8, artwork.getArtist() != null ? artwork.getArtist().getName() : null);

            // Le titre sert de clé pour trouver quelle œuvre modifier
            statement.setString(9, artwork.getTitle());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.err.println("Aucune œuvre mise à jour. Le titre a peut-être changé ?");
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Artwork WHERE title = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        // Jointure avec la table Artist pour filtrer par nom
        String sql = """
            SELECT aw.* FROM Artwork aw 
            JOIN Artist a ON aw.id_artist = a.id_artist 
            WHERE a.name = ?
        """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, artistName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    artworks.add(mapResultSetToArtwork(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    private int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_artwork), 0) + 1 FROM Artwork";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private Artwork mapResultSetToArtwork(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setTitle(rs.getString("title"));
        artwork.setCreationYear(rs.getInt("creationYear"));
        artwork.setType(rs.getString("type"));
        artwork.setMedium(rs.getString("medium"));
        artwork.setDimensions(rs.getString("dimensions"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        artwork.setStatus(Artwork.Status.valueOf(rs.getString("status")));

        String name = rs.getString("artist_name");
        if (name != null) {
            Artist artist = new Artist();
            artist.setName(name);
            artwork.setArtist(artist);
        }

        return artwork;
    }
}