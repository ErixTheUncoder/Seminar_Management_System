# Academic Seminar Management System

A comprehensive Java Swing desktop application for managing academic seminars, presentations, evaluations, and awards.

## 🎯 Features

### Student Features
- ✅ Login with credentials
- ✅ View available sessions
- ✅ Register for seminars (submit title, abstract, supervisor, file, presentation type)
- ✅ View registered sessions with schedule and evaluators
- ✅ View evaluation scores and comments
- ✅ Vote for presentations (People's Choice Award)
- ✅ View awards list

### Evaluator Features
- ✅ Login with credentials
- ✅ View assigned sessions
- ✅ View presentations in sessions
- ✅ Provide evaluation based on rubrics (Problem Clarity, Methodology, Results, Presentation)
- ✅ Add comments for each presentation
- ✅ Edit evaluations

### Coordinator Features
- ✅ Login with credentials
- ✅ Create and manage sessions (date, venue, time, type)
- ✅ View evaluator list and schedules
- ✅ Assign evaluators to sessions
- ✅ Assign evaluators and sessions to submissions
- ✅ Assign poster boards to poster presentations
- ✅ Generate reports with analytics (participant count, average scores)
- ✅ Generate awards list (Best Overall, Best Poster, Best Oral, People's Choice)

## 🏗️ Architecture

### 3-Tier Architecture:
1. **Presentation Layer (GUI)** - Swing components for user interaction
2. **Service Layer** - FileService for data persistence and business logic
3. **Domain Model Layer** - Core business entities

### Design Patterns:
- **MVC Pattern** - Separation of Model, View, Controller
- **Inheritance** - User → Student/Evaluator/Coordinator
- **Composition** - Submission contains Evaluation (inner class)
- **Factory Pattern** - FileService creates objects from CSV
- **State Pattern** - EvaluationStat enum (PENDING → PROCESSING → COMPLETED)

### Project Structure:
```
seminar-system/
├── model/              # Domain entities
│   ├── User.java
│   ├── Student.java
│   ├── Evaluator.java
│   ├── Coordinator.java
│   ├── Session.java
│   ├── Submission.java
│   ├── Board.java
│   ├── Role.java
│   ├── SessionType.java
│   └── EvaluationStat.java
├── service/            # Business logic & persistence
│   └── FileService.java
├── gui/                # Swing UI components
│   ├── AppGUI.java
│   ├── LoginScreen.java
│   ├── StudentDashboard.java
│   ├── EvaluatorDashboard.java
│   └── CoordinatorDashboard.java
└── data/               # CSV data files
    ├── StudentData.csv
    ├── EvaluatorData.csv
    ├── CoordinatorData.csv
    ├── SessionData.csv
    ├── SubmissionData.csv
    └── BoardData.csv
```

## 🚀 How to Run

### Prerequisites:
- Java JDK 8 or higher
- Terminal/Command Prompt

### Compilation & Execution:

**On Linux/Mac:**
```bash
# Navigate to the seminar-system directory
cd seminar-system

# Compile all Java files
find . -name "*.java" -type f > sources.txt
javac -d . @sources.txt

# Run the application
java gui.AppGUI
```

**On Windows:**
```cmd
REM Navigate to the seminar-system directory
cd seminar-system

REM Compile all Java files
dir /s /B *.java > sources.txt
javac -d . @sources.txt

REM Run the application
java gui.AppGUI
```

## 🔐 Test Credentials

### Student Accounts:
- Email: `alice@email.com` | Password: `pass123`
- Email: `bob@email.com` | Password: `pass123`
- Email: `carol@email.com` | Password: `pass123`

### Evaluator Accounts:
- Email: `smith@email.com` | Password: `pass456`
- Email: `lee@email.com` | Password: `pass456`
- Email: `garcia@email.com` | Password: `pass456`

### Coordinator Accounts:
- Email: `brown@email.com` | Password: `pass789`
- Email: `white@email.com` | Password: `pass789`

## 📊 Data Persistence

All data is stored in CSV files in the `data/` directory:
- **StudentData.csv** - Student records
- **EvaluatorData.csv** - Evaluator records
- **CoordinatorData.csv** - Coordinator records
- **SessionData.csv** - Session/seminar records
- **SubmissionData.csv** - Presentation submissions with evaluations
- **BoardData.csv** - Poster board inventory

## 🎓 Evaluation Rubric

Each presentation is scored out of 40 points:
- **Problem Clarity** (0-10 points)
- **Methodology** (0-10 points)
- **Results** (0-10 points)
- **Presentation** (0-10 points)

## 🏆 Awards Categories

1. **Best Overall Presentation** - Highest total score
2. **Best Poster Presentation** - Highest score among poster presentations
3. **Best Oral Presentation** - Highest score among oral presentations
4. **People's Choice Award** - Most votes from students

## 📝 Usage Guide

### For Students:
1. Login with student credentials
2. Navigate to "Available Sessions" tab
3. Select a session and click "Register for Selected Session"
4. Fill in presentation details (title, abstract, supervisor, file, type)
5. View your submissions in "My Submissions" tab
6. Vote for other presentations in "My Submissions" → "Vote (People's Choice)"
7. View evaluation results once evaluators complete their reviews

### For Evaluators:
1. Login with evaluator credentials
2. View assigned submissions in "Assigned Submissions" tab
3. Select a submission and click "Evaluate Selected"
4. Score each criterion (0-10) and provide comments
5. Submit evaluation (status changes to COMPLETED)

### For Coordinators:
1. Login with coordinator credentials
2. Create sessions in "Manage Sessions" tab
3. Assign evaluators to sessions
4. Assign evaluators and sessions to submissions in "Manage Submissions" tab
5. Generate analytics reports in "Reports & Analytics" tab
6. Generate awards list in "Generate Awards" tab

## 🛠️ Technical Notes

- **CSV Format**: All data uses comma-separated values
- **Date Format**: YYYY-MM-DD HH:MM (e.g., 2026-03-15 14:00)
- **File Paths**: Submission file paths are stored as strings (actual file upload not implemented)
- **State Management**: Evaluation status follows PENDING → PROCESSING → COMPLETED lifecycle
- **Thread Safety**: Not thread-safe; designed for single-user desktop use

## 🔧 Customization

To add new users, edit the corresponding CSV file in `data/` directory:
- Add students to `StudentData.csv`
- Add evaluators to `EvaluatorData.csv`
- Add coordinators to `CoordinatorData.csv`

## 📄 License

Educational project for Object-Oriented Analysis and Design course.

## 👥 System Users

- **Students** - Submit and view presentations
- **Evaluators** - Review and score presentations
- **Coordinators** - Manage entire system and generate reports
