# Quick Start Guide

## 🚀 Running the Application

### Option 1: Use the Run Script (Recommended)

**Linux/Mac:**
```bash
cd seminar-system
./run.sh
```

**Windows:**
```cmd
cd seminar-system
run.bat
```

### Option 2: Manual Compilation

**Linux/Mac:**
```bash
cd seminar-system
find . -name "*.java" -type f > sources.txt
javac -d . @sources.txt
java gui.AppGUI
```

**Windows:**
```cmd
cd seminar-system
dir /s /B *.java > sources.txt
javac -d . @sources.txt
java gui.AppGUI
```

## 🔑 Login Credentials

### Quick Test Accounts:

**Student:**
- Email: `alice@email.com`
- Password: `pass123`

**Evaluator:**
- Email: `smith@email.com`
- Password: `pass456`

**Coordinator:**
- Email: `brown@email.com`
- Password: `pass789`

## 📋 Quick Workflow

### Student Workflow:
1. Login as student (alice@email.com / pass123)
2. Go to "Available Sessions" tab
3. Select "Spring Research Symposium"
4. Click "Register for Selected Session"
5. Fill in:
   - Title: "My Research Project"
   - Abstract: "This is my research..."
   - Supervisor: "Dr. Anderson"
   - File Path: "/files/myproject.pdf"
   - Type: POSTER or ORAL
6. Click "Submit Registration"
7. View your submission in "My Submissions" tab

### Evaluator Workflow:
1. Login as evaluator (smith@email.com / pass456)
2. Go to "Assigned Submissions" tab
3. Select a submission
4. Click "Evaluate Selected"
5. Score each criterion (0-10):
   - Problem Clarity
   - Methodology
   - Results
   - Presentation
6. Add comments
7. Click "Submit Evaluation"

### Coordinator Workflow:
1. Login as coordinator (brown@email.com / pass789)
2. **Create a Session:**
   - Go to "Manage Sessions" tab
   - Click "Create New Session"
   - Fill in title, type, date, time, venue
   - Click "Create Session"

3. **Assign Evaluators to Session:**
   - Select a session
   - Click "Assign Evaluators"
   - Select evaluators from list
   - Click "Assign Selected Evaluators"

4. **Assign Submission to Evaluator:**
   - Go to "Manage Submissions" tab
   - Select a submission
   - Click "Assign Evaluator & Session"
   - Choose session, evaluator, and board (if poster)
   - Click "Assign"

5. **Generate Reports:**
   - Go to "Reports & Analytics" tab
   - Click "Generate Report"
   - View statistics and analytics

6. **Generate Awards:**
   - Go to "Generate Awards" tab
   - Click "Generate Awards List"
   - View award winners

## 🎯 Testing the Complete Flow

1. **Login as Coordinator** (brown@email.com / pass789)
   - Create a new session
   - Assign evaluator EVAL001 to the session

2. **Login as Student** (carol@email.com / pass123)
   - Register for the new session
   - Submit presentation details

3. **Login as Coordinator** again
   - Go to "Manage Submissions"
   - Assign the new submission to session and evaluator EVAL001

4. **Login as Evaluator** (smith@email.com / pass456)
   - View assigned submission
   - Provide evaluation with scores and comments

5. **Login as Student** again
   - View evaluation results in "My Submissions"
   - Vote for other presentations

6. **Login as Coordinator** again
   - Generate analytics report
   - Generate awards list

## 📁 Data Files

All data is stored in `data/` directory as CSV files:
- StudentData.csv
- EvaluatorData.csv
- CoordinatorData.csv
- SessionData.csv
- SubmissionData.csv
- BoardData.csv

You can edit these files directly to add more test data.

## ⚠️ Troubleshooting

**Problem:** "javac not found"
**Solution:** Install Java JDK and add to PATH

**Problem:** "Class not found"
**Solution:** Make sure you're in the seminar-system directory when running

**Problem:** CSV files not loading
**Solution:** Ensure data/ directory exists with all CSV files

**Problem:** Can't login
**Solution:** Check credentials match those in CSV files exactly

## 💡 Tips

- The system uses CSV files - all changes are saved automatically
- Evaluation status follows: PENDING → PROCESSING → COMPLETED
- Only assigned evaluators can evaluate submissions
- Coordinators have full control over the system
- Students can vote for any submission except their own

## 📞 Features by Role

**Student:** Register, View, Vote
**Evaluator:** Evaluate, Comment, View
**Coordinator:** Create, Assign, Report, Awards

Enjoy using the Academic Seminar Management System! 🎓
