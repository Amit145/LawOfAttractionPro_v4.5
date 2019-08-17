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

public class info extends AppCompatActivity {

    Button infoButon1;
    TextView txt1,TextView1,TextView2,TextView3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img1;
    ImageView appShare;

    @Override
    public void onBackPressed() {

        Intent art1 = new Intent(getApplicationContext(), Home.class);
        // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      try {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_info);

          infoButon1 =  findViewById(R.id.infoButtonn1);
          txt1 =  findViewById(R.id.ihome);
          img1 =  findViewById(R.id.igg1);
          TextView1 =  findViewById(R.id.textView1);
          TextView2 =  findViewById(R.id.textView2);
          TextView3 =  findViewById(R.id.textView3);
          appShare = findViewById(R.id.share);

          ButterKnife.bind(this);

          //get user selected language from shared preferences

          SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

          //Store selected language in a Variable called value
          final String value1 = pref.getString("language","en");

          updateViews(value1);


          img1.setImageBitmap(
                  decodeSampledBitmapFromResource(getResources(), R.drawable.lae, 200, 200));

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

          txt1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  v.startAnimation(buttonClick);
                  Intent art1 = new Intent(getApplicationContext(), Home.class);
                  // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(art1);


              }
          });

          infoButon1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  v.startAnimation(buttonClick);
                  Intent art1 = new Intent(info.this, exer.class);
                  // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(art1);


              }
          });
      }
      catch (OutOfMemoryError e)
      {
          System.gc();
      }
    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


        //txt1= (TextView) findViewById(R.id.textView3);

        //Story

        infoButon1.setText(getString(R.string.activity_info3));
        txt1.setText(getString(R.string.activity_info4));
        TextView1.setText(getString(R.string.activity_info));
        TextView2.setText(getString(R.string.activity_info1));
        TextView3.setText(getString(R.string.activity_info2));




    }
}
