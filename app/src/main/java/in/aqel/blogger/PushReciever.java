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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;


public class PushReciever extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new parseNotifications().execute();
        //new getXMLAsync().execute();
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
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("posts");
            query.setLimit(20);
            // Sorts the results in descending order by the score field
            query.orderByDescending("date");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(PushReciever.this);
                        data.open();
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            data.addNotifications(parseList.getString("post_id"), parseList.getInt("blog_id"), parseList.getString("content"), parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name"));
                            data.addPost(parseList.getString("post_id"), parseList.getInt("blog_id"), parseList.getString("content"), parseList.getString("date"), parseList.getString("title"));
                        }
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
            Intent intent = new Intent(PushReciever.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }
    }

    public class getXMLAsync extends AsyncTask<Void, Void, Void> {
        static final String URL = "http://oorkkadavu.blogspot.com/feeds/posts/default";
        // XML node keys
        static final String KEY_ITEM = "entry"; // parent node
        static final String KEY_ID = "id";
        static final String KEY_TITLE = "title";
        static final String KEY_CONTENT = "content";
        static final String KEY_DATE = "published";
        SharedPreferences xmlStore;
        String[] id, title, content, date;
        StringFormatter converter = new StringFormatter();
        private ProgressDialog pDialog;
        DatabaseHelper data = new DatabaseHelper(PushReciever.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PushReciever.this);
            pDialog.setMessage("Updating");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            XMLParser parser = new XMLParser();
            String xml = null;

            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            SharedPreferences xmlStore = getSharedPreferences("xmlStore", Context.MODE_PRIVATE);

            if (activeInfo != null && activeInfo.isConnected()) {
                xml = parser.getXmlFromUrl(URL);
                Log.d("xml", xml);
                SharedPreferences.Editor editor = xmlStore.edit();
                editor.putString("xml", xml);
                editor.putInt("loadedInt", 1);
                editor.commit();
                Document doc = parser.getDomElement(xml); // getting DOM element
                NodeList nl = doc.getElementsByTagName(KEY_ITEM);
                // looping through all item nodes <item>

                id = new String[nl.getLength()];
                title = new String[nl.getLength()];
                content = new String[nl.getLength()];
                date = new String[nl.getLength()];

                data.open();

                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    id[i] = parser.getValue(e, KEY_ID);
                    String titleString = converter.ConvertToMalayalam(parser.getValue(e, KEY_TITLE));
                    title[i] = titleString;
                    String contentString = parser.getValue(e, KEY_CONTENT);
                    content[i] = contentString;
                    date[i] = parser.getValue(e, KEY_DATE);
                    data.addPost(id[i],1, content[i], date[i], title[i]);
                }
                data.close();
            } else {
                Toast.makeText(PushReciever.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }


            Log.d("reached", "here");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            Intent intent = new Intent(PushReciever.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            super.onPostExecute(aVoid);
        }


    }
}
