package advancejavaproject4.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Enrollment {
    private final IntegerProperty enrollmentId;
    private final IntegerProperty studentId;
    private final IntegerProperty courseId;
    private final StringProperty semester;
    private final IntegerProperty year;
    private final StringProperty grade;
    private final StringProperty enrollmentStatus;
    private final ObjectProperty<LocalDate> enrollmentDate;

    private final StringProperty studentName;
    private final StringProperty courseName;

    
    public Enrollment() {
        this.enrollmentId = new SimpleIntegerProperty();
        this.studentId = new SimpleIntegerProperty();
        this.courseId = new SimpleIntegerProperty();
        this.semester = new SimpleStringProperty();
        this.year = new SimpleIntegerProperty();
        this.grade = new SimpleStringProperty();
        this.enrollmentStatus = new SimpleStringProperty();
        this.enrollmentDate = new SimpleObjectProperty<>();
        this.studentName = new SimpleStringProperty();
        this.courseName = new SimpleStringProperty();
    }


    public Enrollment(int enrollmentId, int studentId, int courseId,
                      String semester, int year, String grade,
                      String enrollmentStatus, LocalDate enrollmentDate) {
        this();
        setEnrollmentId(enrollmentId);
        setStudentId(studentId);
        setCourseId(courseId);
        setSemester(semester);
        setYear(year);
        setGrade(grade);
        setEnrollmentStatus(enrollmentStatus);
        setEnrollmentDate(enrollmentDate);
    }

    public IntegerProperty enrollmentIdProperty() {
        return enrollmentId;
    }

    public int getEnrollmentId() {
        return enrollmentId.get();
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId.set(enrollmentId);
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

    public IntegerProperty courseIdProperty() {
        return courseId;
    }

    public int getCourseId() {
        return courseId.get();
    }

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public int getYear() {
        return year.get();
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public StringProperty enrollmentStatusProperty() {
        return enrollmentStatus;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus.get();
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus.set(enrollmentStatus);
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

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + getEnrollmentId() +
                ", studentId=" + getStudentId() +
                ", courseId=" + getCourseId() +
                ", semester='" + getSemester() + '\'' +
                ", year=" + getYear() +
                ", grade='" + getGrade() + '\'' +
                ", enrollmentStatus='" + getEnrollmentStatus() + '\'' +
                '}';
    }
}
