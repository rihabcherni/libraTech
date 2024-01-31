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

import Model.LivreModel;
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
public class LivreController extends GlobalController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TableColumn<LivreModel, String> titreColumn;
    @FXML
    private TableColumn<LivreModel, String> actionsColumn;
    @FXML
    private TableColumn<LivreModel, String> auteurColumn;
    @FXML
    private TableColumn<LivreModel, String> categorieColumn;
    @FXML
    private TableColumn<LivreModel, Boolean> categorieColumn1;
    @FXML
    private TableColumn<LivreModel, Long> codeISBNColumn;
    @FXML
    private TableColumn<LivreModel, Integer> codeLivreColumn;
    @FXML
    private TableColumn<LivreModel, String> etagereColumn;
    @FXML
    private TableColumn<LivreModel, Date> Date_ajoutColumn;
    @FXML
    private TableColumn<LivreModel, String> disponibleColumn;
    @FXML
    private TableView<LivreModel> livresTableView;
    @FXML
    private Button ajouterLivre;
    @FXML
    private Button actLivre;
    @FXML
    private Button exportPdfLivre;
    @FXML
    private TextField rechercheLivre;
    @FXML
    private Button rechercheLivreBtn;
    @SuppressWarnings("exports")
	public LivreModel selectedLivre= null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    LivreModel livre = null ;
	String query = null;
	Connection connection = null ;
    ObservableList<LivreModel>  LivreList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
			loadDate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	 @FXML 
	private void actLivre(MouseEvent event){
		 refreshTable();
	}
    @FXML
    void openAddLivre(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.crudLivreLink));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Crud Livre");

        CrudLivreController crudLivreController = loader.getController();
        crudLivreController.setLivreController(this);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        newStage.setResizable(false);
        newStage.getIcons().add(icon);
        newStage.show();
    }
    @FXML
    private void rechercherLivre(KeyEvent event) {
        String searchText = rechercheLivre.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            refreshTable();
        } else {
            List<LivreModel> filteredList = LivreList.stream()
                    .filter(livre ->
                            livre.getTitre().toLowerCase().contains(searchText) ||
                            livre.getAuteur().toLowerCase().contains(searchText)||
                            livre.getCategorie().toLowerCase().contains(searchText)||
                            String.valueOf(livre.getCodeISBN()).contains(searchText) ||
                            String.valueOf(livre.getDisponibilite()).contains(searchText) ||
                            String.valueOf(livre.getEtagere_id()).contains(searchText) ||
                            String.valueOf(livre.getDate_ajout()).contains(searchText))
                    .collect(Collectors.toList());
            livresTableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML void refreshTable() {
        try {
            LivreList.clear();
            query = "SELECT * FROM `livre`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LivreList.add(new  LivreModel(
                        resultSet.getInt("code"),
                        resultSet.getInt("etagere_id"),
                        resultSet.getString("titre"),
                        resultSet.getString("auteur"),
                        resultSet.getString("categorie"),
                        resultSet.getInt("codeISBN"),
                        resultSet.getInt("Disponibilite"),
                        resultSet.getDate("Date_ajout"))
                	);
                livresTableView.setItems(LivreList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LivreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void loadDate() throws SQLException {
        connection = ConnexionDb.getConnection();
        refreshTable();
        codeLivreColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        codeISBNColumn.setCellValueFactory(new PropertyValueFactory<>("codeISBN"));
        etagereColumn.setCellValueFactory(new PropertyValueFactory<>("etagere_id"));
        Date_ajoutColumn.setCellValueFactory(new PropertyValueFactory<>("Date_ajout"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("Disponibilite"));

        Callback<TableColumn<LivreModel, String>, TableCell<LivreModel, String>> cellFoctory = (TableColumn<LivreModel, String> param) -> {
            final TableCell<LivreModel, String> cell = new TableCell<LivreModel, String>() {
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
      		                    LivreModel livre = (LivreModel) getTableView().getItems().get(getIndex());
      		                    supprimerLivre(livre);
      		                });
      		            }
                    	final Button editIcon = createButton("/images/update.png");
                    	{
                    		editIcon.setOnAction(event -> {
      		                  LivreModel livre = (LivreModel) getTableView().getItems().get(getIndex());
                              FXMLLoader loader = new FXMLLoader ();
                              loader.setLocation(getClass().getResource(Links.crudLivreLink));
                              try {
                                  loader.load();
                              } catch (IOException ex) {
                                  Logger.getLogger(LivreController.class.getName()).log(Level.SEVERE, null, ex);
                              }

                              CrudLivreController crudLivreController = loader.getController();
                              crudLivreController.setUpdate(true);
                              crudLivreController.setTextField(livre.getCode(), livre.getTitre(), livre.getAuteur(),livre.getCategorie(),(int)livre.getEtagere_id(), (int)livre.getCodeISBN());
                              crudLivreController.titleLivre.setText("Modifier Livre");
                              crudLivreController.ajouterLivre.setText("Modifier");
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
      		                    LivreModel livre = (LivreModel) getTableView().getItems().get(getIndex());
                                try {
									exportToPDFOneBook(livre);
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
        livresTableView.setItems(LivreList);
    }
    private void supprimerLivre(LivreModel livre) {
	    String query = "DELETE FROM livre WHERE code = ?";
	    try (Connection connection = ConnexionDb.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setLong(1, livre.getCode());
	        pstmt.executeUpdate();
	        afficherSuccess("Livre supprimé avec succès.");
            refreshTable();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        afficherAlerte("Erreur", "Erreur SQL lors de la suppression du livre.", AlertType.ERROR);
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
    private void exportToPDFOneBook(LivreModel livre) throws FileNotFoundException {
        Document docu = new Document();

        try {
            String format = "dd-MM-yy_HH-mm";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            java.util.Date date = new java.util.Date();

            // Show a file save dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.setInitialFileName(livre.getTitre() + "_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph("Informations sur le livre:\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK)));
                docu.add(new Paragraph("Titre: " + livre.getTitre() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Auteur: " + livre.getAuteur() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Catégorie: " + livre.getCategorie() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Code ISBN: " + livre.getCodeISBN() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Etagère ID: " + livre.getEtagere_id() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Date d'ajout: " + formater.format(livre.getDate_ajout()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Disponibilité: " + livre.getDisponibilite() + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
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
            fileChooser.setInitialFileName("Livres_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph(" "));
                PdfPTable table = new PdfPTable(8);
                table.setWidthPercentage(100);
                addTableHeader(table);
                for (LivreModel livre : LivreList) {
                    addRowToTable(table, livre);
                }
                PdfPTable titleTable = new PdfPTable(2);
                titleTable.setWidthPercentage(100);
                PdfPCell imageCell = new PdfPCell(com.itextpdf.text.Image.getInstance("C:/Users/Rihab-Cherni/eclipse-workspace/LibraProject/src/images/libratech.PNG"));
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setPaddingBottom(20);
                PdfPCell titleCell = new PdfPCell();
                titleCell.addElement(new Paragraph("Le " + formater.format(date),
                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE)));
                titleCell.addElement(new Paragraph("Liste des livres dans notre bibliothèque libratech:\n",
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
        String[] headers = {"Code", "Numéro étagère", "Titre", "Auteur", "Catégorie", "Code ISBN", "Disponibilité", "Date Ajout"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    private void addRowToTable(PdfPTable table, LivreModel livre) {
    	String disponible= livre.getDisponibilite()== 1?"Disponible":"Non disponible";
        table.addCell(String.valueOf(livre.getCode()));
        table.addCell(String.valueOf(livre.getEtagere_id()));
        table.addCell(livre.getTitre());
        table.addCell(livre.getAuteur());
        table.addCell(livre.getCategorie());
        table.addCell(String.valueOf(livre.getCodeISBN()));
        table.addCell(disponible);
        table.addCell(String.valueOf(livre.getDate_ajout()));
    }
}