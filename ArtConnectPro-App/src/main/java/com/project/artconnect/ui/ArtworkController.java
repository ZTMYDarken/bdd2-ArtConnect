package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import java.util.Optional;

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
        Dialog<Artwork> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une œuvre");
        dialog.setHeaderText("Remplissez les informations de l'œuvre");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField       = new TextField();
        TextField yearField        = new TextField();
        TextField typeField        = new TextField();
        TextField mediumField      = new TextField();
        TextField dimensionsField  = new TextField();
        TextArea  descriptionField = new TextArea();
        TextField priceField       = new TextField();
        ComboBox<Artwork.Status> statusBox = new ComboBox<>(
                FXCollections.observableArrayList(Artwork.Status.values()));
        TextField artistField      = new TextField();

        titleField.setPromptText("ex: La Joconde");
        yearField.setPromptText("ex: 2024");
        typeField.setPromptText("ex: Painting");
        mediumField.setPromptText("ex: Oil on canvas");
        dimensionsField.setPromptText("ex: 77x53 cm");
        descriptionField.setPromptText("Description...");
        descriptionField.setPrefRowCount(3);
        priceField.setPromptText("ex: 1500.0");
        statusBox.setValue(Artwork.Status.FOR_SALE);
        artistField.setPromptText("Nom de l'artiste");

        grid.add(new Label("Titre *"),       0, 0); grid.add(titleField,       1, 0);
        grid.add(new Label("Année"),         0, 1); grid.add(yearField,        1, 1);
        grid.add(new Label("Type"),          0, 2); grid.add(typeField,        1, 2);
        grid.add(new Label("Medium"),        0, 3); grid.add(mediumField,      1, 3);
        grid.add(new Label("Dimensions"),    0, 4); grid.add(dimensionsField,  1, 4);
        grid.add(new Label("Description"),   0, 5); grid.add(descriptionField, 1, 5);
        grid.add(new Label("Prix (€)"),      0, 6); grid.add(priceField,       1, 6);
        grid.add(new Label("Statut"),        0, 7); grid.add(statusBox,        1, 7);
        grid.add(new Label("Artiste"),       0, 8); grid.add(artistField,      1, 8);

        dialog.getDialogPane().setContent(grid);

        // Désactiver le bouton Ajouter tant que le titre est vide
        javafx.scene.Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldVal, newVal) ->
                addButton.setDisable(newVal.trim().isEmpty()));

        dialog.setResultConverter(buttonType -> {
            if (buttonType != addButtonType) return null;

            Artwork artwork = new Artwork();
            artwork.setTitle(titleField.getText().trim());
            artwork.setType(typeField.getText().trim());
            artwork.setMedium(mediumField.getText().trim());
            artwork.setDimensions(dimensionsField.getText().trim());
            artwork.setDescription(descriptionField.getText().trim());
            artwork.setStatus(statusBox.getValue());

            try { artwork.setCreationYear(Integer.parseInt(yearField.getText().trim())); }
            catch (NumberFormatException ignored) { artwork.setCreationYear(2026); }

            try { artwork.setPrice(Double.parseDouble(priceField.getText().trim())); }
            catch (NumberFormatException ignored) { artwork.setPrice(0.0); }

            String artistName = artistField.getText().trim();
            if (!artistName.isEmpty()) {
                Artist artist = new Artist();
                artist.setName(artistName);
                artwork.setArtist(artist);
            }

            return artwork;
        });

        Optional<Artwork> result = dialog.showAndWait();
        result.ifPresent(artwork -> {
            artworkService.createArtwork(artwork);
            refreshTable();
        });
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

