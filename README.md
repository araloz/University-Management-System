# 🎓 UniTrack  
## University Enrollment & Course Management System

UniTrack is a JavaFX-based desktop application designed to manage university students, courses, and enrollments.  
It provides a structured and user-friendly way to register students, define courses, and manage student–course relationships through a relational database.

The project is built using a layered architecture with the DAO (Data Access Object) pattern, making it modular, maintainable, and scalable.

---

## 🚀 Features

- Add, view, and manage students  
- Add and manage university courses  
- Enroll students into courses  
- Query and display enrollment data  
- Input validation and error handling  
- Alert-based user feedback  
- Database-driven persistent storage  

---

## 🧩 Project Architecture

The project follows a clean **MVC + DAO** architecture.

### Models
- Student  
- Course  
- Enrollment  

### Data Access Layer (DAO)
- StudentDAO  
- CourseDAO  
- EnrollmentDAO  
- DatabaseConnection  

This layer handles all database operations and isolates SQL logic from the UI.

### User Interface
Built with JavaFX and FXML:
- MainView.fxml  
- QueryView.fxml  

Controllers:
- MainViewController  
- QueryViewController  

### Utility Classes
- ValidationHelper – validates user inputs  
- AlertHelper – shows user-friendly alerts  

---

## 🗄️ Database

The application uses a relational database (via JDBC) to store:
- Students  
- Courses  
- Student enrollments  

All database communication is handled through the DAO layer.

---

## 🛠️ Technologies Used

- Java  
- JavaFX  
- FXML  
- JDBC  
- SQL  
- DAO Design Pattern  
- MVC Architecture  

---

## ▶️ How to Run

1. Install Java (JDK 11 or later)  
2. Configure your database credentials in `DatabaseConnection.java`  
3. Run `AdvanceJavaProject4.java`  
4. The JavaFX interface will start  

---

## 🎯 Project Purpose

UniTrack was developed as an advanced Java project to demonstrate:
- Object-oriented programming  
- Database integration  
- GUI development  
- Clean architecture using DAO and MVC patterns  

It simulates a real university enrollment management system.

---

## 📌 Future Improvements

- User authentication  
- Course capacity limits  
- Student transcript generation  
- Export to PDF or CSV  
- Web-based version  
