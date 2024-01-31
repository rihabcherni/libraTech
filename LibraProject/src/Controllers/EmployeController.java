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

import Model.EmployeModel;
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
public class EmployeController extends GlobalController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private TableColumn<EmployeModel, String> actionsColumn;

    @FXML
    private Button ajouterEmploye;

    @FXML
    private TableColumn<EmployeModel, String> emailColumn;

    @FXML
    private TableColumn<EmployeModel, Integer> employeIdColumn;

    @FXML
    private TableView<EmployeModel> employesTableView;

    @FXML
    private Button exportPdfEmploye;

    @FXML
    private TableColumn<EmployeModel, String> nomColumn;

    @FXML
    private TableColumn<EmployeModel, Integer> phoneColumn;

    @FXML
    private TableColumn<EmployeModel, String> posteColumn;

    @FXML
    private TableColumn<EmployeModel, String> prenomColumn;

    @FXML
    private TextField rechercheEmploye;
    
    @SuppressWarnings("exports")
	public EmployeModel selectedEmploye= null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    EmployeModel employe = null ;
	String query = null;
	Connection connection = null ;

    ObservableList<EmployeModel>  EmployeList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
			loadDate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    @FXML
    void openAddEmploye(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Links.crudEmployeLink));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setTitle("Crud employe");

        CrudEmployeController crudEmployeController = loader.getController();
        crudEmployeController.setEmployeController(this);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        newStage.setResizable(false);
        newStage.getIcons().add(icon);
        newStage.show();
    }
    @FXML
    private void rechercherEmploye(KeyEvent event) {
        String searchText = rechercheEmploye.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            refreshTable();
        } else {
            List<EmployeModel> filteredList = EmployeList.stream()
                    .filter(employe ->
                    		employe.getNom().toLowerCase().contains(searchText) ||
                    		employe.getPrenom().toLowerCase().contains(searchText)||
                    		employe.getPhone().toLowerCase().contains(searchText)||
                    		employe.getEmail().toLowerCase().contains(searchText)||
                            employe.getPoste().contains(searchText) ||
                            String.valueOf(employe.getEmploye_id()).contains(searchText))
                    .collect(Collectors.toList());
            employesTableView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML void refreshTable() {
        try {
            EmployeList.clear();
            query = "SELECT * FROM `employe`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                EmployeList.add(new  EmployeModel(
                        resultSet.getInt("employe_id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("poste"),
                        resultSet.getString("mot_de_passe"))
                	);
                employesTableView.setItems(EmployeList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void loadDate() throws SQLException {
        connection = ConnexionDb.getConnection();
        refreshTable();
        employeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employe_id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        Callback<TableColumn<EmployeModel, String>, TableCell<EmployeModel, String>> cellFoctory = (TableColumn<EmployeModel, String> param) -> {
            final TableCell<EmployeModel, String> cell = new TableCell<EmployeModel, String>() {
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
      		                    EmployeModel employe = (EmployeModel) getTableView().getItems().get(getIndex());
      		                    supprimerEmploye(employe);
      		                });
      		            }
                    	final Button editIcon = createButton("/images/update.png");
                    	{
                    		editIcon.setOnAction(event -> {
      		                  EmployeModel employe = (EmployeModel) getTableView().getItems().get(getIndex());
                              FXMLLoader loader = new FXMLLoader ();
                              loader.setLocation(getClass().getResource(Links.crudEmployeLink));
                              try {
                                  loader.load();
                              } catch (IOException ex) {
                                  Logger.getLogger(EmployeController.class.getName()).log(Level.SEVERE, null, ex);
                              }

                              CrudEmployeController crudEmployeController = loader.getController();
                              crudEmployeController.setUpdate(true);
                              crudEmployeController.setTextField(employe.getEmploye_id(), employe.getNom(), employe.getPrenom(),employe.getEmail(), employe.getPhone(), employe.getPoste(), employe.getMot_de_passe());
                              crudEmployeController.titleEmploye.setText("Modifier employe");
                              crudEmployeController.ajouterEmploye.setText("Modifier");
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
      		                    EmployeModel employe = (EmployeModel) getTableView().getItems().get(getIndex());
                                try {
									exportToPDFOneBook(employe);
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
        employesTableView.setItems(EmployeList);
    }
    private void supprimerEmploye(EmployeModel employe) {
	    String query = "DELETE FROM employe WHERE employe_id = ?";
	    try (Connection connection = ConnexionDb.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setLong(1, employe.getEmploye_id());
	        pstmt.executeUpdate();
	        afficherAlerte("Succès", "Employe supprimé avec succès.", AlertType.INFORMATION);
            refreshTable();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        afficherAlerte("Erreur", "Erreur SQL lors de la suppression du employe.", AlertType.ERROR);
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
    private void exportToPDFOneBook(EmployeModel employe) throws FileNotFoundException {
        Document docu = new Document();

        try {
            String format = "dd-MM-yy_HH-mm";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            java.util.Date date = new java.util.Date();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.setInitialFileName(employe.getNom() + "_" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph("Informations sur le employe:\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK)));
                docu.add(new Paragraph("Numero employe: " + employe.getEmploye_id() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Nom: " + employe.getNom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Prénom: " + employe.getPrenom() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Email: " + employe.getEmail() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Phone: " + employe.getPhone() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
                docu.add(new Paragraph("Poste: " + employe.getPoste() + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK)));
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
            fileChooser.setInitialFileName("Employes-" + formater.format(date) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            if (selectedFile != null) {
                String fileName = selectedFile.getAbsolutePath();
                PdfWriter.getInstance(docu, new FileOutputStream(fileName));
                docu.open();
                docu.add(new Paragraph(" "));
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                addTableHeader(table);
                for (EmployeModel employe : EmployeList) {
                    addRowToTable(table, employe);
                }
                PdfPTable titleTable = new PdfPTable(2);
                titleTable.setWidthPercentage(100);
                PdfPCell imageCell = new PdfPCell(com.itextpdf.text.Image.getInstance("C:/Users/Rihab-Cherni/eclipse-workspace/LibraProject/src/images/libratech.PNG"));
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setPaddingBottom(20);
                PdfPCell titleCell = new PdfPCell();
                titleCell.addElement(new Paragraph("Le " + formater.format(date),
                        FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE)));
                titleCell.addElement(new Paragraph("Liste des employes dans notre bibliothèque libratech:\n",
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
        String[] headers = {"Numéro employe", "Nom", "Prénom", "Email", "Phone", "Poste"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    private void addRowToTable(PdfPTable table, EmployeModel employe) {
        table.addCell(String.valueOf(employe.getEmploye_id()));
        table.addCell(employe.getNom());
        table.addCell(employe.getPrenom());
        table.addCell(employe.getEmail());
        table.addCell(employe.getPhone());
        table.addCell(String.valueOf(employe.getPoste()));
    }
}