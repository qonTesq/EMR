# EMR

A CLI program for managing electronic medical records.

## Features

- CLI interface for user interaction.
- MySQL database integration.
- Menu-driven navigation between different entities.

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

### Patients Table

```sql
CREATE TABLE `patients` (
	`mrn` int NOT NULL,
	`fname` text NOT NULL,
	`lname` text NOT NULL,
	`dob` date NOT NULL,
	`address` text NOT NULL,
	`state` text NOT NULL,
	`city` text NOT NULL,
	`zip` int NOT NULL,
	`insurance` text NOT NULL,
	PRIMARY KEY (`mrn`),
	UNIQUE KEY `mrn_UNIQUE` (`mrn`)
);
```

### Patient History Table

```sql
CREATE TABLE `patient_history` (
	`id` varchar(255) NOT NULL,
	`patientID` int NOT NULL,
	`procedureID` text NOT NULL,
	`date` date NOT NULL,
	`billing` double NOT NULL,
	`doctor` text NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `id_UNIQUE` (`id`)
);
```

### Procedures Table

```sql
CREATE TABLE `procedures` (
	`id` varchar(255) NOT NULL,
	`name` text NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `Procedure ID_UNIQUE` (`id`)
);
```

## Running the Program

1. Compile the source code:

   ```
   javac -cp "lib/mysql-connector-j-9.4.0.jar" -d bin src/main/**/*.java src/main/*.java
   ```

2. Run the program:
   ```
   java -cp "bin:lib/mysql-connector-j-9.4.0.jar" main.App
   ```

## Usage

The program provides a main menu where you can choose which entity to manage:

1. **Patients**: Manage patient records with full CRUD operations
2. **Patient History**: Manage patient procedure history records
3. **Procedures**: Manage available medical procedures

Each entity has its own submenu with the following operations:

- Create (with validation for all required fields)
- Read (single record or all records)
- Update (modify existing records)
- Delete (remove records with confirmation)

When creating records, all fields are validated to ensure no empty values are accepted.
