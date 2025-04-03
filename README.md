EduMentor Learning and Mentorship Platform

EduMentor is an Android application designed to empower students, mentors, and administrators by providing a robust, interactive learning and mentorship environment. The platform integrates modern UI design with real-time data management to deliver a seamless educational experience.

Features
User Authentication:
Secure sign-in options using Email/Password, Google, Facebook, and Apple. Integration with Firebase Authentication ensures safe and reliable user management.

Course Management:
Administrators can create, update, and manage courses. Each course includes detailed information such as title, category (e.g., "Basic Level | 20 Videos"), course type (trending or recommended), rating, and price.

Real-Time Data Updates:
Courses are stored and synchronized using Firebase Firestore. The application merges default courses with newly created courses from Firestore, ensuring that updates (such as new ratings) are reflected instantly.

Dynamic Dashboards:
Users enjoy a responsive UI with dynamic dashboards that display trending, recommended, and enrolled courses using RecyclerView. The layout adapts to various screen sizes and orientations for an optimal viewing experience.

Session Booking:
Users can book sessions directly from the course listings. Clicking on course prices initiates a booking process, streamlining the enrollment process.

Technology Stack
Programming Language: Java

Platform: Android SDK

Authentication: Firebase Authentication

Database: Firebase Firestore

Third-Party Integrations:

Google Sign-In API

Facebook SDK

UI Components: Material Components, CardView, RecyclerView

Project Structure
Activities:

LoginActivity: Handles multi-method user sign-in and authentication.

CreateCourseActivity: Allows admins to create new courses and save them to Firestore.

AdminCoursesActivity: Displays all courses (default and dynamically fetched) with options to manage them.

BookSessionActivity: Enables users to book sessions for selected courses.

Adapters:
Custom adapters are implemented (e.g., RecommendedCoursesAdapter) to bind course data to UI components and manage real-time updates.

Models:
Data models such as RecommendedCourse, TrendingCourse, and EnrolledCourse define the structure of course-related data.
