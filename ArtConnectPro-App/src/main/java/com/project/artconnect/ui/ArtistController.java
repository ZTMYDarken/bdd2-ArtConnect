package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

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
        Dialog<Artist> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un artiste");
        dialog.setHeaderText("Remplissez les informations de l'artiste");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField        = new TextField();
        TextField cityField        = new TextField();
        TextField emailField       = new TextField();
        TextField phoneField       = new TextField();
        TextField birthYearField   = new TextField();
        TextField websiteField     = new TextField();
        TextField socialMediaField = new TextField();
        TextArea  bioField         = new TextArea();
        CheckBox  activeBox        = new CheckBox("Actif");

        nameField.setPromptText("ex: Pablo Picasso");
        cityField.setPromptText("ex: Paris");
        emailField.setPromptText("ex: contact@artiste.com");
        phoneField.setPromptText("ex: 0612345678");
        birthYearField.setPromptText("ex: 1990");
        websiteField.setPromptText("ex: https://monsite.com");
        socialMediaField.setPromptText("ex: @artiste");
        bioField.setPromptText("Biographie...");
        bioField.setPrefRowCount(3);
        activeBox.setSelected(true);

        grid.add(new Label("Nom *"),         0, 0); grid.add(nameField,        1, 0);
        grid.add(new Label("Ville"),         0, 1); grid.add(cityField,        1, 1);
        grid.add(new Label("Email"),         0, 2); grid.add(emailField,       1, 2);
        grid.add(new Label("Téléphone"),     0, 3); grid.add(phoneField,       1, 3);
        grid.add(new Label("Année de naissance"), 0, 4); grid.add(birthYearField, 1, 4);
        grid.add(new Label("Site web"),      0, 5); grid.add(websiteField,     1, 5);
        grid.add(new Label("Réseaux sociaux"), 0, 6); grid.add(socialMediaField, 1, 6);
        grid.add(new Label("Biographie"),    0, 7); grid.add(bioField,         1, 7);
        grid.add(new Label("Statut"),        0, 8); grid.add(activeBox,        1, 8);

        dialog.getDialogPane().setContent(grid);

        // Bouton Ajouter désactivé tant que le nom est vide
        javafx.scene.Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        nameField.textProperty().addListener((obs, oldVal, newVal) ->
                addButton.setDisable(newVal.trim().isEmpty()));

        dialog.setResultConverter(buttonType -> {
            if (buttonType != addButtonType) return null;

            Artist artist = new Artist();
            artist.setName(nameField.getText().trim());
            artist.setCity(cityField.getText().trim());
            artist.setContactEmail(emailField.getText().trim());
            artist.setPhone(phoneField.getText().trim());
            artist.setWebsite(websiteField.getText().trim());
            artist.setSocialMedia(socialMediaField.getText().trim());
            artist.setBio(bioField.getText().trim());
            artist.setActive(activeBox.isSelected());

            try { artist.setBirthYear(Integer.parseInt(birthYearField.getText().trim())); }
            catch (NumberFormatException ignored) {}

            return artist;
        });

        Optional<Artist> result = dialog.showAndWait();
        result.ifPresent(artist -> {
            artistService.createArtist(artist);
            refreshTable();
        });
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
