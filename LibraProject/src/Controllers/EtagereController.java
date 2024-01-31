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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import Model.EtagereModel;
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
public class EtagereController extends GlobalController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<EtagereModel> etagereTableView;
    @FXML
    private TableColumn<EtagereModel, String> NomEtageColumn;
    @FXML
    private TableColumn<EtagereModel, Integer> capaciteColumn;
    @FXML
    private TableColumn<EtagereModel, Integer> etagereIdColumn;
    @FXML
    private TableColumn<EtagereModel, Integer> quantiteInstantaneeColumn;
    @FXML
    private TableColumn<EtagereModel, String> actionsColumn;
    @FXML
    private Button ajouterEtagere;
    @FXML
    private Button exportPdfEtagere;
    @FXML
    private TextField rechercheEtagere;
    @FXML
    private Button rechercheEtagereBtn;
    @SuppressWarnings("exports")
	public EtagereModel selectedEtagere= null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    EtagereModel etagere = null ;
	String query = null;
	Connection connection = null ;
    ObservableList<EtagereModel>  EtagereList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
			loadDate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    @FXML
    void openAddEtagere(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.crudEtageLink));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Crud Etagere");

        CrudEtagereController crudEtagereController = loader.getController();
        crudEtagereController.setEtagereController(this);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        newStage.setResizable(false);
        newStage.getIcons().add(icon);
        newStage.show();
    }
    @FXML
    private void rechercherEtagere(KeyEvent event) {
        String searchText = rechercheEtagere.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            refreshTable();
        } else {
            List<EtagereModel> filteredList = EtagereList.stream()
                    .filter(etagere ->
                    		etagere.getNom_Etage().toLowerCase().contains(searchText) ||
                            String.valueOf(etagere.getCapacite()).contains(searchText) ||
                            String.valueOf(etagere.getQuantite_instantanee()).contains(searchText) ||
                            String.valueOf(etagere.getEtagere_id()).contains(searchText))
                    .collect(Collectors.toList());
            etagereTableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML void refreshTable() {
        try {
            EtagereList.clear();
            query = "SELECT * FROM `etagere`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                EtagereList.add(new  EtagereModel(
                        resultSet.getInt("etagere_id"),
                        resultSet.getString("Nom_Etage"),
                        resultSet.getInt("capacite"),
                        resultSet.getInt("quantite_instantanee"))
                	);
                etagereTableView.setItems(EtagereList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EtagereController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void loadDate() throws SQLException {
        connection = ConnexionDb.getConnection();
        refreshTable();
        etagereIdColumn.setCellValueFactory(new PropertyValueFactory<>("etagere_id"));
        NomEtageColumn.setCellValueFactory(new PropertyValueFactory<>("Nom_Etage"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        quantiteInstantaneeColumn.setCellValueFactory(new PropertyValueFactory<>("quantite_instantanee"));
                
        Callback<TableColumn<EtagereModel, String>, TableCell<EtagereModel, String>> cellFoctory = (TableColumn<EtagereModel, String> param) -> {
            final TableCell<EtagereModel, String> cell = new TableCell<EtagereModel, String>() {
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
      		                    EtagereModel etagere = (EtagereModel) getTableView().getItems().get(getIndex());
      		                    supprimerEtagere(etagere);
      		                });
      		            }
                    	final Button editIcon = createButton("/images/update.png");
                    	{
                    		editIcon.setOnAction(event -> {
      		                  EtagereModel etagere = (EtagereModel) getTableView().getItems().get(getIndex());
                              FXMLLoader loader = new FXMLLoader ();
                              loader.setLocation(getClass().getResource(Links.crudEtageLink));
                              try {
                                  loader.load();
                              } catch (IOException ex) {
                                  Logger.getLogger(EtagereController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              CrudEtagereController crudEtagereController = loader.getController();
                              crudEtagereController.setUpdate(true);
                              crudEtagereController.setTextField(etagere.getEtagere_id(), etagere.getNom_Etage(), (int)etagere.getCapacite());
                              crudEtagereController.titleEtagere.setText("Modifier Etagere");
                              crudEtagereController.ajouterEtage.setText("Modifier");
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
      		                    EtagereModel etagere = (EtagereModel) getTableView().getItems().get(getIndex());
                                try {
									exportToPDFOneBook(etagere);
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
        etagereTableView.setItems(EtagereList);
    }
    private void supprimerEtagere(EtagereModel etagere) {
        String deleteLivresQuery = "DELETE FROM livre WHERE etagere_id = ?";
        String deleteEtagereQuery = "DELETE FROM etagere WHERE etagere_id = ?";

        try (Connection connection = ConnexionDb.getConnection();
             PreparedStatement deleteLivresStmt = connection.prepareStatement(deleteLivresQuery);
             PreparedStatement deleteEtagereStmt = connection.prepareStatement(deleteEtagereQuery)) {
            deleteLivresStmt.setLong(1, etagere.getEtagere_id());
            deleteLivresStmt.executeUpdate();
            deleteEtagereStmt.setLong(1, etagere.getEtagere_id());
            deleteEtagereStmt.executeUpdate();

            afficherAlerte("Succès", "Etagere supprimé avec succès avec tous les livres.", AlertType.INFORMATION);
            refreshTable();

        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Erreur SQL lors de la suppression d'etagere.", AlertType.ERROR);
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
    private void exportToPDFOneBook(EtagereModel etagere) throws FileNotFoundException {
        Document docu = new Document();

        try {
            String format = "dd-MM-yy_HH-mm";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            java.util.Date date = new java.util.Date();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.setInitialFileName(etagere.getNom_Etage() + "_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph("Informations sur le etagere:\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK)));
                docu.add(new Paragraph("Numero: " + etagere.getEtagere_id() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Nom_Etage: " + etagere.getNom_Etage() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("capacite: " + etagere.getCapacite() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("quantite_instantanee: " + etagere.getQuantite_instantanee() + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
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
            fileChooser.setInitialFileName("Etageres_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph(" "));
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                addTableHeader(table);
                for (EtagereModel e : EtagereList) {
                    addRowToTable(table, e);
                }
                PdfPTable titleTable = new PdfPTable(2);
                titleTable.setWidthPercentage(100);
                PdfPCell imageCell = new PdfPCell(com.itextpdf.text.Image.getInstance("C:/Users/Rihab-Cherni/eclipse-workspace/LibraProject/src/images/libratech.PNG"));
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setPaddingBottom(20);
                PdfPCell titleCell = new PdfPCell();
                titleCell.addElement(new Paragraph("Le " + formater.format(date),
                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE)));
                titleCell.addElement(new Paragraph("Liste des etageres dans notre bibliothèque libratech:\n",
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
        String[] headers = {"Numéro étagère", "Nom etagere", "Capacité", "Quantité instantannée"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    private void addRowToTable(PdfPTable table, EtagereModel etagere) {
        table.addCell(String.valueOf(etagere.getEtagere_id()));
        table.addCell(etagere.getNom_Etage());
        table.addCell(String.valueOf(etagere.getCapacite()));
        table.addCell(String.valueOf(etagere.getQuantite_instantanee()));
    }
}