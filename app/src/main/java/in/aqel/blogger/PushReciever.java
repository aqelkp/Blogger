package in.aqel.blogger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class PushReciever extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new parseNotifications().execute();
        setContentView(R.layout.activity_push_reciever);
    }

    public class parseNotifications extends AsyncTask<Void, Void, Void>{
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PushReciever.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()) {
                parseNotifications();
            }else {
                    Toast.makeText(PushReciever.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        private void parseNotifications() {
            final SharedPreferences lastParsedNotif = getSharedPreferences("lastParsedNotif", Context.MODE_PRIVATE);
            int notifications_already_saved = DataCollections_posts.bloggerId.length;
            final int notification_number = lastParsedNotif.getInt("lastParsedNotifNumber", notifications_already_saved);
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("notifications");
            query.whereGreaterThan("notification_number", notification_number);
            final DatabaseHelper data = new DatabaseHelper(PushReciever.this);

            query.setLimit(20);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        try{
                            int new_notif_num = notification_number;
                            for (int i = 0; i < userList.size(); i++){
                                ParseObject parseList = userList.get(i);
                                Log.d("score", "Retrieved " + userList.size() + " posts");
                                data.open();
                                if (new_notif_num < parseList.getInt("notification_number")){
                                    new_notif_num = parseList.getInt("notification_number");
                                }
                                // int bloggerId,  String date , String title, String blogger_name, String categ, String blogName
                                Log.d("notification Number", Integer.toString(parseList.getInt("notification_number")));

                                data.addNotifications(parseList.getInt("notification_number"),parseList.getInt("blogger_id"), parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name_eng"),parseList.getString("category"), parseList.getString("blog_name_eng"));
                                data.close();
                            }
                            SharedPreferences.Editor editor = lastParsedNotif.edit();
                            editor.putInt("lastParsedNotifNumber", new_notif_num);
                            editor.putLong("lastParsedNotif_time", System.currentTimeMillis());
                            editor.commit();
                        }catch (NullPointerException es){
                            es.printStackTrace();
                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Intent intent = new Intent(PushReciever.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }
    }


}
