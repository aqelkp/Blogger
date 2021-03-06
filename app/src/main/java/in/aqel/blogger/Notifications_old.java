package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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


public class Notifications_old extends Fragment {
    public Notifications_old() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        setHasOptionsMenu(true);
        SharedPreferences lastNotif = getActivity().getSharedPreferences("lastNotif", Context.MODE_PRIVATE);
        int loadingFirst = lastNotif.getInt("loadingFirst", 0);
        int parsedNotif = lastNotif.getInt("parsedNotif", 0);

        ListView list = (ListView) view.findViewById(R.id.listView);

        if (loadingFirst == 0){
         /*
            Log.d("Loaded", "Its first time");
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            //(String blogId, int bloggerId, String content, String date , String title, String blogger_name
            String[] blogId = DataCollections_posts.blogId;
            int[] bloggerId = DataCollections_posts.bloggerId;
            int[] blogsCateg = DataCollections_posts.blogsCateg;
            String[] content = DataCollections_posts.content;
            String[] date = DataCollections_posts.date;
            String[] title = DataCollections_posts.title;
            String[] blogger_name = DataCollections_posts.blogger_name;
            String[] links = DataCollections_posts.links;
            for (int i = 0; i < bloggerId.length;i++){
                String contentString = Html.fromHtml(content[i]).toString();
              //  data.addNotifications(blogId[i], bloggerId[i], contentString, date[i], title[i], blogger_name[i], links[i]);
                data.addPost(blogId[i], bloggerId[i], contentString, date[i], title[i], title[i], blogsCateg[i]);
            }
            data.close();
            SharedPreferences.Editor editor = lastNotif.edit();
            editor.putInt("loadingFirst", 1);
            editor.commit();*/
        }


        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllNotifications();
        if (cur.getCount() != 0) {
            NotificationLIstAdapter_old customAdapter = new NotificationLIstAdapter_old(getActivity(), cur);
            //CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
        }
        data.close();
        return view;

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
            new parseNotifications().execute();
        }

        return super.onOptionsItemSelected(item);

    }

    public class parseNotifications extends AsyncTask<Void, Void, Void>{
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
            final SharedPreferences lastNotif = getActivity().getSharedPreferences("lastNotif", Context.MODE_PRIVATE);
            final int notification_number = lastNotif.getInt("lastNotif",0);
            final SharedPreferences.Editor editor = lastNotif.edit();
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("posts_test");
            query.whereGreaterThan("notification_number", notification_number);
            query.setLimit(20);
            // Sorts the results in descending order by the score field
            query.orderByDescending("date");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        editor.putInt("parsedNotif", 1);
                        editor.commit();
                        Log.d("score", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(getActivity());

                        int new_notif_num = notification_number;
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            try{
                                data.open();
                                if (notification_number < parseList.getInt("notification_number")){
                                    new_notif_num = parseList.getInt("notification_number");
                                }

                                Log.d("notification Number", Integer.toString(parseList.getInt("notification_number")));
                                //String content  = Html.fromHtml(
                                // parseList.getString("content")).toString();
                                String content = parseList.getString("content");
                                Log.d("content", content);

                               // data.addNotifications(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name"), parseList.getString("link"));
                                data.addPost(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("link"), parseList.getInt("categ"));
                                data.close();
                            }catch (NullPointerException es){
                                es.printStackTrace();
                            }

                        }

                        editor.putInt("lastNotif", new_notif_num);
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
            ListView list = (ListView) getView().findViewById(R.id.listView);
                DatabaseHelper data = new DatabaseHelper(getActivity());
                data.open();
                Cursor cur = data.getAllNotifications();
                Log.d("here", "Here");
                if (cur!= null) {
                    NotificationLIstAdapter_old customAdapter = new NotificationLIstAdapter_old(getActivity(), cur);
                    list.setAdapter(customAdapter);
                }else{
                    Log.d("Null", "Null Pointer");
                }
                data.close();

        }
    }

}
