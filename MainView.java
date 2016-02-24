package com.example.raunak.moodleplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainView extends AppCompatActivity implements View.OnClickListener{
    private Button logout;
    private Button homepage;
    private Button assignment;
    private Button notification;
    private Button thread;
    private Button grade;
    private static final String DOMAIN = "http://10.192.33.139:8000";
    private String urlJsonObj;
    private String urlJsonArry;
    private static String TAG = MainView.class.getSimpleName();
    private String jsonResponse;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        logout = (Button)findViewById(R.id.logout);
        homepage = (Button)findViewById(R.id.homepage);
        assignment = (Button)findViewById(R.id.assignment);
        notification = (Button)findViewById(R.id.notification);
        thread = (Button)findViewById(R.id.thread);
        grade = (Button)findViewById(R.id.grade);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


    }

    public void onClick(View v){
        if(v==logout){
            setLogout();
        }
        else if(v==homepage){
            setHomepage();
        }

    }


    private void setLogout() {
        showpDialog();
        urlJsonObj = DOMAIN+"/default/logout.json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());





                    Intent intent = new Intent(MainView.this, MainActivity.class);

                    startActivity(intent);


                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void setHomepage() {

                Intent intent = new Intent(MainView.this, MainCourse.class);

                startActivity(intent);


    }


    private void makeJsonArrayRequest() {
        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                                jsonResponse += "Mobile: " + mobile + "\n\n\n";

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }

                        hidepDialog();
                        Intent intent = new Intent(MainView.this, MainView.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void startnewActivity(){
        Intent intent = new Intent(MainView.this,MainView.class);
        startActivity(intent);
    }
}
