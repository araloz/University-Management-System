package advancejavaproject4;

import advancejavaproject4.database.*;
import advancejavaproject4.model.*;
import advancejavaproject4.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Main controller for Student Records Management System
 * Handles all CRUD operations and UI interactions
 * @author yigitt
 */
public class MainViewController {

    // FXML Injected components
    @FXML private Label statusLabel;
    @FXML private Button connectButton;
    @FXML private ListView<String> tableList;
    @FXML private TableView tableView;
    @FXML private Button displayButton;
    @FXML private Button queryButton;
    @FXML private TextField field1, field2, field3;

    // DAO objects
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    // Current selected table
    private String currentTable = "";

    /**
     * Initializes the controller
     */
    @FXML
    private void initialize() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();

        // Add listener to table selection
        tableList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onTableSelectionChanged(newValue)
        );

        // Add listener to TableView selection for populating form
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateFormFromSelection()
        );
    }

    /**
     * Handles database connection
     */
    @FXML
    private void handleConnect() {
        try {
            DatabaseConnection dbConn = DatabaseConnection.getInstance();

            if (dbConn.testConnection()) {
                statusLabel.setText("Connected to database: " + dbConn.getDatabaseName());
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                connectButton.setDisable(true);

                // Populate table list
                populateTableList();

                AlertHelper.showInfo("Success", "Successfully connected to database!");
            } else {
                throw new SQLException("Connection test failed");
            }
        } catch (Exception e) {
            statusLabel.setText("Connection failed: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            AlertHelper.showError("Connection Error",
                    "Failed to connect to database:\n" + e.getMessage() +
                    "\n\nPlease ensure:\n" +
                    "1. XAMPP is running\n" +
                    "2. MySQL service is started\n" +
                    "3. Database 'student_records_db' exists\n" +
                    "4. MySQL Connector JAR is added to project");
        }
    }

    /**
     * Populates the table list view with database table names
     */
    private void populateTableList() {
        try {
            ObservableList<String> tables = FXCollections.observableArrayList(
                    DatabaseConnection.getInstance().getTableNames()
            );
            tableList.setItems(tables);
        } catch (SQLException e) {
            AlertHelper.showError("Error", "Failed to load table names:\n" + e.getMessage());
        }
    }

    /**
     * Handles table selection changes
     */
    private void onTableSelectionChanged(String newTable) {
        if (newTable != null) {
            currentTable = newTable;
            clearForm();
            updateFieldLabels();
        }
    }

    /**
     * Handles table selection event
     */
    @FXML
    private void handleTableSelected() {
        // This is handled by the listener in initialize()
    }

    /**
     * Displays contents of selected table
     */
    @FXML
    private void handleDisplayContents() {
        String selectedTable = tableList.getSelectionModel().getSelectedItem();

        if (selectedTable == null) {
            AlertHelper.showWarning("No Selection", "Please select a table from the list.");
            return;
        }

        try {
            switch (selectedTable) {
                case "students":
                    displayStudents();
                    break;
                case "courses":
                    displayCourses();
                    break;
                case "enrollments":
                    displayEnrollments();
                    break;
                default:
                    AlertHelper.showInfo("Info", "Table '" + selectedTable + "' is not supported for display.");
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display table data:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays students table
     */
    private void displayStudents() throws SQLException {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        // Create columns
        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        idCol.setPrefWidth(50);

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<Student, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Student, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Student, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        gpaCol.setPrefWidth(60);

        tableView.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, phoneCol, statusCol, gpaCol);

        // Fetch and display data
        ObservableList<Student> students = FXCollections.observableArrayList(studentDAO.getAllStudents());
        tableView.setItems(students);
    }

    /**
     * Displays courses table
     */
    private void displayCourses() throws SQLException {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        // Create columns
        TableColumn<Course, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        idCol.setPrefWidth(50);

        TableColumn<Course, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(100);

        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        nameCol.setPrefWidth(250);

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        creditsCol.setPrefWidth(70);

        TableColumn<Course, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        deptCol.setPrefWidth(150);

        tableView.getColumns().addAll(idCol, codeCol, nameCol, creditsCol, deptCol);

        // Fetch and display data
        ObservableList<Course> courses = FXCollections.observableArrayList(courseDAO.getAllCourses());
        tableView.setItems(courses);
    }

    /**
     * Displays enrollments table with JOIN data
     */
    private void displayEnrollments() throws SQLException {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        // Create columns
        TableColumn<Enrollment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        idCol.setPrefWidth(50);

        TableColumn<Enrollment, String> studentCol = new TableColumn<>("Student Name");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentCol.setPrefWidth(150);

        TableColumn<Enrollment, String> courseCol = new TableColumn<>("Course Name");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCol.setPrefWidth(200);

        TableColumn<Enrollment, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semesterCol.setPrefWidth(80);

        TableColumn<Enrollment, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(70);

        TableColumn<Enrollment, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setPrefWidth(60);

        TableColumn<Enrollment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
        statusCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, studentCol, courseCol, semesterCol, yearCol, gradeCol, statusCol);

        // Fetch and display data
        ObservableList<Enrollment> enrollments = FXCollections.observableArrayList(enrollmentDAO.getAllEnrollments());
        tableView.setItems(enrollments);
    }

    /**
     * Handles Add New button
     */
    @FXML
    private void handleAdd() {
        if (currentTable.isEmpty()) {
            AlertHelper.showWarning("No Table Selected", "Please select a table first.");
            return;
        }

        try {
            switch (currentTable) {
                case "students":
                    addStudent();
                    break;
                case "courses":
                    addCourse();
                    break;
                case "enrollments":
                    addEnrollment();
                    break;
                default:
                    AlertHelper.showInfo("Info", "Adding to '" + currentTable + "' is not supported.");
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to add record:\n" + e.getMessage());
        }
    }

    /**
     * Adds a new student
     */
    private void addStudent() throws SQLException {
        String firstName = field1.getText().trim();
        String lastName = field2.getText().trim();
        String email = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(firstName, lastName, email)) {
            AlertHelper.showWarning("Validation Error", "First Name, Last Name, and Email are required.");
            return;
        }

        if (!ValidationHelper.isValidEmail(email)) {
            AlertHelper.showWarning("Validation Error", "Please enter a valid email address.");
            return;
        }

        // Create student object
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(""); // Default empty
        student.setEnrollmentDate(LocalDate.now());
        student.setStatus("Active");
        student.setGpa(0.0);

        // Add to database
        boolean success = studentDAO.addStudent(student);

        if (success) {
            AlertHelper.showInfo("Success", "Student added successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to add student.");
        }
    }

    /**
     * Adds a new course
     */
    private void addCourse() throws SQLException {
        String courseCode = field1.getText().trim();
        String courseName = field2.getText().trim();
        String creditsStr = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(courseCode, courseName, creditsStr)) {
            AlertHelper.showWarning("Validation Error", "Course Code, Course Name, and Credits are required.");
            return;
        }

        if (!ValidationHelper.isValidInteger(creditsStr)) {
            AlertHelper.showWarning("Validation Error", "Credits must be a valid number.");
            return;
        }

        int credits = Integer.parseInt(creditsStr);
        if (!ValidationHelper.isValidCredits(credits)) {
            AlertHelper.showWarning("Validation Error", "Credits must be greater than 0.");
            return;
        }

        // Create course object
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setDepartment("General"); // Default
        course.setDescription(""); // Default empty

        // Add to database
        boolean success = courseDAO.addCourse(course);

        if (success) {
            AlertHelper.showInfo("Success", "Course added successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to add course.");
        }
    }

    /**
     * Adds a new enrollment
     */
    private void addEnrollment() throws SQLException {
        String studentIdStr = field1.getText().trim();
        String courseIdStr = field2.getText().trim();
        String semester = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(studentIdStr, courseIdStr, semester)) {
            AlertHelper.showWarning("Validation Error", "Student ID, Course ID, and Semester are required.");
            return;
        }

        if (!ValidationHelper.isValidInteger(studentIdStr) || !ValidationHelper.isValidInteger(courseIdStr)) {
            AlertHelper.showWarning("Validation Error", "Student ID and Course ID must be valid numbers.");
            return;
        }

        int studentId = Integer.parseInt(studentIdStr);
        int courseId = Integer.parseInt(courseIdStr);

        // Create enrollment object
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSemester(semester);
        enrollment.setYear(LocalDate.now().getYear());
        enrollment.setGrade(null); // No grade yet
        enrollment.setEnrollmentStatus("Enrolled");
        enrollment.setEnrollmentDate(LocalDate.now());

        // Add to database
        boolean success = enrollmentDAO.addEnrollment(enrollment);

        if (success) {
            AlertHelper.showInfo("Success", "Enrollment added successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to add enrollment.");
        }
    }

    /**
     * Handles Update Selected button
     */
    @FXML
    private void handleUpdate() {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            AlertHelper.showWarning("No Selection", "Please select a record to update.");
            return;
        }

        if (currentTable.isEmpty()) {
            AlertHelper.showWarning("No Table Selected", "Please select a table first.");
            return;
        }

        try {
            switch (currentTable) {
                case "students":
                    updateStudent((Student) selectedItem);
                    break;
                case "courses":
                    updateCourse((Course) selectedItem);
                    break;
                case "enrollments":
                    updateEnrollment((Enrollment) selectedItem);
                    break;
                default:
                    AlertHelper.showInfo("Info", "Updating '" + currentTable + "' is not supported.");
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to update record:\n" + e.getMessage());
        }
    }

    /**
     * Updates a student record
     */
    private void updateStudent(Student student) throws SQLException {
        String firstName = field1.getText().trim();
        String lastName = field2.getText().trim();
        String email = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(firstName, lastName, email)) {
            AlertHelper.showWarning("Validation Error", "First Name, Last Name, and Email are required.");
            return;
        }

        if (!ValidationHelper.isValidEmail(email)) {
            AlertHelper.showWarning("Validation Error", "Please enter a valid email address.");
            return;
        }

        // Update student object
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);

        // Update in database
        boolean success = studentDAO.updateStudent(student);

        if (success) {
            AlertHelper.showInfo("Success", "Student updated successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to update student.");
        }
    }

    /**
     * Updates a course record
     */
    private void updateCourse(Course course) throws SQLException {
        String courseCode = field1.getText().trim();
        String courseName = field2.getText().trim();
        String creditsStr = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(courseCode, courseName, creditsStr)) {
            AlertHelper.showWarning("Validation Error", "Course Code, Course Name, and Credits are required.");
            return;
        }

        if (!ValidationHelper.isValidInteger(creditsStr)) {
            AlertHelper.showWarning("Validation Error", "Credits must be a valid number.");
            return;
        }

        int credits = Integer.parseInt(creditsStr);
        if (!ValidationHelper.isValidCredits(credits)) {
            AlertHelper.showWarning("Validation Error", "Credits must be greater than 0.");
            return;
        }

        // Update course object
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredits(credits);

        // Update in database
        boolean success = courseDAO.updateCourse(course);

        if (success) {
            AlertHelper.showInfo("Success", "Course updated successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to update course.");
        }
    }

    /**
     * Updates an enrollment record
     */
    private void updateEnrollment(Enrollment enrollment) throws SQLException {
        String studentIdStr = field1.getText().trim();
        String courseIdStr = field2.getText().trim();
        String semester = field3.getText().trim();

        // Validate inputs
        if (!ValidationHelper.isNotEmpty(studentIdStr, courseIdStr, semester)) {
            AlertHelper.showWarning("Validation Error", "Student ID, Course ID, and Semester are required.");
            return;
        }

        if (!ValidationHelper.isValidInteger(studentIdStr) || !ValidationHelper.isValidInteger(courseIdStr)) {
            AlertHelper.showWarning("Validation Error", "Student ID and Course ID must be valid numbers.");
            return;
        }

        int studentId = Integer.parseInt(studentIdStr);
        int courseId = Integer.parseInt(courseIdStr);

        // Update enrollment object
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSemester(semester);

        // Update in database
        boolean success = enrollmentDAO.updateEnrollment(enrollment);

        if (success) {
            AlertHelper.showInfo("Success", "Enrollment updated successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to update enrollment.");
        }
    }

    /**
     * Handles Delete Selected button
     */
    @FXML
    private void handleDelete() {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            AlertHelper.showWarning("No Selection", "Please select a record to delete.");
            return;
        }

        // Confirm deletion
        boolean confirmed = AlertHelper.showConfirmation("Delete Confirmation",
                "Are you sure you want to delete this record?\nThis action cannot be undone.");

        if (!confirmed) {
            return;
        }

        if (currentTable.isEmpty()) {
            AlertHelper.showWarning("No Table Selected", "Please select a table first.");
            return;
        }

        try {
            switch (currentTable) {
                case "students":
                    deleteStudent((Student) selectedItem);
                    break;
                case "courses":
                    deleteCourse((Course) selectedItem);
                    break;
                case "enrollments":
                    deleteEnrollment((Enrollment) selectedItem);
                    break;
                default:
                    AlertHelper.showInfo("Info", "Deleting from '" + currentTable + "' is not supported.");
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to delete record:\n" + e.getMessage());
        }
    }

    /**
     * Deletes a student record
     */
    private void deleteStudent(Student student) throws SQLException {
        boolean success = studentDAO.deleteStudent(student.getStudentId());

        if (success) {
            AlertHelper.showInfo("Success", "Student deleted successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to delete student.");
        }
    }

    /**
     * Deletes a course record
     */
    private void deleteCourse(Course course) throws SQLException {
        boolean success = courseDAO.deleteCourse(course.getCourseId());

        if (success) {
            AlertHelper.showInfo("Success", "Course deleted successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to delete course.");
        }
    }

    /**
     * Deletes an enrollment record
     */
    private void deleteEnrollment(Enrollment enrollment) throws SQLException {
        boolean success = enrollmentDAO.deleteEnrollment(enrollment.getEnrollmentId());

        if (success) {
            AlertHelper.showInfo("Success", "Enrollment deleted successfully!");
            handleDisplayContents(); // Refresh display
            clearForm();
        } else {
            AlertHelper.showError("Error", "Failed to delete enrollment.");
        }
    }

    /**
     * Handles Custom Query button - opens query dialog
     */
    @FXML
    private void handleCustomQuery() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QueryView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Custom SQL Query");
            stage.setScene(new Scene(root, 600, 500));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to open query window:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Clears form fields
     */
    private void clearForm() {
        field1.clear();
        field2.clear();
        field3.clear();
    }

    /**
     * Populates form fields from selected table row
     */
    private void populateFormFromSelection() {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        try {
            switch (currentTable) {
                case "students":
                    Student student = (Student) selectedItem;
                    field1.setText(student.getFirstName());
                    field2.setText(student.getLastName());
                    field3.setText(student.getEmail());
                    break;
                case "courses":
                    Course course = (Course) selectedItem;
                    field1.setText(course.getCourseCode());
                    field2.setText(course.getCourseName());
                    field3.setText(String.valueOf(course.getCredits()));
                    break;
                case "enrollments":
                    Enrollment enrollment = (Enrollment) selectedItem;
                    field1.setText(String.valueOf(enrollment.getStudentId()));
                    field2.setText(String.valueOf(enrollment.getCourseId()));
                    field3.setText(enrollment.getSemester());
                    break;
            }
        } catch (Exception e) {
            // Silently ignore casting errors
        }
    }

    /**
     * Updates field labels based on selected table
     */
    private void updateFieldLabels() {
        // Note: Since FXML doesn't have label references, we'll keep generic field names
        // In a production app, we would dynamically update labels
        // For now, field mapping is:
        // Students: field1=firstName, field2=lastName, field3=email
        // Courses: field1=courseCode, field2=courseName, field3=credits
        // Enrollments: field1=studentId, field2=courseId, field3=semester
    }
}
