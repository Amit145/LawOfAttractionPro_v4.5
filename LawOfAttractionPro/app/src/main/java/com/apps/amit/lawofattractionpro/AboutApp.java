//Package Name of APP on PlayStore
package com.apps.amit.lawofattractionpro;

//All import statements Required for Current Java File
import android.content.Context;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.apps.amit.lawofattractionpro.helper.LocaleHelper;
import com.bumptech.glide.Glide;

//Main Class
public class AboutApp extends AppCompatActivity {

    ImageView img3;
    TextView title;
    TextView subtitle;


    // Override methods of Current Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();

        img3=null;
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutapp);                                        // Set Content Layout (Activity.xml) for current Activity

        //For Version3.3
        /*
        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);     //Get user Selected Language from Shared preference

        */
        String value1 = "en";

        //Store selected language in a Variable called value
        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        Resources resources = context.getResources();

        //Find all Id's from Activity.xml in Activity.java

        img3 = findViewById(R.id.imageView6);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);


        //Set Text based on user choosen language, get Language specific text from Resources for specified language

        title.setText(getString(R.string.aboutApp));
        subtitle.setText(getString(R.string.aboutApp1));


        Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(img3);

    }
}
