# ⚡ Quick Deploy Guide - Railway

## 🎯 Fastest Way to Deploy Your App

This guide will get your Student Course Management System live in under 10 minutes!

## 📋 What You'll Get

- ✅ Live web application
- ✅ MySQL database
- ✅ Swagger API documentation
- ✅ SSL certificate
- ✅ Custom domain (optional)
- ✅ Automatic deployments

## 🚀 Step-by-Step Deployment

### 1. Prepare Your Repository
Make sure your code is pushed to GitHub with all the files I just created:
- `Dockerfile`
- `railway.json`
- `application-prod.properties`
- `DEPLOYMENT.md`

### 2. Sign Up for Railway
1. Go to [railway.app](https://railway.app)
2. Click "Sign Up" and connect your GitHub account
3. Railway will automatically detect your repositories

### 3. Create New Project
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Choose your repository
4. Railway will start building automatically

### 4. Add MySQL Database
1. In your Railway project, click "New"
2. Select "Database" → "MySQL"
3. Railway will create a MySQL instance
4. Copy the connection details (you'll need them in the next step)

### 5. Configure Environment Variables
1. Go to your project's "Variables" tab
2. Add these environment variables:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://your-railway-mysql-host:3306/railway
DATABASE_USERNAME=root
DATABASE_PASSWORD=your-railway-mysql-password
PORT=8080
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

**Replace the database values with what Railway provided in step 4.**

### 6. Deploy!
1. Railway will automatically redeploy when you add variables
2. Wait for the build to complete (usually 2-3 minutes)
3. Click on your service to get the live URL

## 🎉 You're Live!

Your app will be available at: `https://your-app-name.railway.app`

### Test Your Deployment:
1. **Main App**: Visit the URL
2. **Admin Login**: Use `admin` / `admin123`
3. **Student Login**: Use any student credentials from your data
4. **API Docs**: Visit `/swagger-ui.html`

## 🔧 Custom Domain (Optional)
1. Go to your Railway project settings
2. Click "Custom Domains"
3. Add your domain and follow the DNS instructions

## 📊 Monitoring
- **Logs**: View real-time logs in Railway dashboard
- **Metrics**: Monitor CPU, memory, and network usage
- **Deployments**: Automatic deployments on every Git push

## 💰 Cost
- **Free Tier**: $0/month (limited usage)
- **Paid**: $5-20/month depending on usage

## 🆘 Need Help?
- Check the full `DEPLOYMENT.md` guide
- Railway has excellent documentation
- Your app logs are available in the Railway dashboard

---

## 🎯 Alternative: One-Click Deploy

If you prefer, you can also use the Railway CLI:

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway up
```

---

**That's it! Your full-stack Spring Boot application is now live on the internet! 🌐**
