-- Clean Database Script
-- This script removes all dummy data and keeps only essential admin accounts

-- Clear all data in reverse dependency order
DELETE FROM grades;
DELETE FROM enrollments;
DELETE FROM notifications;
DELETE FROM admin_notifications;
DELETE FROM students;
DELETE FROM courses;
DELETE FROM administrators;

-- Reset auto-increment counters
ALTER TABLE grades AUTO_INCREMENT = 1;
ALTER TABLE enrollments AUTO_INCREMENT = 1;
ALTER TABLE notifications AUTO_INCREMENT = 1;
ALTER TABLE admin_notifications AUTO_INCREMENT = 1;
ALTER TABLE students AUTO_INCREMENT = 1;
ALTER TABLE courses AUTO_INCREMENT = 1;
ALTER TABLE administrators AUTO_INCREMENT = 1;
