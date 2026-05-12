package com.project.artconnect.persistence;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcGalleryDao {

    public List<Gallery> findAll() {
        List<Gallery> galleries = new ArrayList<>();
        String sql = "SELECT * FROM Gallery ORDER BY name";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                galleries.add(mapResultSetToGallery(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return galleries;
    }

    public Optional<Gallery> findById(Long id) {
        String sql = "SELECT * FROM Gallery WHERE id_gallery = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGallery(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Gallery mapResultSetToGallery(ResultSet rs) throws SQLException {
        Gallery g = new Gallery();
        g.setName(rs.getString("name"));
        g.setAddress(rs.getString("adress"));
        g.setOwnerName(rs.getString("ownerName"));
        g.setOpeningHours(rs.getString("openingHours"));
        g.setContactPhone(rs.getString("contactPhone"));
        g.setRating(rs.getDouble("rating"));
        g.setWebsite(rs.getString("website"));
        return g;
    }
}