package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class BloggersList extends Fragment {

    public BloggersList() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        Log.d("LogAq Id of item", Integer.toString(menuId));
        switch (menuId){
            case R.id.action_refresh_bloggers:
                new parseBloggers().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.home, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_bloggers_list, container, false);


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
        } else {

            ListView list = (ListView) rootView.findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllBloggers();
            if (cur != null) {
                BloggerCursorAdapter customAdapter = new BloggerCursorAdapter(getActivity(), cur);

                list.setAdapter(customAdapter);
            }
            data.close();



        }
        int halfMonth = 1000*60*60*24*15;
        final SharedPreferences bloggersNum = getActivity().getSharedPreferences("bloggersNum", Context.MODE_PRIVATE);
        Long lastBloggersParsedTime = bloggersNum.getLong("lastBloggersParsedTime", 0);
        if (System.currentTimeMillis() > lastBloggersParsedTime + halfMonth){
            //new parseBloggersInBackground().execute();
            new parseBloggers().execute();
        }
        return rootView;
    }


    public class storeBloggerDetails extends AsyncTask<Void, Void,Void>{
        private ProgressDialog pDialog;
        String bloggerNameEn[] = DataCollections_bloggers.bloggerNameEn;
        String bloggerNameMal[]= DataCollections_bloggers.bloggerNameMal;
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
            DatabaseHelper data= new DatabaseHelper(getActivity());
            data.open();
            for (int i = 0; i < bloggerNameEn.length; i++){
                data.addBlogger( bloggersId[i],bloggerNameEn[i], bloggerNameMal[i], blogNameEn[i], blogNameMal[i], description[i], phoneNumber[i], gmail[i],
                        googlePlus[i], facebookId[i], weight[i], 0 ,blogUrl[i]);
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
            ListView list = (ListView) getView().findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllBloggers();
            if (cur != null) {
                BloggerCursorAdapter customAdapter = new BloggerCursorAdapter(getActivity(), cur);
                list.setAdapter(customAdapter);
            }
            data.close();

        }


    }

    public class parseBloggers extends AsyncTask<Void, Void, Void>{
        private ProgressDialog pDialog;

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
        }
    }


    public class parseBloggersInBackground extends AsyncTask<Void, Void, Void>{

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

