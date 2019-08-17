package com.apps.amit.lawofattractionpro;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.sun.jna.platform.unix.X11;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ThankYou extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE= 1;
    TextView thankYouCount;
    RequestQueue requestQueue;
    Thread thread;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout linearLayout;
    List<Integer> drawableList = new ArrayList<>();
    ImageView img,img1;
    List<String> colorList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(netInfo!=null && netInfo.isConnected()) {

            thread.interrupt();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        linearLayout = findViewById(R.id.thankActivity);
        img =  findViewById(R.id.gratitudelayoutimage);
        img1 =  findViewById(R.id.instaImage);

        colorList.add("#3498DB");
        colorList.add("#17A589");
        colorList.add("#2E86C1");
        colorList.add("#52BE80");
        colorList.add("#4C9246");
        colorList.add("#27AE60");

        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Intent i = new Intent(getApplicationContext(), ThankYou.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ThankYou.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        if(netInfo!=null && netInfo.isConnected()) {

            Random rn = new Random();
            int answer = rn.nextInt(colorList.size());

            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setBackgroundColor(Color.parseColor(colorList.get(answer)));
            getCount();

            thankYouCount = findViewById(R.id.thankYouCount);
            drawableList.add(R.drawable.thank_share1);
            drawableList.add(R.drawable.thank_share2);
            drawableList.add(R.drawable.thank_share3);
            drawableList.add(R.drawable.thank_share4);

            Glide.with(getApplicationContext()).load(R.drawable.thank_act_image).thumbnail(0.1f).into(img);
            Glide.with(getApplicationContext()).load(R.drawable.insta).thumbnail(0.1f).into(img1);



        }

        else
        {
            linearLayout.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }

    }

    public void sayThankYou(View view) {

        if (checkPermission()) {
            String tp= "tp";
            SendThankYouToServer(tp);

            Random rn = new Random();
            int answer = rn.nextInt(drawableList.size());

            try {
                Uri imageUri = null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                            BitmapFactory.decodeResource(getResources(), drawableList.get(answer)), null, null));
                } catch (NullPointerException e) {
                }
                String text = "Thank You So Much !";
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT,text);
                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                startActivity(shareIntent);

                //call update thankyou


            } catch (android.content.ActivityNotFoundException ex) {
                //Toast.makeText(mActivity, mActivity.getString(R.string.share_google_plus_not_installed), Toast.LENGTH_LONG).show();
            }
        } else {
            requestPermission();
        }


    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    //Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions to use this feature",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                                        if (ActivityCompat.shouldShowRequestPermissionRationale(ThankYou.this,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                                            // now, user has denied permission (but not permanently!)

                                                            requestPermission();

                                                        } else {

                                                            // now, user has denied permission permanently!

                                                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have declined Storage permission.\n" +
                                                                    "You must approve Storage permission in \"App Permissions\" in the app settings on your device.", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                                                                }
                                                            });
                                                            View snackbarView = snackbar.getView();
                                                            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                                            textView.setMaxLines(5);  //Or as much as you need
                                                            snackbar.show();

                                                        }
                                                        break;
                                                    }

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }

                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ThankYou.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", okListener);
        builder .setNegativeButton("Cancel", null);
        builder .create();
        builder .show();
    }

    public void SendThankYouToServer(final String Likes){
        class  SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Likes", Likes));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertThankYouCount.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    //  Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    /// Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_LONG).show();
                getCount();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Likes);
        // getSqlDetails();
    }

    private  void getCount()
    {

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestQueue = Volley.newRequestQueue(getApplicationContext());

                                String url= "http://www.innovativelabs.xyz/showThankYouCount.php";

                                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                                        url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {

                                                    JSONArray jsonarray = new JSONArray(response);

                                                    for(int i=0; i < jsonarray.length(); i++) {

                                                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                                                        thankYouCount.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(jsonobject.getString("ThankYouCount"))));

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();


                                                }




                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                if(error != null){

                                                    //Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.nwError), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                );

                                requestQueue.add(stringRequest);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

    }

    public void openInsta(View view) {

        Uri uri = Uri.parse("http://instagram.com/_u/thelaw24x7");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/thelaw24x7")));
        }
    }
}
