package advancejavaproject4;

import advancejavaproject4.database.DatabaseConnection;
import advancejavaproject4.util.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

/**
 * Controller for custom SQL query window
 * @author yigitt
 */
public class QueryViewController {

    @FXML private TextArea queryTextArea;
    @FXML private Label resultLabel;
    @FXML private TableView<ObservableList<String>> resultTableView;

    @FXML
    private void handleExecute() {
        String query = queryTextArea.getText().trim();

        if (query.isEmpty()) {
            AlertHelper.showWarning("Empty Query", "Please enter a SQL query.");
            return;
        }

        try {
            // Determine query type: SELECT or DML (INSERT/UPDATE/DELETE)
            if (query.toUpperCase().startsWith("SELECT")) {
                executeSelectQuery(query);
            } else {
                executeUpdateQuery(query);
            }
        } catch (SQLException e) {
            AlertHelper.showError("SQL Error", "Query execution failed:\n" + e.getMessage());
        }
    }

    /**
     * Executes SELECT queries and displays results in TableView
     */
    private void executeSelectQuery(String query) throws SQLException {
        resultTableView.getColumns().clear();
        resultTableView.getItems().clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Build columns dynamically from ResultSet metadata
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create columns
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i - 1;
                String columnName = metaData.getColumnName(i);

                TableColumn<ObservableList<String>, String> column =
                        new TableColumn<>(columnName);

                column.setCellValueFactory(param -> {
                    ObservableList<String> row = param.getValue();
                    if (row != null && row.size() > columnIndex) {
                        return new SimpleStringProperty(row.get(columnIndex));
                    }
                    return new SimpleStringProperty("");
                });

                resultTableView.getColumns().add(column);
            }

            // Populate data
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    row.add(value != null ? value : "NULL");
                }
                data.add(row);
            }

            resultTableView.setItems(data);
            resultLabel.setText("Query executed successfully. Rows returned: " + data.size());
            resultLabel.setStyle("-fx-text-fill: green;");

        } catch (SQLException e) {
            resultLabel.setText("Query failed: " + e.getMessage());
            resultLabel.setStyle("-fx-text-fill: red;");
            throw e;
        }
    }

    /**
     * Executes DML queries (INSERT, UPDATE, DELETE) and displays rows affected
     */
    private void executeUpdateQuery(String query) throws SQLException {
        resultTableView.getColumns().clear();
        resultTableView.getItems().clear();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query);

            resultLabel.setText("Query executed successfully. Rows affected: " + rowsAffected);
            resultLabel.setStyle("-fx-text-fill: green;");

            AlertHelper.showInfo("Success", "Query executed. " + rowsAffected + " row(s) affected.");

        } catch (SQLException e) {
            resultLabel.setText("Query failed: " + e.getMessage());
            resultLabel.setStyle("-fx-text-fill: red;");
            throw e;
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) queryTextArea.getScene().getWindow();
        stage.close();
    }
}
