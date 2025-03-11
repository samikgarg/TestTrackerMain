# 📊 Test Tracker App 📝

## 📌 Description
The **Test Tracker App** is an interactive **study management tool** designed to help students **track, analyze, and manage their test scores and study progress**. Built as part of the **IB Diploma Computer Science Higher Level Course**, this app **organizes test results, monitors subject-wise progress, and helps users set and achieve academic goals**. 

With features like **test tracking, filtering, sorting, goal setting, and progress analysis**, the app provides **insightful data visualizations** to aid in performance improvement. The app ensures **all test data is automatically saved**, allowing users to resume tracking seamlessly.

---

## 🚀 Features

### ✅ **Test Tracking & Management**
- **Dynamic Test List**: Displays all recorded tests in a structured **table format**.
- **Test Details**: Displays **test name, subject, score, total marks, percentage**, and **reflection**.
- **Mock Exam Toggle**: Tests can be marked/unmarked as **mock exams**.
- **Add, Edit, and Delete Tests**:
  - A **new test** can be added by entering **test name, score, total marks, and reflection**.
  - **Existing subjects** can be selected, or a **new subject** can be added.
  - **Validation errors** occur for incorrect values (e.g., score > total marks).
  - **Edits and deletions** are instantly reflected in the main table.
  - **Undo feature** allows changes to be reverted.

---

### 🔍 **Filtering, Sorting & Searching**
- **Subject Filtering**: A **Combo Box** allows filtering tests by **subject**.
- **Search Functionality**: Users can type in a search string to display **matching tests**.
- **Sorting Options**:
  - Tests can be **sorted by**:
    - 📊 **Percentage Score**
    - 📅 **Date Added**
    - 📖 **Test Name**
    - 🎯 **Subject**

---

### 📈 **Data Visualization**
- **Graphical Representation**:
  - **Bar & Line Graphs** visualize test performance.
  - Graphs **adjust dynamically** based on applied filters and sorting preferences.
  - **Graph Type Toggle** allows switching between **bar and line graph** views.

---

### 🎯 **Goal Setting**
- **Customizable Subject Goals**:
  - Users can set **average percentage score goals** for each subject or **all subjects combined**.
  - **Mock Exam Inclusion**: Users can toggle whether **mock exams** are included in the goal calculations.
  - A **default goal of 80%** is added for each subject.
  - **Validation ensures** that only valid percentage values are accepted.

---

### 📊 **Performance Analysis**
- **Goal Progress Tracking**:
  - The **Analyze Page** calculates the **average score** for each subject and compares it with the **goal score**.
  - If the **goal has not been met**, the app estimates:
    - 📉 **Minimum number of tests needed** to reach the goal.
    - 🏆 **Required scores for upcoming tests**.
  - If the **goal is met**, the app calculates:
    - 🎯 **Score needed to maintain the goal** in the next test.
  - If the goal is **unachievable in 50 tests**, a warning is displayed.
  - **Graphical Data Visualization**:
    - Past test scores and required future scores are plotted for better understanding.

---

### 💾 **Persistent Data Storage**
- **All test records and goal settings** are **saved automatically**.
- Data is **retained even after closing the application**.

---

### 📝 **Activity Log & History**
- **Change Log**:
  - The **Show Log Button** displays a history of **modifications made to tests and goals**.

---

## 🛠️ Technologies Used
- **Java** (Backend Logic)
- **Java Swing & AWT** (User Interface)
- **SQLite** (Database Management)
- **JFreeChart** (Data Visualization)

---
