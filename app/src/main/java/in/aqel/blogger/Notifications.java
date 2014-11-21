package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Notifications extends Fragment {
    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notif, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Log.d("refresh", "refresh");
            TryParseNotifications(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SharedPreferences lastParsedNotif;
    int twoDay = 1000*60*60*24*2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        lastParsedNotif = getActivity().getSharedPreferences("lastParsedNotif", Context.MODE_PRIVATE);
        Long lastParsedNotif_time = lastParsedNotif.getLong("lastParsedNotif_time", 0);
        SharedPreferences firstLogin = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String versionName;
        try {
            versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getActivity().getResources().getString(R.string.version_name);
            e.printStackTrace();
        }
        if (!firstLogin.getBoolean("LoadedOrNot" + versionName, false)) {
            new storeBloggerDetails().execute();
            saveInBuildNotifications();
            TryParseNotifications(0);
        }
        if (System.currentTimeMillis() > lastParsedNotif_time + twoDay ){
            TryParseNotifications(1);
        }
        setHasOptionsMenu(true);
        ListView list = (ListView) view.findViewById(R.id.listView);
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllNotifications();
        if (cur.getCount() != 0) {
            NotificationsAdatpter customAdapter = new NotificationsAdatpter(getActivity(), cur);
            //CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
        }
        data.close();


        return view;
    }

    private void saveInBuildNotifications() {
        String blogName[] = DataCollections_posts.blogName;
        String bloggerName[] = DataCollections_posts.bloggerName;
        int bloggerId[] = DataCollections_posts.bloggerId;
        String category[] = DataCollections_posts.category;
        String date[] = DataCollections_posts.date;
        String title[] = DataCollections_posts.title;
        int notifId[] = DataCollections_posts.notifId;
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        for (int i = 0; i < blogName.length; i++){
            data.addNotifications(notifId[i],bloggerId[i],date[i],title[i],bloggerName[i],category[i],blogName[i]);
        }
        data.close();
    }

    private void TryParseNotifications(int mode) {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            parseNotifications();
        }else {
            if (mode == 0){
                Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseNotifications() {
        int notifications_already_saved = DataCollections_posts.bloggerId.length;
        final int notification_number = lastParsedNotif.getInt("lastParsedNotifNumber", notifications_already_saved);
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("notifications");
        query.whereGreaterThan("notification_number", notification_number);
        final DatabaseHelper data = new DatabaseHelper(getActivity());

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

    public class storeBloggerDetails extends AsyncTask<Void, Void,Void> {
        private ProgressDialog pDialog;
        String bloggerNameEn[] = DataCollections_bloggers.bloggerNameEn;
        String bloggerNameMal[] = DataCollections_bloggers.bloggerNameMal;
        String blogNameEn[] = DataCollections_bloggers.blogNameEn;
        String blogNameMal[] = DataCollections_bloggers.blogNameMal;
        String description[] = DataCollections_bloggers.description;
        String phoneNumber[] = DataCollections_bloggers.phoneNumber;
        String gmail[] = DataCollections_bloggers.gmail;
        String googlePlus[] = DataCollections_bloggers.googlePlus;
        String facebookId[] = DataCollections_bloggers.facebookId;
        int weight[] = DataCollections_bloggers.weight;
        int bloggersId[] = DataCollections_bloggers.bloggerId;
        String blogUrl[] = DataCollections_bloggers.blogUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            for (int i = 0; i < bloggerNameEn.length; i++) {
                data.addBlogger(bloggersId[i], bloggerNameEn[i], bloggerNameMal[i], blogNameEn[i], blogNameMal[i], description[i], phoneNumber[i], gmail[i],
                        googlePlus[i], facebookId[i], weight[i], 0, blogUrl[i]);
            }
            data.close();
            String versionName;
            try {
                versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = getActivity().getResources().getString(R.string.version_name);
                e.printStackTrace();
            }
            SharedPreferences firstLogin = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = firstLogin.edit();
            editor.putBoolean("LoadedOrNot" + versionName, true);
            editor.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();


        }
    }

    public class parseBloggers extends AsyncTask<Void, Void, Void>{
        private ProgressDialog pDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("LogAqel", "Parsing Bloggers");
            String[] bloggers = DataCollections_bloggers.bloggerNameEn;
            int numBloggers = bloggers.length;
            Log.d("LogAqel Number of bloggers", Integer.toString(numBloggers));
            final SharedPreferences bloggersNum = getActivity().getSharedPreferences("bloggersNum", Context.MODE_PRIVATE);
            final int bloggersNumber = bloggersNum.getInt("bloggersNum", numBloggers);
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("bloggers");
            query.whereGreaterThan("blogger_id", bloggersNumber);
            query.setLimit(40);
            // Sorts the results in descending order by the score field
            query.orderByDescending("blogger_id");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(getActivity());

                        int new_blogger_num = bloggersNumber;
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            try{
                                data.open();
                                if (new_blogger_num < parseList.getInt("blogger_id")){
                                    new_blogger_num = parseList.getInt("blogger_id");
                                }

                                Log.d("LogAqel notification Number", Integer.toString(parseList.getInt("blogger_id")));

                                String blogger_name_en = parseList.getString("blogger_name_en");
                                String blogger_name_mal = parseList.getString("blogger_name_mal");
                                String blog_name_en = parseList.getString("blog_name_en");
                                String blog_name_mal = parseList.getString("blog_name_mal");
                                String about_blogger = parseList.getString("about_blogger");
                                String phone_number = parseList.getString("phone_number");
                                String gmail = parseList.getString("gmail");
                                String facebook_id = parseList.getString("facebook_id");
                                String googleplus_id = parseList.getString("googleplus_id");
                                String blog_link = parseList.getString("blog_link");
                                int rank = parseList.getInt("rank");
                                int blogger_id = parseList.getInt("blogger_id");
                                //String blogger_name_en, String blogger_name_mal, String blog_name_en , String blog_name_mal, String description, java.lang.String phone_number, java.lang.String email, java.lang.String google_plus, java.lang.String facebook, int weight, int loadedOrNot, String blog_url
                                data.addBlogger(blogger_id, blogger_name_en, blogger_name_mal, blog_name_en, blog_name_mal,about_blogger,phone_number,gmail,googleplus_id,facebook_id, rank, 0, blog_link);
                                data.close();
                            }catch (NullPointerException es){
                                es.printStackTrace();
                            }

                        }
                        SharedPreferences.Editor editor = bloggersNum.edit();
                        editor.putInt("bloggersNum", new_blogger_num);
                        editor.putLong("lastBloggersParsedTime", System.currentTimeMillis());
                        editor.commit();

                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });

            return null;
        }
    }


}
