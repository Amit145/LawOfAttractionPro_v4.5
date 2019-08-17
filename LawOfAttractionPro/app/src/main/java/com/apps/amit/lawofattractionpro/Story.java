package com.apps.amit.lawofattractionpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.apps.amit.lawofattractionpro.helper.LocaleHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Story extends AppCompatActivity {


    LinearLayout l1,inter;
    ImageView b1;
    WebView mywebview;
    //SwipeRefreshLayout mSwipeRefreshLayout;
    Resources resources = null;

    String storyLink,views,shares;
    int viewcount,storyid,shareInt;
    ProgressBar bar;

    ConnectivityManager connMngr;
    NetworkInfo netInfo;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // mywebview.destroy();
        this.finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mywebview.destroy();
        //this.finish();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if((keyCode == KeyEvent.KEYCODE_BACK) && mywebview.canGoBack())
        {
            mywebview.goBack();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }





    @Override
    protected void onStop() {
        super.onStop();

    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_story);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                netInfo = connMngr.getActiveNetworkInfo();
            }

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
             resources = context.getResources();

            mywebview =  findViewById(R.id.webView1);
            l1 = findViewById(R.id.layoutshare);

            inter =  findViewById(R.id.internet);
            bar =  findViewById(R.id.progressBar);


            if(netInfo!=null && netInfo.isConnected()) {


                Intent result = getIntent();

                if(result.getExtras()!=null)
                {
                    storyLink = result.getExtras().getString("storyLink");
                    shares  = result.getExtras().getString("storyShares");
                    views  = result.getExtras().getString("storyViews");
                    storyid =  result.getExtras().getInt("storyID");

                }

                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(storyLink);

                try {
                    viewcount = Integer.parseInt(views) + 1;
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                }
                views = String.valueOf(viewcount);

                SendViewsToServer(views,String.valueOf(storyid));


            }
            else
            {

                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }


        }catch (OutOfMemoryError e)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            System.gc();
        }
    }

    class MyAppWebViewClient extends WebViewClient{

        boolean timeout;
        private MyAppWebViewClient() {
            timeout = true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            inter.setVisibility(View.INVISIBLE);

            Runnable run = new Runnable()  {
                @Override
                public void run() {

                    if(timeout) {

                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.nwError, Toast.LENGTH_SHORT).show();
                        mywebview.setVisibility(View.INVISIBLE);
                        l1.setVisibility(View.INVISIBLE);
                        inter.setVisibility(View.VISIBLE);
                        //b1.setVisibility(View.INVISIBLE);
                        // Toast.makeText(getApplicationContext(), "Timed Out ...", Toast.LENGTH_SHORT).show();

                    }
                }
            };

            Handler myHandler = new Handler(Looper.myLooper());
            myHandler.postDelayed(run, 20000);

        }




        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.INVISIBLE);
            timeout = false;
            b1 =  findViewById(R.id.share);
            l1.setVisibility(View.VISIBLE);
            //b1.setVisibility(View.VISIBLE);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    //share.setPackage("com.whatsapp");
                    share.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.storyShare)+"\n-------------------------\nhttps://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));

                        try {
                            shareInt = Integer.parseInt(shares) + 1;
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                        }
                        shares = String.valueOf(shareInt);

                        SendSharesToServer(shares,String.valueOf(storyid));


                    } catch (android.content.ActivityNotFoundException ex) {
                        //Toast.makeText(this, "Error".toString(), Toast.LENGTH_SHORT);
                    }
                }
            });
            //Toast.makeText(getApplicationContext(), "Not", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            //Toast.makeText(getApplicationContext(), "NO INTERNET", Toast.LENGTH_SHORT).show();

            bar.setVisibility(View.INVISIBLE);
         //   Toast.makeText(getApplicationContext(), "Your Internet is not working fine... Please check your Internet", Toast.LENGTH_SHORT).show();
            mywebview.setVisibility(View.INVISIBLE);
            l1.setVisibility(View.INVISIBLE);
            inter.setVisibility(View.VISIBLE);

        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(storyLink);
            }
            else
            {


                view.loadUrl(url);

                bar.setVisibility(View.INVISIBLE);
                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }


    public void SendViewsToServer(final String Views,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("views", Views));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertStoryViews.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                   // Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    //Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                // Toast.makeText(getApplicationContext(), "You Shared it" , Toast.LENGTH_LONG).show();


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Views, id);
        // getSqlDetails();
    }

    public void SendSharesToServer(final String Shares,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("shares", Shares));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertStorySharesViews.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                    //Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                   // Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                // Toast.makeText(getApplicationContext(), "You Shared it" , Toast.LENGTH_LONG).show();


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Shares, id);
        // getSqlDetails();
    }


}
