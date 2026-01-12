package advancejavaproject4.database;

import advancejavaproject4.model.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Enrollment entity
 * Handles all CRUD operations for the enrollments table
 * @author yigitt
 */
public class EnrollmentDAO {

    public List<Enrollment> getAllEnrollments() throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, " +
                     "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                     "c.course_name " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "ORDER BY e.enrollment_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setSemester(rs.getString("semester"));
                enrollment.setYear(rs.getInt("year"));
                enrollment.setGrade(rs.getString("grade"));
                enrollment.setEnrollmentStatus(rs.getString("enrollment_status"));

                Date enrollDate = rs.getDate("enrollment_date");
                if (enrollDate != null) {
                    enrollment.setEnrollmentDate(enrollDate.toLocalDate());
                }

                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseName(rs.getString("course_name"));

                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all enrollments: " + e.getMessage());
            throw e;
        }

        return enrollments;
    }


    public List<Enrollment> getEnrollmentsByStudent(int studentId) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, " +
                     "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                     "c.course_name " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ? " +
                     "ORDER BY e.year DESC, e.semester";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setSemester(rs.getString("semester"));
                    enrollment.setYear(rs.getInt("year"));
                    enrollment.setGrade(rs.getString("grade"));
                    enrollment.setEnrollmentStatus(rs.getString("enrollment_status"));

                    Date enrollDate = rs.getDate("enrollment_date");
                    if (enrollDate != null) {
                        enrollment.setEnrollmentDate(enrollDate.toLocalDate());
                    }

                    enrollment.setStudentName(rs.getString("student_name"));
                    enrollment.setCourseName(rs.getString("course_name"));

                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching enrollments by student: " + e.getMessage());
            throw e;
        }

        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, " +
                     "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                     "c.course_name " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.course_id = ? " +
                     "ORDER BY s.last_name, s.first_name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                    enrollment.setStudentId(rs.getInt("student_id"));
                    enrollment.setCourseId(rs.getInt("course_id"));
                    enrollment.setSemester(rs.getString("semester"));
                    enrollment.setYear(rs.getInt("year"));
                    enrollment.setGrade(rs.getString("grade"));
                    enrollment.setEnrollmentStatus(rs.getString("enrollment_status"));

                    Date enrollDate = rs.getDate("enrollment_date");
                    if (enrollDate != null) {
                        enrollment.setEnrollmentDate(enrollDate.toLocalDate());
                    }

                    enrollment.setStudentName(rs.getString("student_name"));
                    enrollment.setCourseName(rs.getString("course_name"));

                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching enrollments by course: " + e.getMessage());
            throw e;
        }

        return enrollments;
    }


    public boolean addEnrollment(Enrollment enrollment) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id, semester, year, " +
                     "grade, enrollment_status, enrollment_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setString(3, enrollment.getSemester());
            pstmt.setInt(4, enrollment.getYear());
            pstmt.setString(5, enrollment.getGrade());
            pstmt.setString(6, enrollment.getEnrollmentStatus());

            if (enrollment.getEnrollmentDate() != null) {
                pstmt.setDate(7, Date.valueOf(enrollment.getEnrollmentDate()));
            } else {
                pstmt.setNull(7, Types.DATE);
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for duplicate enrollment constraint violation
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate enrollment. Student is already enrolled in this course for the same semester and year.", e);
            }
            // Check for foreign key constraint violation
            if (e.getErrorCode() == 1452) {
                throw new SQLException("Invalid student ID or course ID. Please ensure the student and course exist.", e);
            }
            System.err.println("Error adding enrollment: " + e.getMessage());
            throw e;
        }
    }


    public boolean updateEnrollment(Enrollment enrollment) throws SQLException {
        String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, semester = ?, " +
                     "year = ?, grade = ?, enrollment_status = ?, enrollment_date = ? " +
                     "WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setString(3, enrollment.getSemester());
            pstmt.setInt(4, enrollment.getYear());
            pstmt.setString(5, enrollment.getGrade());
            pstmt.setString(6, enrollment.getEnrollmentStatus());

            if (enrollment.getEnrollmentDate() != null) {
                pstmt.setDate(7, Date.valueOf(enrollment.getEnrollmentDate()));
            } else {
                pstmt.setNull(7, Types.DATE);
            }

            pstmt.setInt(8, enrollment.getEnrollmentId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for duplicate enrollment constraint violation
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate enrollment. Student is already enrolled in this course for the same semester and year.", e);
            }
            // Check for foreign key constraint violation
            if (e.getErrorCode() == 1452) {
                throw new SQLException("Invalid student ID or course ID.", e);
            }
            System.err.println("Error updating enrollment: " + e.getMessage());
            throw e;
        }
    }

    
    public boolean deleteEnrollment(int enrollmentId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            throw e;
        }
    }
}
