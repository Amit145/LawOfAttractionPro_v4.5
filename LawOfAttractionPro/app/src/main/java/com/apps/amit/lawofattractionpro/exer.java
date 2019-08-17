package com.apps.amit.lawofattractionpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.amit.lawofattractionpro.helper.LocaleHelper;

import butterknife.ButterKnife;

import static com.apps.amit.lawofattractionpro.ScaleImage.decodeSampledBitmapFromResource;

public class exer extends AppCompatActivity {

    Button infoButon;
    TextView txt,txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img;
    ImageView appShare;

    @Override
    public void onBackPressed() {

        Intent art1 = new Intent(getApplicationContext(), info.class);
        // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exer);

            infoButon =  findViewById(R.id.infoButton);
            txt =  findViewById(R.id.ihome);

            txt1 =  findViewById(R.id.textView1);
            txt2 =  findViewById(R.id.textView2);
            txt3 =  findViewById(R.id.textView3);
            txt4 =  findViewById(R.id.textView4);
            txt5 =  findViewById(R.id.textView5);
            txt6 =  findViewById(R.id.textView6);
            txt7 =  findViewById(R.id.textView7);
            txt8 =  findViewById(R.id.textView8);
            txt9 =  findViewById(R.id.textView9);
            txt10 =  findViewById(R.id.textView10);
            txt11 =  findViewById(R.id.textView11);
            txt12 =  findViewById(R.id.textView12);
            appShare = findViewById(R.id.share);

            img =  findViewById(R.id.ig);

            img.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.brainexer, 200, 200));


            ButterKnife.bind(this);

            //get user selected language from shared preferences

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            updateViews(value1);

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Home.class);
                    // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(art1);


                }
            });

            appShare.setOnClickListener(new View.OnClickListener() {
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

            infoButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Manual.class);
                    // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(art1);


                }
            });

        } catch (OutOfMemoryError e)
        {
            System.gc();
        }
    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        infoButon.setText(resources.getString(R.string.activity_exer12));
        txt.setText(resources.getString(R.string.activity_exer13));


        txt1.setText(resources.getString(R.string.activity_exer));
        txt2.setText(resources.getString(R.string.activity_exer1));
        txt3.setText(resources.getString(R.string.activity_exer2));
        txt4.setText(resources.getString(R.string.activity_exer3));
        txt5.setText(resources.getString(R.string.activity_exer4));
        txt6.setText(resources.getString(R.string.activity_exer5));
        txt7.setText(resources.getString(R.string.activity_exer6));
        txt8.setText(resources.getString(R.string.activity_exer7));
        txt9.setText(resources.getString(R.string.activity_exer8));
        txt10.setText(resources.getString(R.string.activity_exer9));
        txt11.setText(resources.getString(R.string.activity_exer10));
        txt12.setText(resources.getString(R.string.activity_exer11));





    }
}
