package advancejavaproject4.model;

import javafx.beans.property.*;
import java.time.LocalDate;


public class Student {
    private final IntegerProperty studentId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty phone;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final ObjectProperty<LocalDate> enrollmentDate;
    private final StringProperty status;
    private final DoubleProperty gpa;


    public Student() {
        this.studentId = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.dateOfBirth = new SimpleObjectProperty<>();
        this.enrollmentDate = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
        this.gpa = new SimpleDoubleProperty();
    }

  
    public Student(int studentId, String firstName, String lastName, String email,
                   String phone, LocalDate dateOfBirth, LocalDate enrollmentDate,
                   String status, double gpa) {
        this();
        setStudentId(studentId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setDateOfBirth(dateOfBirth);
        setEnrollmentDate(enrollmentDate);
        setStatus(status);
        setGpa(gpa);
    }

    public IntegerProperty studentIdProperty() {
        return studentId;
    }

    public int getStudentId() {
        return studentId.get();
    }

    public void setStudentId(int studentId) {
        this.studentId.set(studentId);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public ObjectProperty<LocalDate> enrollmentDateProperty() {
        return enrollmentDate;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate.get();
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate.set(enrollmentDate);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public DoubleProperty gpaProperty() {
        return gpa;
    }

    public double getGpa() {
        return gpa.get();
    }

    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + getStudentId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", gpa=" + getGpa() +
                '}';
    }
}
