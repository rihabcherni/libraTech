package Controllers;

import application.ConnexionDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label nbEmploye;
    @FXML
    private Label nbEmprunt;
    @FXML
    private Label nbLecteur;
    @FXML
    private Label nbLivre;
    @FXML
    private Label nbRetourn;
    @FXML
    private AnchorPane root;
    @FXML
    private PieChart categoryPieChart;
    @FXML
    private Pane barChartPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCartCount(nbLivre, "SELECT COUNT(*) FROM livre");
        setCartCount(nbLecteur, "SELECT COUNT(*) FROM lecteur");
        setCartCount(nbEmploye, "SELECT COUNT(*) FROM employe");
        setCartCount(nbEmprunt, "SELECT COUNT(*) FROM details_emprunt WHERE date_retour_reel IS NULL");
        setCartCount(nbRetourn, "SELECT COUNT(*) FROM details_emprunt WHERE date_retour_reel IS NOT NULL");

        ObservableList<PieChart.Data> pieChartData = getBookCategoryData();
        categoryPieChart.setData(pieChartData);
        categoryPieChart.setMinSize(400, 300);
        categoryPieChart.setTitle("Nombre de livres par catégorie");
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(1);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        ObservableList<XYChart.Series<String, Number>> barChartData = getBarChartData();
        barChart.getData().addAll(barChartData);
        barChart.setMinSize(400, 300);
        barChart.setTitle("Nombre des livres empruntés et retournés au fil du temps");

        barChartPane.getChildren().add(barChart);
    }

    private ObservableList<XYChart.Series<String, Number>> getBarChartData() {

        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Livres empruntés");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Livres retournés");

        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT Date_Emprunt, COUNT(*) as count FROM details_emprunt where date_retour_reel is null GROUP BY Date_Emprunt";
            String query2 = "SELECT date_retour_reel, COUNT(*) as count FROM details_emprunt where date_retour_reel is not null GROUP BY date_retour_reel";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("Date_Emprunt");
                    int count = resultSet.getInt("count");
                    series.getData().add(new XYChart.Data<>(date, count));
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query2);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("date_retour_reel");
                    int count = resultSet.getInt("count");
                    series2.getData().add(new XYChart.Data<>(date, count));
                }
            }
            barChartData.add(series);
            barChartData.add(series2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barChartData;
    }

    private ObservableList<PieChart.Data> getBookCategoryData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try (Connection connection = ConnexionDb.getConnection()) {
            String query = "SELECT categorie, COUNT(*) as count FROM livre GROUP BY categorie";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String category = resultSet.getString("categorie");
                    int count = resultSet.getInt("count");
                    pieChartData.add(new PieChart.Data(category, count));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pieChartData;
    }

    private void setCartCount(Label label, String query) {
        try (Connection connection = ConnexionDb.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                label.setText(String.valueOf(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
