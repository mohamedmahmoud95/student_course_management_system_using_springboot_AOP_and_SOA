# Database Setup Guide

## MySQL Database Configuration

This application has been configured to use MySQL instead of H2 in-memory database.

### Prerequisites

1. **Install MySQL Server**
   - Download and install MySQL Server from [MySQL Official Website](https://dev.mysql.com/downloads/mysql/)
   - Or use a package manager:
     ```bash
     # Ubuntu/Debian
     sudo apt-get install mysql-server
     
     # macOS (using Homebrew)
     brew install mysql
     
     # Windows
     # Download and install from MySQL website
     ```

2. **Start MySQL Service**
   ```bash
   # Ubuntu/Debian
   sudo systemctl start mysql
   
   # macOS
   brew services start mysql
   
   # Windows
   # MySQL service should start automatically after installation
   ```

3. **Secure MySQL Installation**
   ```bash
   sudo mysql_secure_installation
   ```

### Database Configuration

The application uses different profiles for different environments:

#### Development Environment (Default)
- **Database**: `scms_db_dev`
- **Profile**: `dev`
- **DDL Auto**: `update` (creates/updates tables automatically)
- **SQL Logging**: Enabled

#### Production Environment
- **Database**: `scms_db_prod`
- **Profile**: `prod`
- **DDL Auto**: `validate` (validates schema only)
- **SQL Logging**: Disabled

### Configuration Files

1. **application.properties** - Default configuration
2. **application-dev.properties** - Development environment
3. **application-prod.properties** - Production environment

### Running the Application

#### Development Mode (Default)
```bash
mvn spring-boot:run
```

#### Production Mode
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

### Environment Variables (Production)

For production deployment, set these environment variables:

```bash
export DB_USERNAME=your_mysql_username
export DB_PASSWORD=your_mysql_password
```

### Database Connection Details

- **Host**: localhost
- **Port**: 3306
- **Driver**: com.mysql.cj.jdbc.Driver
- **Timezone**: UTC

### Troubleshooting

1. **Connection Refused**
   - Ensure MySQL service is running
   - Check if port 3306 is available

2. **Access Denied**
   - Verify MySQL username and password
   - Ensure user has proper permissions

3. **Database Not Found**
   - The application will create the database automatically if it doesn't exist
   - Ensure MySQL user has CREATE DATABASE privileges

### Manual Database Creation (Optional)

If you prefer to create the database manually:

```sql
CREATE DATABASE scms_db_dev;
CREATE DATABASE scms_db_prod;
```

### Migration from H2

If you were previously using H2 database:
1. Export your data from H2 (if needed)
2. The application will create new tables in MySQL automatically
3. Re-import your data if necessary

### Security Notes

- Change default MySQL root password
- Use environment variables for production credentials
- Enable SSL for production connections
- Restrict database user permissions appropriately
