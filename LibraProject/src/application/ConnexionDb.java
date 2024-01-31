package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ConnexionDb {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/libratech";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
    	 try {
              Class.forName("com.mysql.cj.jdbc.Driver");
              return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    	  } catch (ClassNotFoundException | SQLException e) {
              e.printStackTrace();
              afficherAlerte("Problème connexion à la base de données");
      		  return null;
          }
    }
    private static void afficherAlerte(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}