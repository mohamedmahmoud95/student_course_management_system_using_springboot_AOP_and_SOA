# 🚀 Deployment Guide - Student Course Management System

This guide provides multiple deployment options for your Spring Boot application.

## 📋 Prerequisites

- GitHub repository with your code
- MySQL database (managed or self-hosted)
- Java 17 runtime

## 🎯 Deployment Options

### Option 1: Railway (Recommended - Easiest)

**Railway** is the easiest option for full-stack applications.

#### Steps:
1. **Sign up** at [railway.app](https://railway.app)
2. **Connect your GitHub repository**
3. **Create a new project** and select your repository
4. **Add MySQL database**:
   - Click "New" → "Database" → "MySQL"
   - Railway will provide connection details
5. **Configure environment variables**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://your-railway-mysql-url
   DATABASE_USERNAME=your-username
   DATABASE_PASSWORD=your-password
   PORT=8080
   ```
6. **Deploy** - Railway will automatically build and deploy your app

#### Benefits:
- ✅ Free tier available
- ✅ Automatic deployments
- ✅ Built-in MySQL database
- ✅ SSL certificates included
- ✅ Custom domains supported

---

### Option 2: Google Cloud Platform (GCP)

#### A. Google App Engine

1. **Install Google Cloud SDK**
2. **Initialize your project**:
   ```bash
   gcloud init
   gcloud config set project YOUR_PROJECT_ID
   ```
3. **Create Cloud SQL instance**:
   ```bash
   gcloud sql instances create scms-db \
     --database-version=MYSQL_8_0 \
     --tier=db-f1-micro \
     --region=us-central1
   ```
4. **Create database**:
   ```bash
   gcloud sql databases create scms_db --instance=scms-db
   ```
5. **Update app.yaml** with your Cloud SQL connection details
6. **Deploy**:
   ```bash
   gcloud app deploy
   ```

#### B. Google Cloud Run

1. **Build and push Docker image**:
   ```bash
   gcloud builds submit --tag gcr.io/YOUR_PROJECT/scms
   ```
2. **Deploy to Cloud Run**:
   ```bash
   gcloud run deploy scms \
     --image gcr.io/YOUR_PROJECT/scms \
     --platform managed \
     --region us-central1 \
     --allow-unauthenticated
   ```

---

### Option 3: Heroku

1. **Install Heroku CLI**
2. **Login and create app**:
   ```bash
   heroku login
   heroku create your-app-name
   ```
3. **Add MySQL addon**:
   ```bash
   heroku addons:create jawsdb:kitefin
   ```
4. **Configure environment**:
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   ```
5. **Deploy**:
   ```bash
   git push heroku main
   ```

---

## 🔧 Environment Variables

Set these environment variables in your deployment platform:

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring profile | `prod` |
| `DATABASE_URL` | MySQL connection URL | `jdbc:mysql://host:port/db` |
| `DATABASE_USERNAME` | Database username | `root` |
| `DATABASE_PASSWORD` | Database password | `your-password` |
| `PORT` | Application port | `8080` |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |

## 🗄️ Database Setup

### For Production Database:

1. **Create database**:
   ```sql
   CREATE DATABASE scms_db_prod;
   ```

2. **Create user** (if needed):
   ```sql
   CREATE USER 'scms_user'@'%' IDENTIFIED BY 'secure_password';
   GRANT ALL PRIVILEGES ON scms_db_prod.* TO 'scms_user'@'%';
   FLUSH PRIVILEGES;
   ```

3. **Update connection details** in your deployment platform

## 🔍 Post-Deployment Verification

After deployment, verify:

1. **Application loads**: Visit your app URL
2. **Database connection**: Check logs for database errors
3. **Admin login**: Test admin credentials
4. **Student login**: Test student functionality
5. **API documentation**: Visit `/swagger-ui.html`

## 📱 Application URLs

After deployment, your app will be available at:

- **Main Application**: `https://your-app-url.com`
- **Swagger API Docs**: `https://your-app-url.com/swagger-ui.html`
- **API Endpoints**: `https://your-app-url.com/api/*`

## 🛠️ Troubleshooting

### Common Issues:

1. **Database Connection Failed**:
   - Check DATABASE_URL format
   - Verify database credentials
   - Ensure database is accessible from deployment platform

2. **Port Issues**:
   - Set PORT environment variable
   - Ensure platform allows port 8080

3. **Build Failures**:
   - Check Java version (requires Java 17)
   - Verify Maven dependencies
   - Check Dockerfile syntax

### Logs:
- **Railway**: View logs in Railway dashboard
- **GCP**: `gcloud app logs tail` or Cloud Console
- **Heroku**: `heroku logs --tail`

## 🔒 Security Considerations

1. **Use strong passwords** for database and admin accounts
2. **Enable HTTPS** (automatic on most platforms)
3. **Set up proper CORS** if needed
4. **Regular security updates**
5. **Database backups**

## 💰 Cost Estimation

### Railway:
- Free tier: $0/month (limited usage)
- Paid: $5-20/month depending on usage

### Google Cloud:
- App Engine: $0-50/month (free tier available)
- Cloud SQL: $7-50/month depending on instance

### Heroku:
- Free tier: $0/month (limited)
- Paid: $7-25/month depending on dyno size

---

## 🎉 Success!

Once deployed, your Student Course Management System will be accessible worldwide with:
- ✅ Full-stack functionality
- ✅ MySQL database
- ✅ Swagger API documentation
- ✅ Responsive web interface
- ✅ Admin and student portals

Choose the deployment option that best fits your needs and budget!
