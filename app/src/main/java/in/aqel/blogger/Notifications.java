package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllNotifications();
        Boolean loadedBefore = false;
        if (cur.getCount() != 0) {
            NotificationLIstAdapter customAdapter = new NotificationLIstAdapter(getActivity(), cur);
            //CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
            loadedBefore = true;
        }
        data.close();
        if (!loadedBefore){
            data.close();
            new parseNotifications().execute();
        }

        return view;

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
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("posts");
            query.whereGreaterThan("notification_number", notification_number);
            query.setLimit(20);
            // Sorts the results in descending order by the score field
            query.orderByDescending("date");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(getActivity());
                        data.open();
                        int new_notif_num = notification_number;
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            if (notification_number < parseList.getInt("notification_number")){
                                new_notif_num = parseList.getInt("notification_number");
                            }

                            Log.d("notification Number", Integer.toString(parseList.getInt("notification_number")));
                            String content  = Html.fromHtml(parseList.getString("content")).toString();
                            Log.d("content", content);

                            data.addNotifications(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name"));
                            data.addPost(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"));
                        }
                        SharedPreferences.Editor editor = lastNotif.edit();
                        editor.putInt("lastNotif", new_notif_num);
                        editor.commit();
                        data.close();
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
                    NotificationLIstAdapter customAdapter = new NotificationLIstAdapter(getActivity(), cur);
                    list.setAdapter(customAdapter);
                }else{
                    Log.d("Null", "Null Pointer");
                }
                data.close();

        }
    }

}
