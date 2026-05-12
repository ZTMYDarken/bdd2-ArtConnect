package com.project.artconnect.persistence;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class JdbcExhibitionDao {

    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();
        // Jointure pour récupérer le nom de la galerie
        String sql = """
            SELECT e.*, g.name as gallery_name 
            FROM Exhibition e 
            LEFT JOIN Gallery g ON e.id_gallery = g.id_gallery 
            ORDER BY e.startDate
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Exhibition exhibition = mapResultSetToExhibition(rs);
                // On cherche les œuvres liées à cette exposition
                exhibition.setArtworks(findArtworksForExhibition(rs.getLong("id_exhibition")));
                exhibitions.add(exhibition);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exhibitions;
    }

    private Exhibition mapResultSetToExhibition(ResultSet rs) throws SQLException {
        Exhibition e = new Exhibition();
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setTheme(rs.getString("theme"));

        // Gestion de la date de début
        Timestamp startTs = rs.getTimestamp("startDate");
        if (startTs != null) {
            e.setStartDate(startTs.toLocalDateTime().toLocalDate());
        }

        // Gestion de la date de fin
        Timestamp endTs = rs.getTimestamp("endDate");
        if (endTs != null) {
            e.setEndDate(endTs.toLocalDateTime().toLocalDate());
        }

        // Mapping de l'objet Gallery
        String galleryName = rs.getString("gallery_name");
        if (galleryName != null) {
            Gallery gallery = new Gallery();
            gallery.setName(galleryName);
            e.setGallery(gallery);
        }

        return e;
    }

    // Méthode pour remplir la List<Artwork> demandée par ton modèle
    private List<Artwork> findArtworksForExhibition(Long exhibitionId) {
        List<Artwork> artworks = new ArrayList<>();
        // Requête sur ta table de liaison (souvent nommée Exhibition_Artwork ou similaire)
        String sql = """
            SELECT a.title FROM Artwork a
            JOIN Exhibition_Artwork ea ON a.id_artwork = ea.id_artwork
            WHERE ea.id_exhibition = ?
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, exhibitionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artwork art = new Artwork();
                    art.setTitle(rs.getString("title"));
                    artworks.add(art);
                }
            }
        } catch (SQLException e) {
            // Si la table de liaison n'existe pas encore, on retourne une liste vide
            return new ArrayList<>();
        }
        return artworks;
    }
}