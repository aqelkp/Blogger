package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;


public class PlaceHolderActivity extends ActionBarActivity {
    String blog_url, previous;
    int id, isFav, isFavValNew;
// new getXMLAsync().execute( blog_url , Integer.toString(id));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main);
        Bundle extras = getIntent().getExtras();
        /*
        intent.putExtra("blogger_name_mal", bloggerNameMal);
        intent.putExtra("blog_name_en", blogNameEn);
        intent.putExtra("blog_name_mal", blogNameMal);
        intent.putExtra("description", description);
        intent.putExtra("phone_number", phoneNumber);
        intent.putExtra("email", gmail);
        intent.putExtra("google_plus", googlePlus);
        intent.putExtra("facebook", facebookId);
        intent.putExtra("weight", weight);
        intent.putExtra("loadedOrNot", loadedOrNot);
        intent.putExtra("blog_url", blogUrl);*/
        id = extras.getInt("id");
        isFav = extras.getInt("isFav");
        //intent.putExtra("previous", "notifications");
        previous = extras.getString("previous");
        String blogger_name_en = extras.getString("blogger_name_en");
        String blog_name_en = extras.getString("blog_name_en");
        String blog_name_mal = extras.getString("blog_name_mal");
        Boolean loadedOrNot= extras.getBoolean("loadedOrNot");
        blog_url = extras.getString("blog_url");
        String blogger_name_mal = extras.getString("blogger_name_mal");


        SharedPreferences lastUpdatedTime = getSharedPreferences("lastUpdatedTime", MODE_PRIVATE);
        Long lastUpdated = lastUpdatedTime.getLong("lastUpdatedTime_"+id,0);
        int oneWeek = 1000*60*60*24*7;





        int blogger_photos[] = DataCollections_bloggers.blogger_photos;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        int numBloggers = DataCollections_bloggers.bloggerNameEn.length;
        if (id>numBloggers){
            image.setImageResource(R.drawable.blooger_photo_gen);
        }else{
            image.setImageResource(blogger_photos[id -1]);
        }
        Typeface font = Typeface.createFromAsset(getAssets(), "karthika.TTF");

        TextView blogName = (TextView) findViewById(R.id.tvBlogName);
        blogName.setTypeface(font);
        blogName.setText(blog_name_mal);

        TextView bloggerName = (TextView) findViewById(R.id.tvBlogger);
        bloggerName.setText(blogger_name_en);


        getSupportActionBar().setTitle(blog_name_en);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences xmlStore = getSharedPreferences("xmlStore", Context.MODE_PRIVATE);

        if (loadedOrNot){
            ListView list = (ListView) findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(this);
            data.open();
            Cursor cur = data.getAllData(id);
            CustomCursorAdapter customAdapter = new CustomCursorAdapter(this, cur);
            list.setAdapter(customAdapter);
            data.close();
        }else{
            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()) {
                new getXMLAsync().execute( blog_url , Integer.toString(id));
                Log.d("Log", "Loading First time");
            }else{
                Toast.makeText(this, "Connect to internet to download the blog.", Toast.LENGTH_SHORT).show();
                ListView list = (ListView) findViewById(R.id.listView);
                DatabaseHelper data = new DatabaseHelper(this);
                data.open();
                Cursor cur = data.getAllData(id);
                CustomCursorAdapter customAdapter = new CustomCursorAdapter(this, cur);
                list.setAdapter(customAdapter);
                data.close();
            }


        }

        //Updateing if last loaded is one week before
        if ((System.currentTimeMillis()>lastUpdated + oneWeek ) && (loadedOrNot) ){
            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()) {
                new getXMLAsync().execute( blog_url , Integer.toString(id));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.place_holder, menu);
        switch (isFav){
            case 0:
                menu.getItem(0).setTitle("Add to My Bloggers");
                isFavValNew= 1;
                break;
            case 1:
                menu.getItem(0).setTitle("Remove From My Bloggers");
                isFavValNew= 0;
                break;
            case 5:
                menu.getItem(0).setTitle("Delete this blog");

                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goBack = new Intent(PlaceHolderActivity.this, MainActivity.class);
        if (isFav == 5) {
            goBack.putExtra("previous", "myBloggers");
        }else{
            goBack.putExtra("previous", "placeHolder");
        }
        startActivity(goBack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idOfMenu = item.getItemId();
        switch (idOfMenu){
            case android.R.id.home:
                Intent goBack = new Intent(PlaceHolderActivity.this, MainActivity.class);
                if (previous.equals("notifications")){
                    goBack.putExtra("previous", "notifications");
                } else if (isFav == 0) {
                    goBack.putExtra("previous", "placeHolder");
                }else{
                    goBack.putExtra("previous", "myBloggers");
                }
                startActivity(goBack);
                break;
            case R.id.action_refresh:
                    ConnectivityManager connMgr =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
                    if (activeInfo != null && activeInfo.isConnected()) {
                        new getXMLAsync().execute( blog_url , Integer.toString(id));
                    }
                break;
            case R.id.action_addToFav:
                Log.d("LogAqel Is fav ", "isFav " + isFav + " Id " + id);
                DatabaseHelper data = new DatabaseHelper(PlaceHolderActivity.this);
                switch (isFav){
                    case 1:
                        item.setTitle("Add to My Bloggers");
                        isFav = 0;
                        data.open();
                        data.addToMyBloggers(id, isFavValNew);
                        data.close();
                        break;
                    case 0:
                        item.setTitle("Remove From My Bloggers");
                        isFav = 1;
                        data.open();
                        data.addToMyBloggers(id, isFavValNew);
                        data.close();
                        break;
                    //5 is the case of bloggers added by users
                    case 5:
                        data.open();
                        data.deleteMyBlogger(id);
                        data.close();
                        Intent intent = new Intent(PlaceHolderActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class getXMLAsync extends AsyncTask<String, Void, Void> {
        // XML node keys
        static final String KEY_ITEM = "entry"; // parent node
        static final String KEY_ID = "id";
        static final String KEY_TITLE = "title";
        static final String KEY_CONTENT = "content";
        static final String KEY_DATE = "published";
        SharedPreferences xmlStore;
        String[] id,title,content, date, links ;
        String[] idRss,titleRss,contentRss, dateRss, linksRss ;
        StringFormatter converter = new StringFormatter();
        private ProgressDialog pDialog;
        DatabaseHelper data = new DatabaseHelper(PlaceHolderActivity.this);
        int bloggerId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlaceHolderActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            bloggerId = Integer.parseInt(params[1]);
            xmlStore = getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
            XMLParser parser = new XMLParser();
            String xml;
            String URL = params[0]+"/feeds/posts/default";
            Log.d("url", URL);
            SharedPreferences xmlStore = getSharedPreferences("xmlStore", Context.MODE_PRIVATE);

                xml = parser.getXmlFromUrl(URL);

            try{
                Log.d("LogAqel xml", xml);
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
                links = new String[nl.getLength()];

                data.open();

                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    String dateString = parser.getValue(e, KEY_DATE);
                    date[i] = dateString;
                    //String idNew = params[1]+"_"+ dateString.substring(0,4)+"_"+dateString.substring(5,7)+"_"+dateString.substring(8,10)+"_1";
                    id[i] =  parser.getValue(e, KEY_ID);
                    Log.d("id", id[i]);
                    String titleString = parser.getValue(e, KEY_TITLE);
                    title[i] = titleString;
                    String contentString = parser.getValue(e, KEY_CONTENT);
                    content[i] = contentString;
                    links[i] = parser.getLink(e);

                    data.addPost(id[i], Integer.parseInt(params[1]), content[i], dateString, title[i], links[i], 0);
                    Log.d("date", params[1]);
                }
                if (nl.getLength() < 2){
                    Log.d("LogAqel", "Its RSS Feed I guess");
                    NodeList nlRss = doc.getElementsByTagName("item");
                    // looping through all item nodes <item>
                    idRss = new String[nlRss.getLength()];
                    titleRss = new String[nlRss.getLength()];
                    contentRss = new String[nlRss.getLength()];
                    dateRss = new String[nlRss.getLength()];
                    linksRss = new String[nlRss.getLength()];
                    for (int i = 0; i < nlRss.getLength(); i++) {
                        Element e = (Element) nlRss.item(i);
                        String dateString = parser.getValue(e, "atom:updated");
                        if (dateString.equals(null)){
                            dateString = parser.getValue(e, "pubDate");
                        }
                        dateRss[i] = dateString;
                        //String idNew = params[1]+"_"+ dateString.substring(0,4)+"_"+dateString.substring(5,7)+"_"+dateString.substring(8,10)+"_1";
                        idRss[i] =  parser.getValue(e, "guid");
                        String titleString = parser.getValue(e, "title");
                        titleRss[i] = titleString;
                        String contentString = parser.getValue(e, "description");
                        contentRss[i] = contentString;
                        linksRss[i] = parser.getValue(e, "link");
                        data.addPost(idRss[i], Integer.parseInt(params[1]), contentRss[i], dateString, titleRss[i], linksRss[i], 0);
                    }
                }
                data.updateAsLoaded(bloggerId);
                data.close();
                SharedPreferences lastUpdatedTime = getSharedPreferences("lastUpdatedTime", MODE_PRIVATE);
                SharedPreferences.Editor editor2  = lastUpdatedTime.edit();
                editor2.putLong("lastUpdatedTime_"+bloggerId, System.currentTimeMillis());
                editor2.commit();

            }catch (NullPointerException e){
                e.printStackTrace();
            }





            Log.d("reached","here");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            //CustomListAdapter adapter = new
            //       CustomListAdapter(getActivity(), id, title, title, date);
            ListView list=(ListView) findViewById(R.id.listView);

            data.open();
            Cursor cur = data.getAllData(bloggerId);
            if (cur != null) {
                CustomCursorAdapter customAdapter = new CustomCursorAdapter(PlaceHolderActivity.this, cur);
                list.setAdapter(customAdapter);
            }
            data.close();


            //  list.setAdapter(adapter);
            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("title", title[i]);
                    intent.putExtra("content", content[i]);
                    intent.putExtra("id",id[i]);
                    intent.putExtra("date", date[i]);
                    startActivity(intent);
                }
            });*/
            super.onPostExecute(aVoid);
        }



    }

    public class parseUpdates extends AsyncTask<Integer, Void, Void>{
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlaceHolderActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(final Integer... params) {
            final SharedPreferences lastNotif = getSharedPreferences("lastNotif", Context.MODE_PRIVATE);
            final int notification_number = lastNotif.getInt("lastNotif_"+ params[0],0);
            final SharedPreferences.Editor editor = lastNotif.edit();
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("posts_test");
            query.whereEqualTo("blog_id", params[0] );
            query.whereGreaterThan("notification_number", notification_number);
            query.setLimit(20);
            // Sorts the results in descending order by the score field
            query.orderByDescending("date");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        editor.putInt("parsedNotif", 1);
                        editor.commit();
                        Log.d("Log", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(PlaceHolderActivity.this);

                        int new_notif_num = notification_number;
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            try{
                                data.open();
                                if (notification_number < parseList.getInt("notification_number")){
                                    new_notif_num = parseList.getInt("notification_number");
                                }

                                Log.d("Log", "Notification Number" + Integer.toString(parseList.getInt("notification_number")));
                                //String content  = Html.fromHtml(
                                // parseList.getString("content")).toString();
                                String content = parseList.getString("content");
                                Log.d("Log Content", content);

                            //    data.addNotifications(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name"), parseList.getString("link"));
                                data.addPost(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("link"), parseList.getInt("categ"));
                                data.close();
                            }catch (NullPointerException es){
                                es.printStackTrace();
                            }

                        }

                        editor.putInt("lastNotif_"+params[0], new_notif_num);
                        editor.commit();

                    } else {
                        Log.d("Log", "Error: " + e.getMessage());
                    }
                    SharedPreferences lastUpdatedTime = getSharedPreferences("lastUpdatedTime", MODE_PRIVATE);
                    SharedPreferences.Editor editor2  = lastUpdatedTime.edit();
                    editor2.putLong("lastUpdatedTime_"+id, System.currentTimeMillis());
                    editor2.commit();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            ListView list = (ListView) findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(PlaceHolderActivity.this);
            data.open();
            Cursor cur = data.getAllData(id);
            CustomCursorAdapter customAdapter = new CustomCursorAdapter(PlaceHolderActivity.this, cur);
            list.setAdapter(customAdapter);
            data.close();

        }
    }


}
