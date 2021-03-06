package com.tartouslab.hasanlab.nensyria;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasanzgh on 22/11/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "nenDatabase";

    // Courses table name
    private static final String TABLE_COURSE = "courses";

    // Courses Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HRS = "hrs";
    private static final String KEY_HRS_P_DAY = "hrsPd";
    private static final String KEY_DAYS_P_WEEK = "dPw";
    private static final String KEY_DAYS = "days";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_HOT_OR_NOT = "hotORnot";
    private static final String KEY_DESC = "description";
    private static final String KEY_CERT = "certificate";
    private static final String KEY_ICON= "icon";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSE_TABLE = "CREATE TABLE " + TABLE_COURSE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_HRS + " TEXT," + KEY_HRS_P_DAY + " TEXT," +
                KEY_DAYS_P_WEEK + " TEXT," +KEY_DAYS + " TEXT," +
                KEY_START_DATE + " TEXT," + KEY_HOT_OR_NOT + " TEXT," +
                KEY_DESC + " TEXT,"+KEY_CERT + " TEXT,"+KEY_ICON + " TEXT"+ ")";
        db.execSQL(CREATE_COURSE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);

        // Create tables again
        onCreate(db);
    }

    // Adding new course
    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, course.getName());
        values.put(KEY_HRS, course.gethrs());
        values.put(KEY_HRS_P_DAY, course.get_hrsPd());
        values.put(KEY_DAYS_P_WEEK , course.get_dPw());
        values.put(KEY_DAYS, course.get_days());
        values.put(KEY_START_DATE, course.get_startDate());
        values.put(KEY_HOT_OR_NOT, course.get_hotORnot());
        values.put(KEY_DESC, course.get_description());
        values.put(KEY_CERT, course.get_certificate());
        values.put(KEY_ICON, course.get_icon());

        // Inserting Row
        db.insert(TABLE_COURSE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single course
    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COURSE, new String[] { KEY_ID,
                        KEY_NAME, KEY_HRS, KEY_HRS_P_DAY, KEY_DAYS_P_WEEK,
                        KEY_DAYS, KEY_START_DATE, KEY_HOT_OR_NOT, KEY_DESC ,KEY_CERT,KEY_ICON }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Course course = new Course(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)
                , cursor.getString(6), cursor.getString(7), cursor.getString(8),cursor.getString(9),cursor.getString(10));
        // return course
        return course;
    }

    // Getting All course
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<Course>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COURSE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setID(Integer.parseInt(cursor.getString(0)));
                course.setName(cursor.getString(1));
                course.sethrs(cursor.getString(2));
                course.set_hrsPd(cursor.getString(3));
                course.set_dPw(cursor.getString(4));
                course.set_days(cursor.getString(5));
                course.set_startDate(cursor.getString(6));
                course.set_hotORnot(cursor.getString(7));
                course.set_description(cursor.getString(8));
                course.set_certificate(cursor.getString(9));
                course.set_icon(cursor.getString(10));
                // Adding course to list
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        // return course list
        return courseList;
    }

    // Getting courses count
    public int getCoursesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COURSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single course
    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, course.getName());
        values.put(KEY_HRS, course.gethrs());
        values.put(KEY_HRS_P_DAY, course.get_hrsPd());
        values.put(KEY_DAYS_P_WEEK, course.get_dPw());
        values.put(KEY_DAYS, course.get_days());
        values.put(KEY_START_DATE, course.get_startDate());
        values.put(KEY_HOT_OR_NOT, course.get_hotORnot());
        values.put(KEY_DESC, course.get_description());

        // updating row
        return db.update(TABLE_COURSE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(course.getID()) });
    }

    // Deleting single course
    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSE, KEY_ID + " = ?",
                new String[] { String.valueOf(course.getID()) });
        db.close();
    }
}
