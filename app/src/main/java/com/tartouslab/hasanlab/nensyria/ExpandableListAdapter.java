package com.tartouslab.hasanlab.nensyria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ExpandableListAdapter extends ExpandableListAdapter {

    private Context _context;
    private int[] _courseToolsImageResourse;
    private String networkImageUrl = "http://192.168.1.103/android_connect/photos/";
    private List<Course> _courselist;
    private List<String> _detailsList;

    public ExpandableListAdapter(Context context, List<Course> courseList) {
        this._context = context;
        this._courselist = courseList;
        _detailsList = new ArrayList<String>();
        _detailsList.add("Course Description...");
        _detailsList.add("Participate with Whatsapp");
        _detailsList.add("Participate with a Phone Call");
        _detailsList.add("Share with a Friend");
        _detailsList.add("Send FeedBack");
        this._courseToolsImageResourse = new int[]{
                R.drawable.info_logo,R.drawable.whatsapp_logo,R.drawable.phonecall_logo,
                R.drawable.messenger_logo,R.drawable.facebook_logo};
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._detailsList.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item,null);
        }

        TextView descriptiontv = (TextView) convertView.findViewById(R.id.description_tv);
        //descriptiontv.setText(_courselist.get(groupPosition).get_description());
        descriptiontv.setText("lkjsjefl");
        descriptiontv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //description dialog...

            }
        });

        Button certBtn = (Button) convertView.findViewById(R.id.certificates_btn);
        certBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show certificates dialog
            }
        });

        Button whatsappBtn = (Button) convertView.findViewById(R.id.whatsapp_comunicate);
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send whatsapp intent to phone number
            }
        });

        Button meetUsBtn = (Button) convertView.findViewById(R.id.meet_us);
        meetUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contact info fragment .. address, phone, mobile, fb
            }
        });

        Button submitBtn = (Button) convertView.findViewById(R.id.submit_course);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submit course into db (if logged in)
                //set submit flag to submitted in sharedPref
            }
        });

        //ImageView courseChildIV = (ImageView) convertView.findViewById(R.id.coursesChildIcon);
        //courseChildIV.setImageResource(_courseToolsImageResourse[childPosition]);

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._courselist.get(groupPosition).getName();
    }

    @Override
    public int getGroupCount() {
        return this._courselist.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.courseTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        //TextView thtv,hpdtv,dpwtv,tdtv,datetv;
        TextView thtv = (TextView) convertView.findViewById(R.id.totalHoursText);
        TextView hpdtv = (TextView) convertView.findViewById(R.id.hoursPerDayText);
        TextView dpwtv = (TextView) convertView.findViewById(R.id.daysPerWeekText);
        TextView tdtv = (TextView) convertView.findViewById(R.id.totalDaysText);
        TextView datetv = (TextView) convertView.findViewById(R.id.courseDateText);
        TextView[] courceParamstvs = {thtv,hpdtv,dpwtv,tdtv,datetv};
        for (int i=0;i<courceParamstvs.length;i++){
            courceParamstvs[i].setPaintFlags(courceParamstvs[i].getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        }
        courceParamstvs[0].setText(_courselist.get(groupPosition).gethrs());
        courceParamstvs[1].setText(_courselist.get(groupPosition).get_hrsPd());
        courceParamstvs[2].setText(_courselist.get(groupPosition).get_dPw());
        courceParamstvs[3].setText(_courselist.get(groupPosition).get_days());
        courceParamstvs[4].setText(_courselist.get(groupPosition).get_startDate());
        if(lblListHeader.getText()=="Android Development"){
            lblListHeader.setTextSize(17f);
        }
        final int finalgroupPosition = groupPosition;
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.courseImageView);
        final ImageView imageViewDefault = (ImageView) convertView.findViewById(R.id.courseImageViewDefault);
        if(_courselist.get(groupPosition).get_icon().equals("eh")){
            imageView.setImageResource(R.drawable.anonymos_icon);
        }else if (_courselist.get(groupPosition).get_icon().equals("an")){
            imageView.setImageResource(R.drawable.androidgreen_icon);
        }else if (_courselist.get(groupPosition).get_icon().equals("DEFAULT")){
            imageView.setImageResource(R.drawable.no_course_icon);
        }else Picasso.with(_context)
                .load(networkImageUrl+_courselist.get(groupPosition).get_icon())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(_context)
                                .load(networkImageUrl+_courselist.get(groupPosition).get_icon())
                                .error(R.drawable.no_course_icon)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
        ImageView hotView = (ImageView) convertView.findViewById(R.id.hotImageView);
        if(_courselist.get(groupPosition).get_hotORnot().equals("not")){
            hotView.setAlpha(0f);
        }else if (_courselist.get(groupPosition).get_hotORnot().equals("new")){

        }else if(_courselist.get(groupPosition).get_hotORnot().equals("hot")){
            hotView.setAlpha(1f);
        };

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}