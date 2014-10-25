package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class PlaceHolderActivity extends ActionBarActivity {

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
        int id = extras.getInt("id");
        String blogger_name_en = extras.getString("blogger_name_en");
        String blog_name_en = extras.getString("blog_name_en");
        String blog_name_mal = extras.getString("blog_name_mal");
        Boolean loadedOrNot= extras.getBoolean("loadedOrNot");
        String blog_url = extras.getString("blog_url");
        String blogger_name_mal = extras.getString("blogger_name_mal");

        int blogger_photos[] = {R.drawable.blogger_photo_1, R.drawable.blogger_photo_2, R.drawable.blogger_photo_3};
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(blogger_photos[id -1]);
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
            }else{
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();

            }
        }





           /* if (cur != null) {

            }else{*/




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.place_holder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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
        String[] id,title,content, date ;
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
            Log.d("Load", "Loading Async Task");
            XMLParser parser = new XMLParser();
            String xml;
            String URL = params[0]+"/feeds/posts/default";
            Log.d("url", URL);
            SharedPreferences xmlStore = getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()) {
                xml = parser.getXmlFromUrl(URL);
                if (!xml.equals("sorry")) {
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
                        //String key = e.getAttributes().getNamedItem("link").getNodeValue();
                        //Log.d("Key", key);
                        id[i] = parser.getValue(e, KEY_ID);
                        Log.d("id", id[i]);
                        String titleString = parser.getValue(e, KEY_TITLE);
                        title[i] = titleString;
                        String contentString = parser.getValue(e, KEY_CONTENT);
                        content[i] = contentString;
                        String dateString = parser.getValue(e, KEY_DATE);
                        date[i] = dateString;
                        data.addPost(id[i], Integer.parseInt(params[1]), content[i], dateString, title[i]);
                        Log.d("date", params[1]);
                    }
                    data.updateAsLoaded(bloggerId);
                    data.close();
                }
            }else{
                Toast.makeText(PlaceHolderActivity.this , "Check your internet connection", Toast.LENGTH_SHORT).show();
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

}
