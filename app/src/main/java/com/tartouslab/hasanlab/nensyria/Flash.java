package com.tartouslab.hasanlab.nensyria;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.tartouslab.hasanlab.nensyria.R;
import com.tartouslab.hasanlab.nensyria.AppController;
import com.tartouslab.hasanlab.nensyria.AppConfig;
import com.tartouslab.hasanlab.nensyria.helper.SessionManager;
import com.tartouslab.hasanlab.nensyria.helper.SQLiteHandler;


public class Flash extends AppCompatActivity {

    Button skipBTN;
    Boolean canAnimtate = true;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        inputEmail = (EditText) findViewById(R.id.emaitET);
        inputPassword = (EditText) findViewById(R.id.passET);
        btnLogin = (Button) findViewById(R.id.logbtn);
        btnLinkToRegister = (Button) findViewById(R.id.signupbtn);
        skipBTN = (Button) findViewById(R.id.skipbtn);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Flash.this, MainDrawerActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        final ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        setAlphaAnimation(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canAnimtate)
                setAlphaAnimation(view);
            }
        });
    }

    public void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, 0.25f);
        fadeOut.setDuration(500);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", 0.25f, 1f);
        fadeIn.setDuration(500);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        final AnimatorSet mAnimationSet2 = new AnimatorSet();
        mAnimationSet2.playSequentially(fadeOut,fadeIn);
        mAnimationSet2.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inputEmail.setVisibility(View.VISIBLE);
                inputPassword.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                skipBTN.setVisibility(View.VISIBLE);
                btnLinkToRegister.setVisibility(View.VISIBLE);
                canAnimtate = true;
                //Intent i = new Intent(Flash.this, MainDrawerActivity.class );
                //startActivity(i);
                //startActivity(i, ActivityOptions.makeSceneTransitionAnimation(Flash.this).toBundle());
            }
        });
        AnimatorSet mAnimationSet1 = new AnimatorSet();
        mAnimationSet1.playSequentially(fadeOut,fadeIn);
        mAnimationSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet2.start();
            }
        });
        canAnimtate = false;
        mAnimationSet1.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void startLogin(View view) {
    }

    public void startActivity(View view) {
        Intent i = new Intent(Flash.this, MainDrawerActivity.class );
        startActivity(i);
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in

                        // Now store the user in SQLite and create a session
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        // Create login session
                        session.createLoginSession(name,email);

                        String created_at = user
                                .getString("created_at");

                        //deleting previous user
                        //db.deleteUsers();

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(Flash.this,
                                MainDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String englishPlease(String errormag){
        String nomalisedError="";
        if(errormag.equals("")){
            nomalisedError = "";
        }
        return nomalisedError;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
