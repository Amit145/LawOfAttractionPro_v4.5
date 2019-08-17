package com.apps.amit.lawofattractionpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.apps.amit.lawofattractionpro.Exercise1.startTime;

public class Finish extends AppCompatActivity {

    ImageView track;
    ImageView home;
    ImageView share;
    ImageView img;
    int dummy = 99;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView txt;
    TextView actText1;
    TextView actText2;
    TextView actText3;
    TextView actText4;
    TextView actText5;
    TextView actText6;
    long endTime;
    double elapsedSeconds;
    long elapsedMilliSeconds;
    DatabaseHandler db;

    List <String> quoteList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        home=null;
        track=null;
        share=null;
        img=null;
        txt=null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {


        db.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.Exit_Text));

        builder.setPositiveButton(getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                ActivityCompat.finishAffinity(Finish.this);
            }
        });

        builder.setNegativeButton(getString(R.string.No_text),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
        img = null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            SharedPreferences sharedPreferencesManifestationType = getApplicationContext().getSharedPreferences("MANIFESTATION_TYPE", Exercise1.MODE_PRIVATE);
            String manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_finish);

            Random rn = new Random();

            quoteList.add("\"You're one step closer to your dreams..\"");
            quoteList.add("\"Trust the process ! You will manifest all your dreams..\"");
            quoteList.add("\"Great thing's take time ! Stay Focused..\"");
            quoteList.add("\"It is not over, unless you mean it..\"");
            quoteList.add("\"Trust the Universe ! It is always there for you ..\"");

            int answer = rn.nextInt(quoteList.size());
            db = new DatabaseHandler(getApplicationContext());

            home =  findViewById(R.id.home1);
            track =  findViewById(R.id.track);
            share =  findViewById(R.id.share);
            img =  findViewById(R.id.imageView2);

            actText1=  findViewById(R.id.textView1);
            actText2=  findViewById(R.id.textView2);
            actText3=  findViewById(R.id.textView3);
            actText4=  findViewById(R.id.textView4);
            actText5=  findViewById(R.id.textView5);
            actText6=  findViewById(R.id.textView6);
            endTime = SystemClock.elapsedRealtime();

            actText1.setText(getString(R.string.activity6_text1));
            actText2.setText(getString(R.string.activity6_text2));
            actText3.setText(quoteList.get(answer));
            actText4.setText(getString(R.string.activity6_text4));
            actText5.setText(getString(R.string.activity6_text5));
            actText6.setText(getString(R.string.activity6_text6));


            elapsedMilliSeconds = endTime - startTime;
            elapsedSeconds = elapsedMilliSeconds / 1000.0;
            int t = (int) (long) elapsedSeconds;
            //String text = t +" "+ getString(R.string.activity6_text7);
            //txt.setText(manifestationTypeValue);

            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",
                    Locale.getDefault());
            String date = df.format(c);

            db.addContact(new Contact(date, manifestationTypeValue));

            Toast.makeText(getApplicationContext(), getString(R.string.event_add), Toast.LENGTH_LONG).show();

            Glide.with(getApplicationContext()).load(R.drawable.tick).thumbnail(0.1f).fitCenter().into(img);

            Glide.with(getApplicationContext()).load(R.drawable.home).thumbnail(0.1f).fitCenter().into(home);

            Glide.with(getApplicationContext()).load(R.drawable.trackprogress).thumbnail(0.1f).fitCenter().into(track);

            Glide.with(getApplicationContext()).load(R.drawable.shareee).thumbnail(0.1f).fitCenter().into(share);


            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);


                    Intent art1 = new Intent(getApplicationContext(), calendar.class);
                    startActivity(art1);
                    img = null;
                    finish();

                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);

                    Intent art1 = new Intent(getApplicationContext(), Home.class);
                    startActivity(art1);
                    img = null;
                    finish();


                }
            });


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.apps.amit.lawofattractionpro");
                    try {
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.chooseToShare)));

                    } catch (android.content.ActivityNotFoundException ex) {

                        Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                    }

                }
            });


        } catch (OutOfMemoryError e) {
            Toast.makeText(getApplicationContext(),getString(R.string.nameError4), Toast.LENGTH_LONG).show();

        }
    }

}
