package Controllers;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import Model.LecteurModel;
import application.ConnexionDb;
import application.Links;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
public class LecteurController extends GlobalController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TableColumn<LecteurModel, Date> Date_InscriptionColumn;
    @FXML
    private TableColumn<LecteurModel, Date> Date_NaissanceColumn;
    @FXML
    private TableColumn<LecteurModel, String> actionsColumn;
    @FXML
    private TableColumn<LecteurModel, String> adresseColumn;
    @FXML
    private Button ajouterLecteur;
    @FXML
    private TableColumn<LecteurModel, Integer> cinColumn;
    @FXML
    private TableColumn<LecteurModel, String> emailColumn;
    @FXML
    private Button exportPdfLecteur;
    @FXML
    private TableColumn<LecteurModel, String> genreColumn;
    @FXML
    private TableView<LecteurModel> lecteursTableView;
    @FXML
    private TableColumn<LecteurModel, String> nomColumn;
    @FXML
    private TableColumn<LecteurModel, Integer> phoneColumn;
    @FXML
    private TableColumn<LecteurModel, String> prenomColumn;
    @FXML
    private TextField rechercheLecteur;
    @FXML
    private Button rechercheLecteurBtn;

  
    @SuppressWarnings("exports")
	public LecteurModel selectedLecteur= null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    LecteurModel lecteur = null ;
	String query = null;
	Connection connection = null ;
    ObservableList<LecteurModel>  LecteurList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
			loadDate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    @FXML
    void openAddLecteur(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.crudLecteurLink));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Crud Lecteur");

        CrudLecteurController crudLecteurController = loader.getController();
        crudLecteurController.setLecteurController(this);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        newStage.setResizable(false);
        newStage.getIcons().add(icon);
        newStage.show();
    }
    @FXML
    private void rechercherLecteur(KeyEvent event) {
        String searchText = rechercheLecteur.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            refreshTable();
        } else {
            List<LecteurModel> filteredList = LecteurList.stream()
                    .filter(lecteur ->
                    		String.valueOf(lecteur.getCin()).contains(searchText) ||
                            lecteur.getNom().toLowerCase().contains(searchText)||
                            lecteur.getPhone().toLowerCase().contains(searchText)||
                            lecteur.getPrenom().toLowerCase().contains(searchText)||
                            lecteur.getAdresse().toLowerCase().contains(searchText)||
                            lecteur.getEmail().toLowerCase().contains(searchText)||
                            lecteur.getGenre().toLowerCase().contains(searchText)||    
                            String.valueOf(lecteur.getDate_Inscription()).contains(searchText)||
                    		String.valueOf(lecteur.getDate_Naissance()).contains(searchText))
                    .collect(Collectors.toList());
            lecteursTableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML void refreshTable() {
        try {
            LecteurList.clear();
            query = "SELECT * FROM `lecteur`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LecteurList.add(new  LecteurModel(
                        resultSet.getInt("cin"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("Adresse"),
                        resultSet.getString("Email"),
                        resultSet.getString("phone"),
                        resultSet.getString("genre"),
                        resultSet.getDate("Date_Inscription"),
                        resultSet.getDate("Date_Naissance"))
                	);
                lecteursTableView.setItems(LecteurList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LecteurController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void loadDate() throws SQLException {
        connection = ConnexionDb.getConnection();
        refreshTable();
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        Date_InscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Date_Inscription"));
        Date_NaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("Date_Naissance"));

        Callback<TableColumn<LecteurModel, String>, TableCell<LecteurModel, String>> cellFoctory = (TableColumn<LecteurModel, String> param) -> {
            final TableCell<LecteurModel, String> cell = new TableCell<LecteurModel, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                    	final Button deleteIcon = createButton("/images/delete.png");
      		            {
      		            	deleteIcon.setOnAction(event -> {
      		                    LecteurModel lecteur = (LecteurModel) getTableView().getItems().get(getIndex());
      		                    supprimerLecteur(lecteur);
      		                });
      		            }
                    	final Button editIcon = createButton("/images/update.png");
                    	{
                    		editIcon.setOnAction(event -> {
      		                  LecteurModel lecteur = (LecteurModel) getTableView().getItems().get(getIndex());
                              FXMLLoader loader = new FXMLLoader ();
                              loader.setLocation(getClass().getResource(Links.crudLecteurLink));
                              try {
                                  loader.load();
                              } catch (IOException ex) {
                                  Logger.getLogger(LecteurController.class.getName()).log(Level.SEVERE, null, ex);
                              }

                              CrudLecteurController crudLecteurController = loader.getController();
                              crudLecteurController.setUpdate(true);
                              
                              
                              crudLecteurController.setTextField(lecteur.getCin(), lecteur.getNom(), lecteur.getPrenom(),lecteur.getAdresse(),
                            		  lecteur.getEmail(),lecteur.getGenre(),lecteur.getPhone(),lecteur.getDate_Naissance());
                              crudLecteurController.titleLecteur.setText("Modifier Lecteur");
                              crudLecteurController.ajouterLecteur.setText("Modifier");
                              Parent parent = loader.getRoot();
                              Stage stage = new Stage();
                              stage.setScene(new Scene(parent));
                              stage.initStyle(StageStyle.UTILITY);
                              stage.show();
      		                });
                    	}
                    	final Button pdfIcon = createButton("/images/pdf.png");
                    	{
                    		pdfIcon.setOnAction(event -> {
      		                    LecteurModel lecteur = (LecteurModel) getTableView().getItems().get(getIndex());
                                try {
									exportToPDFOneBook(lecteur);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
      		                });
                    	}
                        HBox managebtn = new HBox(editIcon, deleteIcon,pdfIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        HBox.setMargin(pdfIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        actionsColumn.setCellFactory(cellFoctory);
        lecteursTableView.setItems(LecteurList);
    }
    private void supprimerLecteur(LecteurModel lecteur) {
	    String query = "DELETE FROM lecteur WHERE cin = ?";
	    try (Connection connection = ConnexionDb.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setLong(1, lecteur.getCin());
	        pstmt.executeUpdate();
	        afficherAlerte("Succès", "Lecteur supprimé avec succès.", AlertType.INFORMATION);
            refreshTable();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        afficherAlerte("Erreur", "Erreur SQL lors de la suppression du lecteur.", AlertType.ERROR);
	    }
	}
    private Button createButton(String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-color: none !important; -fx-text-fill: white !important;");

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        button.setGraphic(imageView);
        return button;
    }
    private void exportToPDFOneBook(LecteurModel lecteur) throws FileNotFoundException {
        Document docu = new Document();

        try {
            String format = "dd-MM-yy_HH-mm";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            java.util.Date date = new java.util.Date();

            // Show a file save dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.setInitialFileName(lecteur.getCin() + "_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph("Informations sur le lecteur:\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK)));
                docu.add(new Paragraph("Nom: " + lecteur.getNom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Prénom: " + lecteur.getPrenom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Cin: " + lecteur.getCin() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Adresse: " + lecteur.getAdresse() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Email: " + lecteur.getEmail() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Numéro telephone: " + lecteur.getPhone() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Genre: " + lecteur.getGenre() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Date de naissance: " + formater.format(lecteur.getDate_Naissance()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Date d'inscription: " + formater.format(lecteur.getDate_Inscription()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.close();
                Desktop.getDesktop().open(selectedFile);
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exportToPDF(MouseEvent event) throws FileNotFoundException {
        Document docu = new Document(PageSize.A4);
        docu.setMargins(20, 20, 20, 20);
        try {
            String format = "dd-MM-yy_HH-mm";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            java.util.Date date = new java.util.Date();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.setInitialFileName("Lecteurs_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph(" "));
                PdfPTable table = new PdfPTable(9);
                table.setWidthPercentage(100);
                addTableHeader(table);
                for (LecteurModel lecteur : LecteurList) {
                    addRowToTable(table, lecteur);
                }
                PdfPTable titleTable = new PdfPTable(2);
                titleTable.setWidthPercentage(100);
                PdfPCell imageCell = new PdfPCell(com.itextpdf.text.Image.getInstance("C:/Users/Rihab-Cherni/eclipse-workspace/LibraProject/src/images/libratech.PNG"));
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setPaddingBottom(20);
                PdfPCell titleCell = new PdfPCell();
                titleCell.addElement(new Paragraph("Le " + formater.format(date),
                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE)));
                titleCell.addElement(new Paragraph("Liste des lecteurs dans notre bibliothèque libratech:\n",
                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.BLUE)));
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setPaddingBottom(20);

                titleTable.addCell(imageCell);
                titleTable.addCell(titleCell);
                docu.add(titleTable);
                docu.add(new Paragraph(" "));
                docu.add(table);
                docu.close();
                Desktop.getDesktop().open(selectedFile);
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Cin", "Nom", "Prénom", "Email", "Genre", "Numéro téléphone", "Adresse", "Date de naissance","Date d'inscription"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    private void addRowToTable(PdfPTable table, LecteurModel lecteur) {
        table.addCell(String.valueOf(lecteur.getCin()));
        table.addCell(lecteur.getNom());
        table.addCell(lecteur.getPrenom());
        table.addCell(lecteur.getEmail());
        table.addCell(lecteur.getGenre());
        table.addCell(String.valueOf(lecteur.getPhone()));
        table.addCell(lecteur.getGenre());
        table.addCell(lecteur.getAdresse());
        table.addCell(String.valueOf(lecteur.getDate_Naissance()));
        table.addCell(String.valueOf(lecteur.getDate_Inscription()));
    }
}