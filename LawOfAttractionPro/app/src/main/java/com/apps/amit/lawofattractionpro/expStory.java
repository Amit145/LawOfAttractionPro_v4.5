package com.apps.amit.lawofattractionpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattractionpro.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
//import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class expStory extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView t1,t2,t3,t4,textView1,textView2;
    Resources resources;
    EditText edt, edt1;
    public AlertDialog alert;
    String id,UToken;
    LinearLayout mainlayout;
    //free version
    String ServerKey ="AAAAF3wcKuU:APA91bHtj0iXrqpFaHjciJncf64p6xntAd7BQ7v6tCYBOwmVNeSaHEr789jfxCLq6qfH6JbeTNkRCk2ALVbMrZTJNozlE3VOt2LMFUnzZtHep4Mgkc0eTCwheT0ZxM8tnh2J4tVs4T4z";

    SwipeRefreshLayout mSwipeRefreshLayout;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    List<DBUtils4> personUtilsList;
    RequestQueue rq;

    String request_url = "http://www.innovativelabs.xyz/show_uComment.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_story);

        Intent result = getIntent();

        mainlayout = findViewById(R.id.mainLayout);


        Glide.with(this).load(R.drawable.starshd).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainlayout.setBackground(drawable);
                }
            }
        });

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        t1 =  findViewById(R.id.textView10);
        t2 =  findViewById(R.id.textView11);
        t3 =  findViewById(R.id.textView12);
        t4 =  findViewById(R.id.reply);

        t4.setText(getString(R.string.expReply1));


        //check If Intent values are not null
        if(result.getExtras()!=null)
        {
            String name = result.getExtras().getString("NameKey");
            String story = result.getExtras().getString("StoryKey");
            String date = result.getExtras().getString("DateKey");
            id = result.getExtras().getString("idKey");
            UToken = result.getExtras().getString("Utoken");

            t1.setText(name);
            t2.setText(story);
            t3.setText(date);
        }

        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), getString(R.string.refresh_text), Toast.LENGTH_SHORT).show();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        personUtilsList.clear();
                        mAdapter.notifyDataSetChanged();
                        sendRequest();
                    }
                }, 2000);
            }
        });

        rq = Volley.newRequestQueue(this);

        recyclerView =  findViewById(R.id.recylcerView4);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        personUtilsList = new ArrayList<>();


        sendRequest();


    }

    /*
    public void Refresh()
    {
        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), getString(R.string.refresh_text), Toast.LENGTH_SHORT).show();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        personUtilsList.clear();
                        mAdapter.notifyDataSetChanged();
                        sendRequest();
                    }
                }, 2000);
            }
        });
    }

    */

    public void sendRequest() {

        //userID(id);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(request_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    DBUtils4 personUtils = new DBUtils4();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String val = jsonObject.getString("id");

                        if(val.equalsIgnoreCase(id)) {
                            personUtils.setPersonFirstName(jsonObject.getString("name"));
                            personUtils.setPersonLastName(jsonObject.getString("comm"));
                            personUtils.setJobProfile(jsonObject.getString("time"));

                            personUtilsList.add(personUtils);
                            //Collections.reverse(personUtilsList);
                            // personUtils.setcount(jsonObject.getString("id"));
                            // personUtils.setcount(jsonObject.getString("id"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //Collections.reverse(personUtilsList);


                }

                mAdapter = new uComment(expStory.this, personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        rq.add(jsonArrayRequest);

    }


    public void onReply(View view) {

        view.startAnimation(buttonClick);
        AlertDialog.Builder builder = new AlertDialog.Builder(expStory.this);
        //LayoutInflater inflater = expStory.this.getLayoutInflater();
        final View dialogView = View.inflate(getApplicationContext(),R.layout.exp_options, null);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.expReply)+"\n ------------------------------------------------\n "+getString(R.string.myStory_warn));

        builder.setView(dialogView);
        alert = builder.create();
        alert.show();


        edt =  dialogView.findViewById(R.id.username);
        edt1 =  dialogView.findViewById(R.id.comments);

        textView1 =  dialogView.findViewById(R.id.name);
        textView2 =  dialogView.findViewById(R.id.experience);


        //edt4 =  dialogView.findViewById(R.id.share);

        Button submit =  dialogView.findViewById(R.id.btnsubmit);
        Button cancel =  dialogView.findViewById(R.id.btncancel);

        textView1.setText(getString(R.string.enterName));
        textView2.setText(getString(R.string.expRplyText));
        submit.setText(getString(R.string.subUniverse_dialogSubmit));
        cancel.setText(getString(R.string.subUniverse_dialogCancel));


        SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        String naam = sp1.getString("userName","");

        if((naam.equalsIgnoreCase("")))
        {
            edt.setText("");

        }

        else{

            edt.setText(naam);
            //Toast.makeText(getApplicationContext(), "Name set: " +naam, Toast.LENGTH_LONG).show();


        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);


               GetDataFromEditText();
                //Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();

                // sendRequest();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                alert.dismiss();
            }
        });

    }

    public void GetDataFromEditText(){



        String Name = edt.getText().toString();
        String Comment = edt1.getText().toString();
        //String share = edt4.getText().toString();
        String date =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", Name);
        editor.apply();

        checkEditText(Name,Comment,date,id);

    }

    public void  checkEditText(String Name,String Comment,String date, String id) {



        if (TextUtils.isEmpty(Name)) {

            edt.setError(getString(R.string.enterName));

            //edt1.setError("Please Enter Your Wish");
            //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

        } else if (!Name.matches("[a-zA-Z ]+")) {

            edt.setError(getString(R.string.nameError3));



        } else if (edt.getText().length()>=25 )
        {
            edt.setError(getString(R.string.nameError));
            //Toast.makeText(getApplicationContext(), "Name Cannot be more than 25 characters", Toast.LENGTH_LONG).show();

        }

        else if (edt.getText().length()<3 )
        {

            edt.setError(getString(R.string.nameError1));
            // Toast.makeText(getApplicationContext(), "Name Cannot be less than 3 characters", Toast.LENGTH_LONG).show();

        }

        else if(edt.getText().toString().toLowerCase().contains("axwound") ||
                edt.getText().toString().toLowerCase().contains("anus") ||
                edt.getText().toString().toLowerCase().contains("arsehole") ||
                edt.getText().toString().toLowerCase().contains("cock") ||
                edt.getText().toString().toLowerCase().contains("fuck") ||
                Name.toLowerCase().contains("hole") ||
                Name.toLowerCase().contains("boner") ||
                Name.toLowerCase().contains("blowjob") ||
                Name.toLowerCase().contains("blow job") ||
                Name.toLowerCase().contains("bitch") ||
                Name.toLowerCase().contains("tit") ||
                Name.toLowerCase().contains("bastard") ||
                Name.toLowerCase().contains("camel toe") ||
                Name.toLowerCase().contains("choad") ||
                Name.toLowerCase().contains("chode") ||
                Name.toLowerCase().contains("sucker") ||
                Name.toLowerCase().contains("coochie") ||
                Name.toLowerCase().contains("coochy") ||
                Name.toLowerCase().contains("cooter") ||
                Name.toLowerCase().contains("cum") ||
                Name.toLowerCase().contains("slut") ||
                Name.toLowerCase().contains("cunnie") ||
                Name.toLowerCase().contains("cunnilingus") ||
                Name.toLowerCase().contains("cunt") ||
                Name.toLowerCase().contains("dick") ||
                Name.toLowerCase().contains("dildo") ||
                Name.toLowerCase().contains("doochbag") ||
                Name.toLowerCase().contains("douche") ||
                Name.toLowerCase().contains("dyke") ||
                Name.toLowerCase().contains("fag") ||
                Name.toLowerCase().contains("fellatio") ||
                Name.toLowerCase().contains("feltch") ||
                Name.toLowerCase().contains("butt") ||
                Name.toLowerCase().contains("gay") ||
                Name.toLowerCase().contains("genital") ||
                Name.toLowerCase().contains("penis") ||
                Name.toLowerCase().contains("handjob") ||
                Name.toLowerCase().contains("hoe") ||
                Name.toLowerCase().contains("sex") ||
                Name.toLowerCase().contains("jizz") ||
                Name.toLowerCase().contains("kooch") ||
                Name.toLowerCase().contains("kootch") ||
                Name.toLowerCase().contains("kunt") ||
                Name.toLowerCase().contains("lesbo") ||
                Name.toLowerCase().contains("lesbian") ||
                Name.toLowerCase().contains("muff") ||
                Name.toLowerCase().contains("vagina") ||
                Name.toLowerCase().contains("pussy") ||
                Name.toLowerCase().contains("pussies") ||
                Name.toLowerCase().contains("poon") ||
                Name.toLowerCase().contains("piss") ||
                Name.toLowerCase().contains("arse") ||
                Name.toLowerCase().contains("erection") ||
                Name.toLowerCase().contains("rimjob") ||
                Name.toLowerCase().contains("scrote") ||
                Name.toLowerCase().contains("snatch") ||
                Name.toLowerCase().contains("whore") ||
                Name.toLowerCase().contains("wank") ||
                Name.toLowerCase().contains("tard") ||
                Name.toLowerCase().contains("testicle") ||
                Name.toLowerCase().contains("twat") ||
                Name.toLowerCase().contains("boob") ||
                Name.toLowerCase().contains("wiener") ||
                Name.toLowerCase().contains("spank") ||
                Name.toLowerCase().contains("http") ||
                Name.toLowerCase().contains("www") ||
                Name.toLowerCase().contains("ass") ||
                Name.toLowerCase().contains("porn") ||
                Name.toLowerCase().contains("breast") ||
                Name.toLowerCase().contains("gand"))
        {

            edt.setText("");

            edt.setError(getString(R.string.nameError2));

        }

        else if (TextUtils.isEmpty(Comment)) {

            //edt.setError("Please Enter Your Name");
            edt1.setError(getString(R.string.expReply2));
            //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

        }
        else
        {
            SendQuoteToServer(Name,Comment,date,id,UToken,ServerKey);



        }


    }
    public void SendQuoteToServer(final String Name,final String Comment, final String Time,final String id,final String TOKEN,final String SERVERKEY){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                //nameValuePairs.add(new BasicNameValuePair("Name", QuickLike));
                nameValuePairs.add(new BasicNameValuePair("UName", Name));
                nameValuePairs.add(new BasicNameValuePair("UComment", Comment));
                //nameValuePairs.add(new BasicNameValuePair("share", QuickShare));
                nameValuePairs.add(new BasicNameValuePair("Time", Time));
                nameValuePairs.add(new BasicNameValuePair("Uid", id));

                nameValuePairs.add(new BasicNameValuePair("FToken", TOKEN));
                nameValuePairs.add(new BasicNameValuePair("ServerKey", SERVERKEY));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insert_uComment.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();

                    alert.dismiss();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(),getString(R.string.nameError4),Toast.LENGTH_LONG).show();


                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(),getString(R.string.nameError4),Toast.LENGTH_LONG).show();
                }
                return " Submitted Successfully";

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                personUtilsList.clear();
                mAdapter.notifyDataSetChanged();
                sendRequest();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute( Name,Comment,Time,id,TOKEN,SERVERKEY);
        alert.dismiss();
       // Toast.makeText(getApplicationContext(),"Thank you for submitting your Story",Toast.LENGTH_LONG).show();

        //personUtilsList.clear();

        // getSqlDetails();
    }
}
