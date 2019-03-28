package com.apps.amit.lawofattractionpro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.apps.amit.lawofattractionpro.timepicker.p1;

/**
 * Created by amit on 27/3/18.
 */

public class CommentsReminder extends BroadcastReceiver {

    String quote;
    String newQuote;
    RequestQueue requestQueue;

    @Override
    public void onReceive(final Context context, Intent arg2) {
        //Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        requestQueue = Volley.newRequestQueue(context);

        String url= "http://www.innovativelabs.xyz/showQuotes.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                quote = jsonobject.getString("Quote");

                                newQuote = String.valueOf(quote);

                                SharedPreferences timerEnable = context.getSharedPreferences("timerNotiEnable", SetTime.MODE_PRIVATE);
                                SharedPreferences.Editor editor = timerEnable.edit();
                                editor.putString("FCMquote", jsonobject.getString("Quote"));
                                editor.apply();



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

                            //Toast.makeText(getApplicationContext(), getString(R.string.nwError), Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        requestQueue.add(stringRequest);

        int NOTIFICATION_ID = 236;
        NotificationManager mngr1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            mngr1.createNotificationChannel(mChannel);

            SharedPreferences sp1 = context.getSharedPreferences("timerNotiEnable", context.MODE_PRIVATE);
            String naam = sp1.getString("FCMquote","");

            //SharedPreferences sp1 = context.getSharedPreferences("timerEnable", context.MODE_PRIVATE);
           // String naam = sp1.getString("userName","");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, "\t OPEN QUOTE", p1)
                    .setContentTitle(naam)
                    .setContentText(naam)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            Intent resultIntent = new Intent(context, Quotes.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Quotes.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            mngr1.notify(NOTIFICATION_ID, builder.build());
        }

        else {

            Intent i = new Intent(context, Quotes.class);
            PendingIntent p1 = PendingIntent.getActivity(context, 0, i, 0);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context);
            builder1.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, "\t OPEN QUOTE", p1)
                    .setContentTitle(newQuote)
                    .setContentText(newQuote)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            //builder.setContentIntent= PendingIntent.getActivity(this,0, new Intent(AlarmReceiver.this,Home.class),PendingIntent.FLAG_UPDATE_CURRENT);

            mngr1.notify(699, builder1.build());
        }

    }
}
