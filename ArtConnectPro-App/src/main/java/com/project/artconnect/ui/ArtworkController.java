package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, Artwork.Status> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();

    @FXML
    public void initialize() {
        artworkTable.setEditable(true);

        // Configuration des colonnes
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(event -> {
            Artwork artwork = event.getRowValue();
            String oldTitle = event.getOldValue(); // On garde l'ancien titre au cas où
            artwork.setTitle(event.getNewValue());

            // On passe l'objet mis à jour.
            artworkService.updateArtwork(artwork);
        });

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setOnEditCommit(event -> {
            Artwork artwork = event.getRowValue();
            artwork.setType(event.getNewValue());
            artworkService.updateArtwork(artwork);
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Artwork artwork = event.getRowValue();
            artwork.setPrice(event.getNewValue());
            artworkService.updateArtwork(artwork);
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        // On peut éditer le statut comme du texte (FOR_SALE, SOLD, etc.)
        statusColumn.setCellFactory(javafx.scene.control.cell.ComboBoxTableCell.forTableColumn(Artwork.Status.values()));
        statusColumn.setOnEditCommit(event -> {
            Artwork artwork = event.getRowValue();
            artwork.setStatus(event.getNewValue());
            artworkService.updateArtwork(artwork);
        });

        artistColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getArtist() != null) {
                return new SimpleStringProperty(cellData.getValue().getArtist().getName());
            } else {
                return new SimpleStringProperty("Unknown");
            }
        });
        artistColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        artistColumn.setOnEditCommit(event -> {
            Artwork artwork = event.getRowValue();
            String newArtistName = event.getNewValue();

            // On vérifie si l'artiste existe dans la base avant de valider
            boolean artistExists = ServiceProvider.getArtistService().getAllArtists().stream()
                    .anyMatch(a -> a.getName().equalsIgnoreCase(newArtistName));

            if (artistExists) {
                com.project.artconnect.model.Artist artist = new com.project.artconnect.model.Artist();
                artist.setName(newArtistName);
                artwork.setArtist(artist);
                artworkService.updateArtwork(artwork);
            } else {
                // Alerte si l'artiste n'existe pas
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("L'artiste '" + newArtistName + "' n'existe pas.");
                alert.showAndWait();
                refreshTable(); // On recharge pour annuler la saisie visuelle
            }
        });

        refreshTable();
    }

    @FXML
    private void handleAddArtwork() {
        Artwork newArt = new Artwork();
        newArt.setTitle("Nouvelle Oeuvre " + System.currentTimeMillis() % 1000);
        newArt.setType("Painting");
        newArt.setPrice(0.0);
        newArt.setStatus(Artwork.Status.FOR_SALE);
        newArt.setCreationYear(2026);

        artworkService.createArtwork(newArt);
        refreshTable();
    }

    @FXML
    private void handleDeleteArtwork() {
        Artwork selected = artworkTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            artworkService.deleteArtwork(selected.getTitle());
            refreshTable();
        }
    }

    private void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }
}

