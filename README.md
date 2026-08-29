# Employee Leave Management System – Java OOP Project

A beginner-friendly console-based Java application designed to demonstrate Object-Oriented Programming (OOP) principles through an Employee Leave Management System with automatic duration calculation and manager approval workflows.

## 📌 Project Overview
The system allows employees to submit leave applications with automatic day calculation, enables managers to review, approve, or reject pending requests, and generates a team-wide leave calendar displaying verified approved leaves.

---

## 🛠️ Main OOP Concepts Demonstrated
* **Encapsulation**: Private attributes with public getters/setters (`Employee`, `LeaveRequest`).
* **Inheritance**: `Manager` extends `Employee` to reuse properties and add managerial capabilities.
* **Polymorphism**: Method overriding (`getRole()`) and dynamic method dispatch.
* **Abstraction**: `LeaveApprover` interface declaring approval workflow contracts (`approveLeave`, `rejectLeave`).
* **Automatic Duration Calculation**: Uses Java `LocalDate` and `ChronoUnit.DAYS.between(startDate, endDate) + 1`.

---

## 📁 Repository Structure
```text
.
├── Main.java                 # Entry point & interactive console user interface
├── Employee.java             # Base employee model
├── Manager.java              # Manager subclass extending Employee
├── LeaveApprover.java        # Interface for leave approval actions
├── LeaveRequest.java         # Leave request model with automatic duration calculation
├── LeaveCalendar.java        # Displays team-wide approved leave calendar
├── LeaveManagementSystem.java# Central controller managing data collections
├── LeaveType.java            # Enum: CASUAL, SICK, ANNUAL, EMERGENCY
├── LeaveStatus.java          # Enum: PENDING, APPROVED, REJECTED
└── README.md                 # Project documentation
```

---

## 🚀 How to Run the Project

### 1. Clone the repository
```bash
git clone https://github.com/milanbino/Leave-Management-System.git
cd Leave-Management-System
```

### 2. Compile Java Source Files
```bash
javac *.java
```

### 3. Execute the Application
```bash
java Main
```

---

## 👥 Pre-Loaded Demo Users

### Employees
* **101**: Esther (IT)
* **102**: Anu (IT)
* **103**: Rahul (IT)
* **104**: David (HR)
* **105**: Sophia (Finance)

### Managers
* **201**: John (IT Manager)
* **202**: Sarah (HR Manager)
