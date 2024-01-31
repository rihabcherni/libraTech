module LibraProject {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires itextpdf;
	requires java.desktop;
    exports Controllers;
    opens Controllers to javafx.fxml;
    opens Model to javafx.base;

	opens application to javafx.graphics, javafx.fxml;
}
