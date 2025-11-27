# EMR

A CLI program for managing electronic medical records.

## Features

- CLI interface for user interaction
- MySQL database integration
- Menu-driven navigation between different entities
- Full CRUD operations for all entities
- Input validation and foreign key constraint checking
- Service layer for business logic
- Clean DAO pattern with BaseDAO interface

## Project Structure

```
src/main/
├── App.java                    # Application entry point
├── cli/                        # Command-line interface classes
│   ├── CLI.java                # Base CLI with common utilities
│   ├── MainCLI.java            # Main menu navigation
│   ├── DoctorsCLI.java         # Doctor management
│   ├── PatientsCLI.java        # Patient management
│   ├── ProceduresCLI.java      # Procedure management
│   └── PatientHistoryCLI.java  # Patient history management
├── config/
│   └── DatabaseConfig.java     # Database configuration
├── dao/                        # Data Access Objects
│   ├── BaseDAO.java            # Generic DAO interface
│   ├── DoctorDAO.java
│   ├── PatientDAO.java
│   ├── ProcedureDAO.java
│   └── PatientHistoryDAO.java
├── exceptions/                 # Custom exceptions
│   ├── EMRException.java
│   ├── DatabaseException.java
│   ├── EntityNotFoundException.java
│   └── ValidationException.java
├── models/                     # Entity models
│   ├── Doctor.java
│   ├── Patient.java
│   ├── Procedure.java
│   └── PatientHistory.java
├── service/                    # Business logic layer
│   ├── DoctorService.java
│   ├── PatientService.java
│   ├── ProcedureService.java
│   └── PatientHistoryService.java
├── util/
│   └── Database.java           # Database connection management
└── validation/                 # Input validation
    ├── ValidationUtils.java
    ├── DoctorValidator.java
    ├── PatientValidator.java
    ├── ProcedureValidator.java
    └── PatientHistoryValidator.java
```

## Environment Configuration

Before running the EMR application, you must configure the database connection settings. The application uses environment variables to securely store database credentials and connection details.

### Setting Up Environment Variables

The application requires three environment variables to connect to your MySQL database:

- `EMR_DB_URL`: The JDBC connection string for your MySQL database
- `EMR_DB_USER`: Your MySQL database username
- `EMR_DB_PASSWORD`: Your MySQL database password

### Configuration Methods

#### Option 1: Using a .env file (Recommended for VS Code)

1. Create a file named `.env` in the root directory of the project
2. Add the following content, replacing the placeholder values with your actual database information:

   ```
   EMR_DB_URL=jdbc:mysql://localhost:3306/your_database_name
   EMR_DB_USER=your_mysql_username
   EMR_DB_PASSWORD=your_mysql_password
   ```

3. The application will automatically load these variables from the `.env` file

#### Option 2: System Environment Variables

Alternatively, you can set these as system environment variables:

- On Windows (PowerShell):

  ```
  $env:EMR_DB_URL="jdbc:mysql://localhost:3306/your_database_name"
  $env:EMR_DB_USER="your_mysql_username"
  $env:EMR_DB_PASSWORD="your_mysql_password"
  ```

- On Linux/macOS:
  ```
  export EMR_DB_URL=jdbc:mysql://localhost:3306/your_database_name
  export EMR_DB_USER=your_mysql_username
  export EMR_DB_PASSWORD=your_mysql_password
  ```

### Database Connection Details

- **Host**: `localhost` (change if your MySQL server is on a different machine)
- **Port**: `3306` (default MySQL port)
- **Database Name**: Replace `your_database_name` with the actual name of your EMR database
- **Username/Password**: Your MySQL credentials with appropriate permissions to read/write to the EMR tables

### Security Note

Never commit the `.env` file to version control. Add `.env` to your `.gitignore` file to keep your database credentials secure.

## Database Setup

Make sure you have a MySQL database with the following tables. All fields are required for each entity:

### Doctors Table

```sql
CREATE TABLE `doctors` (
  `id` varchar(25) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);
```

### Patients Table

```sql
CREATE TABLE `patients` (
  `mrn` int NOT NULL,
  `fname` text NOT NULL,
  `lname` text NOT NULL,
  `dob` text NOT NULL,
  `address` text NOT NULL,
  `state` text NOT NULL,
  `city` text NOT NULL,
  `zip` int NOT NULL,
  `insurance` text NOT NULL,
  `email` text NOT NULL,
  PRIMARY KEY (`mrn`),
  UNIQUE KEY `mrn_UNIQUE` (`mrn`)
);
```

### Procedures Table

```sql
CREATE TABLE `procedures` (
  `id` varchar(25) NOT NULL,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `duration` int NOT NULL,
  `doctorId` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `doctorId_fk_idx` (`doctorId`),
  CONSTRAINT `proc_to_doc_fk` FOREIGN KEY (`doctorId`) REFERENCES `doctors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
```

### Patient History Table

```sql
CREATE TABLE `patient_history` (
  `id` varchar(25) NOT NULL,
  `patientId` int NOT NULL,
  `procedureId` varchar(25) NOT NULL,
  `date` date NOT NULL,
  `billing` double NOT NULL,
  `doctorId` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `procedureId_idx` (`procedureId`),
  KEY `doctorId_idx` (`doctorId`),
  KEY `patientId_idx` (`patientId`),
  CONSTRAINT `ph_to_doc_fk` FOREIGN KEY (`doctorId`) REFERENCES `doctors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ph_to_pat_fk` FOREIGN KEY (`patientId`) REFERENCES `patients` (`mrn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ph_to_proc_fk` FOREIGN KEY (`procedureId`) REFERENCES `procedures` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
```

## Running the Program

1. Compile the source code:

   ```
   javac -cp "lib/mysql-connector-j-9.4.0.jar" -d bin src/main/**/*.java src/main/*.java
   ```

2. Run the program:
   ```
   java -cp "bin;lib/mysql-connector-j-9.4.0.jar" main.App
   ```

   On Linux/macOS use `:` instead of `;`:
   ```
   java -cp "bin:lib/mysql-connector-j-9.4.0.jar" main.App
   ```

## Usage

The program provides a main menu where you can choose which entity to manage:

1. **Doctors**: Manage doctor records
2. **Patients**: Manage patient records
3. **Procedures**: Manage available medical procedures
4. **Patient History**: Manage patient procedure history records

Each entity has its own submenu with the following operations:

- **Create**: Add new records with validation for all required fields
- **Read**: View a single record by ID or all records
- **Update**: Modify existing records with field-by-field updates
- **Delete**: Remove records with confirmation prompt

### Validation Features

- All required fields are validated before submission
- Foreign key references are verified (e.g., doctor must exist before creating a procedure)
- ID length limits are enforced (max 25 characters)
- Date formats are validated (yyyy-MM-dd)
- Email format validation for patients
- Duplicate ID/MRN detection