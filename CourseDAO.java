package advancejavaproject4.database;

import advancejavaproject4.model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO {


    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseName(rs.getString("course_name"));
                course.setCredits(rs.getInt("credits"));
                course.setDepartment(rs.getString("department"));
                course.setDescription(rs.getString("description"));

                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all courses: " + e.getMessage());
            throw e;
        }

        return courses;
    }


    public Course getCourseById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        Course course = null;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course = new Course();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCredits(rs.getInt("credits"));
                    course.setDepartment(rs.getString("department"));
                    course.setDescription(rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course by ID: " + e.getMessage());
            throw e;
        }

        return course;
    }

 
    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_code, course_name, credits, department, description) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDepartment());
            pstmt.setString(5, course.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate course code. Course code must be unique.", e);
            }
            System.err.println("Error adding course: " + e.getMessage());
            throw e;
        }
    }

    public boolean updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ?, " +
                     "department = ?, description = ? WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDepartment());
            pstmt.setString(5, course.getDescription());
            pstmt.setInt(6, course.getCourseId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Duplicate course code. Course code must be unique.", e);
            }
            System.err.println("Error updating course: " + e.getMessage());
            throw e;
        }
    }

    public boolean deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Check for foreign key constraint violation
            if (e.getErrorCode() == 1451) {
                throw new SQLException("Cannot delete course with active enrollments. " +
                        "Please delete enrollments first or use CASCADE DELETE.", e);
            }
            System.err.println("Error deleting course: " + e.getMessage());
            throw e;
        }
    }


    public List<Course> searchCourses(String keyword) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE course_code LIKE ? OR course_name LIKE ? " +
                     "OR department LIKE ? ORDER BY course_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourseId(rs.getInt("course_id"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setCredits(rs.getInt("credits"));
                    course.setDepartment(rs.getString("department"));
                    course.setDescription(rs.getString("description"));

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching courses: " + e.getMessage());
            throw e;
        }

        return courses;
    }
}
