package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        artistTable.setEditable(true); // Autorise l'édition du tableau

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // On rend la colonne Nom éditable avec un TextField
        nameColumn.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Artist artist = event.getRowValue();
            artist.setName(event.getNewValue());
            artistService.updateArtist(artist); // Appel au service pour sauvegarder en base
        });

        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Artist artist = event.getRowValue();
            artist.setCity(event.getNewValue());
            artistService.updateArtist(artist);
        });

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        emailColumn.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            Artist artist = event.getRowValue();
            artist.setContactEmail(event.getNewValue());
            artistService.updateArtist(artist);
        });

        // On définit la factory pour les nombres (Integer)
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        yearColumn.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        yearColumn.setOnEditCommit(event -> {
            Artist artist = event.getRowValue();
            artist.setBirthYear(event.getNewValue());
            artistService.updateArtist(artist); // Sauvegarde en base
        });

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        disciplineFilter.setConverter(new javafx.util.StringConverter<Discipline>() {
            @Override
            public String toString(Discipline d) {
                return (d == null) ? "Filter by Discipline" : d.getName();
            }
            @Override
            public Discipline fromString(String string) {
                return null;
            }
        });

        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = (searchField.getText() == null) ? "" : searchField.getText().trim();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;

        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    @FXML
    private void handleAddArtist() {
        // On crée un artiste par défaut pour tester la persistance JDBC.
        Artist newArtist = new Artist();
        newArtist.setName("Nouvel Artiste " + System.currentTimeMillis() % 1000);
        newArtist.setCity("Paris");
        newArtist.setContactEmail("contact@nouveau.com");
        newArtist.setBirthYear(1995);
        newArtist.setActive(true);

        // Envoi à la base de données
        artistService.createArtist(newArtist);

        // Rafraîchissement de l'affichage
        refreshTable();
    }

    @FXML
    private void handleDeleteArtist() {
        // On récupère l'artiste sélectionné dans le tableau
        Artist selected = artistTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            artistService.deleteArtist(selected.getName());
            refreshTable();
        } else {
            // Alerte si rien n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sélection vide");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un artiste dans le tableau.");
            alert.showAndWait();
        }
    }
}
