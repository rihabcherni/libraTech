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

import Model.EmpruntModel;
import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.util.Callback;
public class ReturnController extends GlobalController implements Initializable {
	  	@FXML
	    private AnchorPane root;
	    @FXML
	    private TableColumn<EmpruntModel, Date> DateRetourPrevuColumn;    
	    @FXML
	    private TableColumn<EmpruntModel, Date> DateRetourReelColumn;
	    @FXML
	    private TableColumn<EmpruntModel, String> auteurColumn;
	    @FXML
	    private TableColumn<EmpruntModel, Integer> cinColumn;
	    @FXML
	    private TableColumn<EmpruntModel, String> nomColumn;
	    @FXML
	    private TableColumn<EmpruntModel, String> prenomColumn;
	    @FXML
	    private TableColumn<EmpruntModel, String> actionsColumn;
	    @FXML
	    private TableColumn<EmpruntModel, String> titreColumn;
	    @FXML
	    private TableColumn<EmpruntModel, Date> dateEmpruntColumn;
	    @FXML
	    private TableView<EmpruntModel> returnTableView;
	    @FXML
	    private TableColumn<EmpruntModel, Integer> idColumn;
	    @FXML
	    private TableColumn<EmpruntModel, Integer> idLivreColumn;
	    @FXML
	    private TextField rechercheReturn;
	    @FXML
	    private Button exportPdfReturn;
	    
	    @SuppressWarnings("exports")
		public EmpruntModel selectedReturn= null;
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet = null ;
	    EmpruntModel retour = null ;
		String query = null;
		Connection connection = null ;
	    ObservableList<EmpruntModel>  RetourList = FXCollections.observableArrayList();
	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
	        try {
				loadDate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    @FXML
	    private void rechercherReturn(KeyEvent event) {
	        String searchText = rechercheReturn.getText().toLowerCase().trim();
	        if (searchText.isEmpty()) {
	            refreshTable();
	        } else {
	            List<EmpruntModel> filteredList = RetourList.stream()
	                    .filter(r ->
	                            String.valueOf(r.getDetails_emprunts_id()).contains(searchText) ||
	                            String.valueOf(r.getLecteur_cin()).contains(searchText) ||
	                            String.valueOf(r.getNom()).contains(searchText) ||
	                            String.valueOf(r.getPrenom()).contains(searchText) ||
	                            String.valueOf(r.getLivre_id()).contains(searchText) ||
	                            String.valueOf(r.getTitre()).contains(searchText) ||
	                            String.valueOf(r.getAuteur()).contains(searchText) ||
	                            String.valueOf(r.getDate_Emprunt()).contains(searchText) ||
	                            String.valueOf(r.getDate_Retour_Prevu()).contains(searchText)||
                        		String.valueOf(r.getDate_retour_reel()).contains(searchText))
	                    .collect(Collectors.toList());
	            returnTableView.setItems(FXCollections.observableArrayList(filteredList));
	        }
	    }
	    @FXML void refreshTable() {
	        try {
	            RetourList.clear();
	            query = "SELECT d.details_emprunts_id, lecteur_cin, livre_id, d.Date_Emprunt, d.Date_Retour_Prevu, l.nom, l.prenom, li.titre, li.auteur, d.date_retour_reel " +
	                    "FROM details_emprunt d " +
	                    "JOIN lecteur l ON d.lecteur_cin = l.cin " +
	                    "JOIN livre li ON d.livre_id = li.code "+
	                    "WHERE d.date_retour_reel IS NOT NULL " +  
	                    "ORDER BY d.Date_Emprunt";  
	            preparedStatement = connection.prepareStatement(query);
	            resultSet = preparedStatement.executeQuery();
	            
	            while (resultSet.next()){
	                RetourList.add(new  EmpruntModel(
	                        resultSet.getInt("details_emprunts_id"),
	                        resultSet.getInt("lecteur_cin"),                       
	                        resultSet.getInt("livre_id"),
	                        resultSet.getDate("Date_Emprunt"),
	                        resultSet.getDate("Date_Retour_Prevu"),
	                        resultSet.getString("nom"),
	                        resultSet.getString("prenom"),
	                        resultSet.getString("titre"),
	                        resultSet.getString("auteur"),
	                        resultSet.getDate("date_retour_reel")
	                	)
	                );
	                returnTableView.setItems(RetourList);
	            }
	        } catch (SQLException ex) {
	            Logger.getLogger(ReturnController.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    private void loadDate() throws SQLException {
	        connection = ConnexionDb.getConnection();
	        refreshTable();
	        idColumn.setCellValueFactory(new PropertyValueFactory<>("details_emprunts_id"));
	        cinColumn.setCellValueFactory(new PropertyValueFactory<>("lecteur_cin"));
	        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
	        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
	        idLivreColumn.setCellValueFactory(new PropertyValueFactory<>("livre_id"));
	        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
	        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
	        dateEmpruntColumn.setCellValueFactory(new PropertyValueFactory<>("Date_Emprunt"));
	        DateRetourPrevuColumn.setCellValueFactory(new PropertyValueFactory<>("Date_Retour_Prevu"));
	        DateRetourReelColumn.setCellValueFactory(new PropertyValueFactory<>("date_retour_reel"));
	        Callback<TableColumn<EmpruntModel, String>, TableCell<EmpruntModel, String>> cellFoctory = (TableColumn<EmpruntModel, String> param) -> {
	            final TableCell<EmpruntModel, String> cell = new TableCell<EmpruntModel, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (empty) {
	                        setGraphic(null);
	                        setText(null);
	                    } else {
	                    	final Button pdfIcon = createButton("/images/pdf.png");
	                    	{
	                    		pdfIcon.setOnAction(event -> {
	      		                    EmpruntModel retour = (EmpruntModel) getTableView().getItems().get(getIndex());
	                                try {
										exportToPDFOneBook(retour);
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									}
	      		                });
	                    	}
	                        HBox managebtn = new HBox(pdfIcon);
	                        managebtn.setStyle("-fx-alignment:center");
	                        HBox.setMargin(pdfIcon, new Insets(2, 3, 0, 2));
	                        setGraphic(managebtn);
	                        setText(null);
	                    }
	                }
	            };
	            return cell;
	        };
	        actionsColumn.setCellFactory(cellFoctory);
	        returnTableView.setItems(RetourList);
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
	    private void exportToPDFOneBook(EmpruntModel retour) throws FileNotFoundException {
	        Document docu = new Document();

	        try {
	            String format = "dd-MM-yy_HH-mm";
	            SimpleDateFormat formater = new SimpleDateFormat(format);
	            java.util.Date date = new java.util.Date();

	            FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Enregistrer le fichier PDF");
	            fileChooser.setInitialFileName(retour.getDetails_emprunts_id() + "_" + formater.format(date) + ".pdf");
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
	            File selectedFile = fileChooser.showSaveDialog(new Stage());

	            if (selectedFile != null) {
	                String fileName = selectedFile.getAbsolutePath();
	                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
	                docu.open();
	                docu.add(new Paragraph("Informations sur les details retour:\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK)));
	                docu.add(new Paragraph("Numéro details retours: " + retour.getDetails_emprunts_id() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Numéro cin lecteur: " + retour.getLecteur_cin() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Nom lecteur: " + retour.getNom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Prénom lecteur: " + retour.getPrenom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Numéro livre: " + retour.getDetails_emprunts_id() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Titre livre: " + retour.getTitre() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Auteur livre: " + retour.getAuteur() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Date emprunt: " + formater.format(retour.getDate_Emprunt()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Date retour prevu: " + formater.format(retour.getDate_Retour_Prevu()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
	                docu.add(new Paragraph("Date retour réel: " + formater.format(retour.getDate_retour_reel()) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
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
	            fileChooser.setInitialFileName("Emprunt_" + formater.format(date) + ".pdf");
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
	            File selectedFile = fileChooser.showSaveDialog(new Stage());
	            if (selectedFile != null) {
	                String fileName = selectedFile.getAbsolutePath();
	                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
	                docu.open();
	                docu.add(new Paragraph(" "));
	                PdfPTable table = new PdfPTable(10);
	                table.setWidthPercentage(100);
	                addTableHeader(table);
	                for (EmpruntModel retour : RetourList) {
	                    addRowToTable(table, retour);
	                }
	                PdfPTable titleTable = new PdfPTable(2);
	                titleTable.setWidthPercentage(100);
	                PdfPCell imageCell = new PdfPCell(com.itextpdf.text.Image.getInstance("C:/Users/Rihab-Cherni/eclipse-workspace/LibraProject/src/images/libratech.PNG"));
	                imageCell.setBorder(Rectangle.NO_BORDER);
	                imageCell.setPaddingBottom(20);
	                PdfPCell titleCell = new PdfPCell();
	                titleCell.addElement(new Paragraph("Le " + formater.format(date),
	                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE)));
	                titleCell.addElement(new Paragraph("Liste des détails emprunts dans notre bibliothèque libratech:\n",
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
	        String[] headers = {"Numéro details retour", "Numéro cin lecteur","nom","prénom", "Numéro livre","Titre","Auteur", "Date emprunt", "Date retour prevu", "Date de retour réel"};
	        for (String header : headers) {
	            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	        }
	    }
	    private void addRowToTable(PdfPTable table, EmpruntModel retour) {
	        table.addCell(String.valueOf(retour.getDetails_emprunts_id()));
	        table.addCell(String.valueOf(retour.getLecteur_cin()));
	        table.addCell(String.valueOf(retour.getNom()));
	        table.addCell(String.valueOf(retour.getPrenom()));
	        table.addCell(String.valueOf(retour.getLivre_id()));
	        table.addCell(String.valueOf(retour.getAuteur()));
	        table.addCell(String.valueOf(retour.getTitre()));
	        table.addCell(String.valueOf(retour.getDate_Emprunt()));
	        table.addCell(String.valueOf(retour.getDate_Retour_Prevu()));
	        table.addCell(String.valueOf(retour.getDate_retour_reel()));
	    }
}