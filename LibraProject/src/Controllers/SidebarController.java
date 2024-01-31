package Controllers;

import java.io.IOException;

import application.Links;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SidebarController extends Application {
    @FXML
    private HBox About;
    @FXML
    private HBox Dashboard;
    @FXML
    private HBox Employes;
    @FXML
    private ImageView aboutImg;
    @FXML
    private ImageView books11;
    @FXML
    private ImageView dashImg;
    @FXML
    private ImageView employeImg;
    @FXML
    private ImageView empruntImg;
    @FXML
    private HBox empruntLivre;
    @FXML
    private HBox etagere;
    @FXML
    private ImageView etagereImg;
    @FXML
    private ImageView lecteurImg;
    @FXML
    private HBox lecteurs;
    @FXML
    Text linkPage;
    @FXML
    private ImageView livreImg;
    @FXML
    private HBox livres;
    @FXML
    private HBox logout;
    @FXML
    private ImageView logoutImg;
    @FXML
    AnchorPane page;
    @FXML
    private ImageView retiurnImg;
    @FXML
    private HBox retorunerLivre;
    @FXML
    private AnchorPane root;
    @FXML
    private Text username;

    public void setNomPrenom(String nom, String prenom) {
        username.setText(nom + " " + prenom);
    }

    @SuppressWarnings("exports")
    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof HBox) {
            HBox link = (HBox) event.getSource();
            Parent root = null;
            resetLinkStyles();

            switch (link.getId()) {
	            case "Dashboard":
	                root = FXMLLoader.load(this.getClass().getResource(Links.dashboardLink));
	                linkPage.setText("Dashboard");
	                Dashboard.getStyleClass().add("activeLink");
	                break;
	            case "lecteurs":
	                root = FXMLLoader.load(this.getClass().getResource(Links.LecteurLink));
	                linkPage.setText("Lecteurs");
	                lecteurs.getStyleClass().add("activeLink");
	                break;
	            case "livres":
	                root = FXMLLoader.load(this.getClass().getResource(Links.LivreLink));
	                linkPage.setText("Livres");
	                livres.getStyleClass().add("activeLink");
	                break;
	            case "empruntLivre":
	                root = FXMLLoader.load(this.getClass().getResource(Links.empruntLink));
	                linkPage.setText("Emprunter Livre");
	                empruntLivre.getStyleClass().add("activeLink");
	                break;
	            case "retorunerLivre":
	                root = FXMLLoader.load(this.getClass().getResource(Links.returnLink));
	                linkPage.setText("Retourner Livre");
	                retorunerLivre.getStyleClass().add("activeLink");
	                break;
	            case "About":
	                root = FXMLLoader.load(this.getClass().getResource(Links.aboutLink));
	                linkPage.setText("About");
	                About.getStyleClass().add("activeLink");
	                break;
	            case "Employes":
	                root = FXMLLoader.load(this.getClass().getResource(Links.employeLink));
	                linkPage.setText("Employes");
	                Employes.getStyleClass().add("activeLink");
	                break;
	            case "etagere":
	                root = FXMLLoader.load(this.getClass().getResource(Links.etagereLink));
	                linkPage.setText("Etagere");
	                etagere.getStyleClass().add("activeLink");
	                break;
	            case "logout":
	            	logout(event);
	                break;
	        }
            if (root != null) {
                page.getChildren().setAll(root);

            }
        }
    }
    @SuppressWarnings("exports")
	public void logout(MouseEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Links.loginLink));
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("/images/libratech.PNG"));
        newStage.setResizable(false);
        newStage.getIcons().add(icon);
        newStage.setTitle("LibraTech-Login");
        newStage.setScene(scene);
        newStage.show();
        currentStage.close();
    }
    private void resetLinkStyles() {
        Dashboard.getStyleClass().remove("activeLink");
        lecteurs.getStyleClass().remove("activeLink");
        livres.getStyleClass().remove("activeLink");
        empruntLivre.getStyleClass().remove("activeLink");
        retorunerLivre.getStyleClass().remove("activeLink");
        About.getStyleClass().remove("activeLink");
        Employes.getStyleClass().remove("activeLink");
        etagere.getStyleClass().remove("activeLink");
    }

    @SuppressWarnings("exports")
    public void playMouseEnterAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);

            DropShadow glow = new DropShadow();
            glow.setColor(Color.RED);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    @SuppressWarnings("exports")
    public void playMouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.YELLOW);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
            scaleT.setToX(1);
            scaleT.setToY(1);
        }
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub

    }
}
