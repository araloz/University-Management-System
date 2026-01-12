package advancejavaproject4.model;

import javafx.beans.property.*;

/**
 * Course data model representing a course record
 * @author yigitt
 */
public class Course {
    private final IntegerProperty courseId;
    private final StringProperty courseCode;
    private final StringProperty courseName;
    private final IntegerProperty credits;
    private final StringProperty department;
    private final StringProperty description;

    public Course() {
        this.courseId = new SimpleIntegerProperty();
        this.courseCode = new SimpleStringProperty();
        this.courseName = new SimpleStringProperty();
        this.credits = new SimpleIntegerProperty();
        this.department = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

  
    public Course(int courseId, String courseCode, String courseName,
                  int credits, String department, String description) {
        this();
        setCourseId(courseId);
        setCourseCode(courseCode);
        setCourseName(courseName);
        setCredits(credits);
        setDepartment(department);
        setDescription(description);
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

    // Course Code
    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode.set(courseCode);
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

    public IntegerProperty creditsProperty() {
        return credits;
    }

    public int getCredits() {
        return credits.get();
    }

    public void setCredits(int credits) {
        this.credits.set(credits);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + getCourseId() +
                ", courseCode='" + getCourseCode() + '\'' +
                ", courseName='" + getCourseName() + '\'' +
                ", credits=" + getCredits() +
                ", department='" + getDepartment() + '\'' +
                '}';
    }
}
