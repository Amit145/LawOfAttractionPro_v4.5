package com.apps.amit.lawofattractionpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattractionpro.helper.LocaleHelper;

public class Policy extends AppCompatActivity {


    LinearLayout l1,inter;
    ImageView b1;
    TextView text1;
    WebView mywebview;
    SwipeRefreshLayout mSwipeRefreshLayout;

    String webUrl = "http://innovativelabs.xyz/privacy_policy_pro.html";
    ProgressBar bar;

    ConnectivityManager connMngr;
    NetworkInfo netInfo;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mywebview.destroy();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mywebview.destroy();
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
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lawtips);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo =connMngr.getActiveNetworkInfo();


            mywebview = (WebView) findViewById(R.id.webView1);
            l1 = (LinearLayout)findViewById(R.id.layoutshare);



            inter = (LinearLayout) findViewById(R.id.internet);
            bar = (ProgressBar) findViewById(R.id.progressBar);

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value);
            final Resources resources = context.getResources();

           // text1.setText(getString(R.string.noInternet_txt));

            //b1.setVisibility(View.INVISIBLE);

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_orange_dark,
                    android.R.color.holo_blue_dark);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    Toast.makeText(getApplicationContext(),getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                    ( new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Intent i = new Intent(getApplicationContext(), Policy.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Policy.this.finish();
                            startActivity(i);
                        }
                    },2000);
                }
            });

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(webUrl);
            }
            else
            {




                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }








        }catch (OutOfMemoryError e)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            System.gc();
        }
    }

    class MyAppWebViewClient extends WebViewClient{

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();

        boolean timeout;
        public MyAppWebViewClient() {
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
                        Toast.makeText(getApplicationContext(), getString(R.string.nwError), Toast.LENGTH_SHORT).show();
                        mywebview.setVisibility(View.INVISIBLE);
                        l1.setVisibility(View.INVISIBLE);
                        inter.setVisibility(View.VISIBLE);
                        text1 = (TextView) findViewById(R.id.text);
                        text1.setText(getString(R.string.refresh_text));
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
            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value);
            final Resources resources = context.getResources();

            super.onPageFinished(view, url);
            bar.setVisibility(View.INVISIBLE);
            timeout = false;
            b1 = (ImageView) findViewById(R.id.share);
            l1.setVisibility(View.VISIBLE);
            //b1.setVisibility(View.VISIBLE);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    //share.setPackage("com.whatsapp");
                    share.putExtra(Intent.EXTRA_TEXT, getString(R.string.AppShare)+" https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(share,getString(R.string.chooseToShare)));

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

            bar.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(), "Your Internet is not working fine... Please check your Internet", Toast.LENGTH_SHORT).show();
            mywebview.setVisibility(View.INVISIBLE);
            l1.setVisibility(View.INVISIBLE);
            inter.setVisibility(View.VISIBLE);
            text1 = (TextView) findViewById(R.id.text);
            text1.setText(getString(R.string.refresh_text));
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value);
            final Resources resources = context.getResources();

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(webUrl);
            }
            else
            {


                view.loadUrl(url);
                bar.setVisibility(View.INVISIBLE);
                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }

            return true;
        }
    }


}
