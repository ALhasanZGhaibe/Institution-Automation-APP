package com.tartouslab.hasanlab.nensyria;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tartouslab.hasanlab.nensyria.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COURSES = "courses";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_HRS = "hours";
    private static final String TAG_HPD = "hours_per_day";
    private static final String TAG_DPW = "days_per_week";
    private static final String TAG_DAYS = "days";
    private static final String TAG_SDATE = "start_date";
    private static final String TAG_HOT = "hot_or_not";
    private static final String TAG_CERT = "certificate";
    private static final String TAG_ICON = "icon";
    // url to get all courses list
    private static String url_all_courses = "https://api.androidhive.info/android_connect/get_all_courses.php";
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> coursesListMap;
    // courses JSONArray
    JSONArray courses = null;
    List<Course> coursesList;
    //user info...
    String username="";
    String email="";
    //SessionDATA
    SessionManager session;
    HashMap<String, String> user;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);


        DatabaseHandler db = new DatabaseHandler(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        //session.checkLogin();
        if(session.isLoggedIn()) {
            // get user data from session
            user = session.getUserDetails();

            // name
            username = user.get(SessionManager.KEY_NAME);

            // email
            email = user.get(SessionManager.KEY_EMAIL);
        }

        // Reading all courses
        Log.d("Reading: ", "Reading all courses..");
        coursesList = db.getAllCourses();

        /**
         * CRUD Operations
         * */
        Log.d("Insert: ", "Trying to Insert..");
        // Insert Courses Here

        addCourseToDB(coursesList,db,new Course("Android Development","24 Hrs", "2 Hrs/D","3 D/W","16 D","30/12/2017","not","Course Description Here...","cert...","an"));
        addCourseToDB(coursesList,db,new Course("Ethical Hacking","27 Hrs", "3 Hrs/D","3 D/W","9 D","22/12/2017","hot","Course Description Here...","cert...","eh"));
        addCourseToDB(coursesList,db,new Course("Course 3", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","not","Course Description Here...","cert...","DEFAULT"));
        addCourseToDB(coursesList,db,new Course("Course 4", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","not","Course Description Here...","cert...","DEFAULT"));
        addCourseToDB(coursesList,db,new Course("Course 6", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","not","Course Description Here...","cert...","DEFAULT"));
        addCourseToDB(coursesList,db,new Course("Course 7", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","hot","Course Description Here...","cert...","DEFAULT"));
        addCourseToDB(coursesList,db,new Course("Course 8", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","not","Course Description Here...","cert...","DEFAULT"));
        addCourseToDB(coursesList,db,new Course("Course 9", "0 Hrs", "0 Hrs/D","0 D/W","0 D","22/12/2017","not","Course Description Here...","cert...","DEFAULT"));
        // Reading all courses
        Log.d("Reading: ", "Reading all courses..");
        coursesList = db.getAllCourses();

        for (Course cn : coursesList) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Hours: " + cn.gethrs()
                    +" ,Hours Per Day: " + cn.get_hrsPd()+" ,Days Per Week: " + cn.get_dPw()+" ,Days: " + cn.get_days()
                    +" ,Start Date: " + cn.get_startDate()+" ,Hot or Not: " + cn.get_hotORnot()+" ,Description: " + cn.get_description()
                    +" ,Cert: " + cn.get_certificate()+" ,Icon: " + cn.get_icon();
            // Writing Courses to log
            Log.d("Name: ", log);
        }
        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(session.isLoggedIn()) {
            getSupportActionBar().setTitle(username+", Welcome to NEN ");
            getSupportActionBar().setIcon(R.drawable.profile_pictures);
        }else getSupportActionBar().setTitle("NEN Syria");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addCourseToDB(List<Course> courses,DatabaseHandler db,Course course) {
        if(!Crs_alredy_exists(courses,course.getName())) {
            Log.d("Insert: ", "Inserting ..");
            db.addCourse(course);
        }else Log.d("Insert: ", "Already Exists In The Table..");
    }

    private Boolean Crs_alredy_exists(List<Course> courses,String name) {
        Boolean exists = false;
        for(Course course : courses){
            if(course.getName().equals(name))
                exists = true;
        }
        return exists;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Courses");
        adapter.addFragment(new TwoFragment(), "Certificates");
        adapter.addFragment(new ThreeFragment(), "Solutions");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
