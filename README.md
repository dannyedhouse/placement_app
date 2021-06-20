# Placement App
Second year university group project - search for placement roles sourced via locally stored CSV file.

- Allows user to create an account (username only for test purposes), login and search for placement roles - both summer and placement years.
- They can also set their preferences, manage favourited placement roles and edit their profile.

---
Data is stored in a local SQLite database on the device/emulator for offline storage and syncs with a MySQL remote database to sync between devices.

To view the database (in Android Studio - Device File Explorer):
*Data -> Data -> com.example.placementapplication -> databases -> placements.db*

**Database structure:**

![Database design](https://i.imgur.com/PY9Mhnz.jpeg)

---
## Server-Side MySQL database
XAMPP was used to mock a server-side MySQL database on the localhost. A database called 'placements' with the 4 specified tables must be created, which can be done by creating the database and importing the 'placements.sql' file in phpMyAdmin. Then, changing the *URL_SAVE_NAME* variable in the *Sync* class to the local IP address, and storing the syncDB PHP script in the htdocs folder will allow the two local and server-side databases to sync.
