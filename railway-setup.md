# 🚀 Railway Setup Guide

## Step 1: Add MySQL Database
1. In Railway dashboard, click "New"
2. Select "Database" → "MySQL"
3. Wait for it to be created
4. Copy the connection details from the database service

## Step 2: Environment Variables
Add these to your Railway project's "Variables" tab:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://your-mysql-host:3306/railway
DATABASE_USERNAME=root
DATABASE_PASSWORD=your-mysql-password
PORT=8080
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

**Replace the database values with what Railway provided.**

## Step 3: Deploy
Railway will automatically redeploy when you add variables.

## Step 4: Test
1. Visit your app URL
2. Test admin login: admin/admin123
3. Test student login with any student credentials
4. Check API docs at /swagger-ui.html

## Troubleshooting
- Check logs in Railway dashboard
- Ensure all environment variables are set correctly
- Make sure database is accessible
