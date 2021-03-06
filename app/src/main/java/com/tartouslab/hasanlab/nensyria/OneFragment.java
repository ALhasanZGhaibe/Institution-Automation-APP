package com.tartouslab.hasanlab.nensyria;


import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> courseImageResources;
    DatabaseHandler db;
    List<List<String>> courseParamList;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> coursesListMap;

    // url to get all courses list
    private static String url_all_courses = "http://192.168.1.103/android_connect/codes/get_all_course.php";

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
    private static final String TAG_DESC = "description";
    private static final String TAG_CERT = "certificate";
    private static final String TAG_ICON = "icon";

    // courses JSONArray
    JSONArray coursesJson = null;

    List<Course> coursesList;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        db = new DatabaseHandler(getActivity());
        // Reading all courses
        Log.d("Reading: ", "Reading all coursesList From Fragment..");
        coursesList = db.getAllCourses();
        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        // preparing list data
        //prepareListData(coursesList);

        listAdapter =
                new ExpandableListAdapter(getActivity(), coursesList);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        /* Inflate the layout for this fragment
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });*/

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (i1 == 0) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                    ad.setTitle(courseParamList.get(i).get(0));
                    ad.setMessage(courseParamList.get(i).get(7));
                    ad.create().show();
                }
                return false;
            }
        });
        new LoadAllProducts().execute();
        return rootView;
    }

    /*
    * Preparing the list data
    *//* no-op */
    private void prepareListData(List<Course> courses) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        for (Course course : courses)
            listDataHeader.add(course.getName());

        // Adding child data
        List<String> detailsList = new ArrayList<String>();
        detailsList.add("Course Description...");
        detailsList.add("Participate with Whatsapp");
        detailsList.add("Participate with a Phone Call");
        detailsList.add("Share with a Friend");
        detailsList.add("Send FeedBack");
        for (int i = 0; i < courses.size(); i++)
            listDataChild.put(listDataHeader.get(i), detailsList);


        courseParamList = new ArrayList<List<String>>();
        for (Course course : courses) {
            List<String> cPRow = new ArrayList<String>();
            cPRow.add(course.getName());
            cPRow.add(course.gethrs());
            cPRow.add(course.get_hrsPd());
            cPRow.add(course.get_dPw());
            cPRow.add(course.get_days());
            cPRow.add(course.get_startDate());
            cPRow.add(course.get_hotORnot());
            cPRow.add(course.get_description());
            cPRow.add(course.get_certificate());
            cPRow.add(course.get_icon());
            courseParamList.add(cPRow);
        }


    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            pDialog = new ProgressDialog(AllProductsActivity.this);
            pDialog.setMessage("Loading courses. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }

        /**
         * getting All courses from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            coursesListMap = new ArrayList<HashMap<String, String>>();
            // getting JSON string from URL
            JSONObject json = null;
            try {
                json = jParser.makeHttpRequest(url_all_courses, "GET", params);
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Check your log cat for JSON reponse
            if (json != null) Log.d("All Courses: ", json.toString());
            if (json != null)
                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // courses found
                        // Getting Array of Products
                        coursesJson = json.getJSONArray(TAG_COURSES);

                        // looping through All Products
                        for (int i = 0; i < coursesJson.length(); i++) {
                            JSONObject c = coursesJson.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString(TAG_ID);
                            String name = c.getString(TAG_NAME);
                            String hrs = c.getString(TAG_HRS);
                            String hpd = c.getString(TAG_HPD);
                            String dpw = c.getString(TAG_DPW);
                            String days = c.getString(TAG_DAYS);
                            String sdate = c.getString(TAG_SDATE);
                            String hot = c.getString(TAG_HOT);
                            String desc = c.getString(TAG_DESC);
                            String cert = c.getString(TAG_CERT);
                            String icon = c.getString(TAG_ICON);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID, id);
                            map.put(TAG_NAME, name);
                            map.put(TAG_HRS, hrs);
                            map.put(TAG_HPD, hpd);
                            map.put(TAG_DPW, dpw);
                            map.put(TAG_DAYS, days);
                            map.put(TAG_SDATE, sdate);
                            map.put(TAG_HOT, hot);
                            map.put(TAG_DESC, desc);
                            map.put(TAG_CERT, cert);
                            map.put(TAG_ICON, icon);

                            // adding HashList to ArrayList
                            coursesListMap.add(map);
                        }
                    } else {
                        // no courses found
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all courses
            //pDialog.dismiss();
            if (coursesJson != null) {
                // updating UI from Background Thread
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        for (int i = 0; i < coursesList.size(); i++) {
                            Log.d("Deleting: ", "Deleting coursesList " + i);
                            db.deleteCourse(coursesList.get(i));
                        }
                        for (int i = 0; i < coursesListMap.size(); i++) {
                            String id = coursesListMap.get(i).get(TAG_ID);
                            String name = coursesListMap.get(i).get(TAG_NAME);
                            String hrs = coursesListMap.get(i).get(TAG_HRS);
                            String hpd = coursesListMap.get(i).get(TAG_HPD);
                            String dpw = coursesListMap.get(i).get(TAG_DPW);
                            String days = coursesListMap.get(i).get(TAG_DAYS);
                            String sdate = coursesListMap.get(i).get(TAG_SDATE);
                            String hot = coursesListMap.get(i).get(TAG_HOT);
                            String desc = coursesListMap.get(i).get(TAG_DESC);
                            String cert = coursesListMap.get(i).get(TAG_CERT);
                            String icon = coursesListMap.get(i).get(TAG_ICON);
                            Log.d("Inserting: ", "From Internet");
                            db.addCourse(new Course(name, hrs, hpd, dpw, days, sdate, hot, desc, cert, icon));
                        }
                        // Reading all courses
                        Log.d("Reading: ", "Reading all coursesList From Fragment..");
                        coursesList = db.getAllCourses();
                        listAdapter =
                                new ExpandableListAdapter(getActivity(), coursesList);
                        listAdapter.notifyDataSetChanged();
                        expListView.setAdapter(listAdapter);
                        expListView.invalidate();
                    }
                });
            }


        }

    }


}