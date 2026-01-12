package advancejavaproject4.database;

import advancejavaproject4.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student entity
 * Handles all CRUD operations for the students table
 * @author yigitt
 */
public class StudentDAO {

 
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));

                // Handle null dates
                Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    student.setDateOfBirth(dob.toLocalDate());
                }

                Date enrollDate = rs.getDate("enrollment_date");
                if (enrollDate != null) {
                    student.setEnrollmentDate(enrollDate.toLocalDate());
                }

                student.setStatus(rs.getString("status"));
                student.setGpa(rs.getDouble("gpa"));

                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all students: " + e.getMessage());
            throw e;
        }

        return students;
    }


    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        Student student = null;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));

                    Date dob = rs.getDate("date_of_birth");
                    if (dob != null) {
                        student.setDateOfBirth(dob.toLocalDate());
                    }

                    Date enrollDate = rs.getDate("enrollment_date");
                    if (enrollDate != null) {
                        student.setEnrollmentDate(enrollDate.toLocalDate());
                    }

                    student.setStatus(rs.getString("status"));
                    student.setGpa(rs.getDouble("gpa"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
            throw e;
        }

        return student;
    }


    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, phone, " +
                     "date_of_birth, enrollment_date, status, gpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());

            // Handle null dates
            if (student.getDateOfBirth() != null) {
                pstmt.setDate(5, Date.valueOf(student.getDateOfBirth()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            if (student.getEnrollmentDate() != null) {
                pstmt.setDate(6, Date.valueOf(student.getEnrollmentDate()));
            } else {
                pstmt.setNull(6, Types.DATE);
            }

            pstmt.setString(7, student.getStatus());
            pstmt.setDouble(8, student.getGpa());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for duplicate email constraint violation
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate email address. Email must be unique.", e);
            }
            System.err.println("Error adding student: " + e.getMessage());
            throw e;
        }
    }

    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, " +
                     "phone = ?, date_of_birth = ?, enrollment_date = ?, status = ?, gpa = ? " +
                     "WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());

            // Handle null dates
            if (student.getDateOfBirth() != null) {
                pstmt.setDate(5, Date.valueOf(student.getDateOfBirth()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            if (student.getEnrollmentDate() != null) {
                pstmt.setDate(6, Date.valueOf(student.getEnrollmentDate()));
            } else {
                pstmt.setNull(6, Types.DATE);
            }

            pstmt.setString(7, student.getStatus());
            pstmt.setDouble(8, student.getGpa());
            pstmt.setInt(9, student.getStudentId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for duplicate email constraint violation
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate email address. Email must be unique.", e);
            }
            System.err.println("Error updating student: " + e.getMessage());
            throw e;
        }
    }


    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for foreign key constraint violation
            if (e.getErrorCode() == 1451) {
                throw new SQLException("Cannot delete student with active enrollments. " +
                        "Please delete enrollments first or use CASCADE DELETE.", e);
            }
            System.err.println("Error deleting student: " + e.getMessage());
            throw e;
        }
    }

 
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE first_name LIKE ? OR last_name LIKE ? " +
                     "OR email LIKE ? ORDER BY student_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));

                    Date dob = rs.getDate("date_of_birth");
                    if (dob != null) {
                        student.setDateOfBirth(dob.toLocalDate());
                    }

                    Date enrollDate = rs.getDate("enrollment_date");
                    if (enrollDate != null) {
                        student.setEnrollmentDate(enrollDate.toLocalDate());
                    }

                    student.setStatus(rs.getString("status"));
                    student.setGpa(rs.getDouble("gpa"));

                    students.add(student);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
            throw e;
        }

        return students;
    }
}
