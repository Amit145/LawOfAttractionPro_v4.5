package com.apps.amit.lawofattractionpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattractionpro.helper.LocaleHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MusicList extends AppCompatActivity {


    TextView intText;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    List<MusicUtils> personUtilsList;
    SwipeRefreshLayout mSwipeRefreshLayout1;
    Resources resources;
    String taskURL = "";
    LinearLayout parent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    RequestQueue rq;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");


        taskURL = "http://www.innovativelabs.xyz/Scripts/"+value1+"_MusicData.php";    //music database

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();



        parent = findViewById(R.id.taskParent);

        intText = findViewById(R.id.internetText);

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        mSwipeRefreshLayout1 =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout1.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout1.setRefreshing(true);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout1.setRefreshing(false);
                        Intent i = new Intent(getApplicationContext(), MusicList.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MusicList.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });


        if(netInfo!=null && netInfo.isConnected()) {
            rq = Volley.newRequestQueue(getApplicationContext());


            recyclerView =  findViewById(R.id.recyclerView1);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);
            try {

                intText.setVisibility(View.INVISIBLE);
            } catch (Exception e) {

                Toast.makeText(this,  resources.getString(R.string.nameError4), Toast.LENGTH_SHORT).show();

            }

            personUtilsList = new ArrayList<>();
            sendRequest(taskURL);

        } else

        {

            intText.setVisibility(View.VISIBLE);

            Toast.makeText(this,  resources.getString(R.string.noInternet_txt), Toast.LENGTH_SHORT).show();

        }

    }


    public void sendRequest(String taskURL){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( taskURL,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    MusicUtils personUtils = new MusicUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("name"));
                        personUtils.setPersonLastName(jsonObject.getString("body"));
                        personUtils.setJobProfile(jsonObject.getString("imgUrl"));
                        personUtils.setimgUrl(jsonObject.getString("webUrl"));
                        personUtils.setid(jsonObject.getInt("id"));
                        personUtils.setShares(jsonObject.getString("shares"));
                        personUtils.setViews(jsonObject.getString("views"));
                        personUtils.setbody(jsonObject.getString("body"));
                        personUtils.setDuration(jsonObject.getInt("duration"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);


                }

                mAdapter = new MusicAdapter(getApplicationContext(),personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                mSwipeRefreshLayout1 =  findViewById(R.id.swipe_container);
                mSwipeRefreshLayout1.setColorSchemeResources(R.color.colorPrimary,
                        android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

                mSwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout1.setRefreshing(true);
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                        ( new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout1.setRefreshing(false);
                                Intent i = new Intent(getApplicationContext(), MusicList.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MusicList.this.finish();
                                startActivity(i);
                            }
                        },2000);
                    }
                });
            }
        });

        rq.add(jsonArrayRequest);


    }



}
