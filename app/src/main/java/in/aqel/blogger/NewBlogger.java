package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class NewBlogger extends ActionBarActivity {
    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_new_blogger);
        final EditText etBlog = (EditText) findViewById(R.id.etBlog);
        final EditText etBlogger = (EditText) findViewById(R.id.etBloggerName);
        final EditText etLink = (EditText) findViewById(R.id.etLink);
        Button bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blog = etBlog.getText().toString();
                String blogger = etBlogger.getText().toString();
                String link = etLink.getText().toString();
                SharedPreferences lastId = getSharedPreferences("lastId", MODE_PRIVATE);
                int id = lastId.getInt("lastId", 100000);
                ConnectivityManager connMgr =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                Boolean isError = false;
                NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
                if (activeInfo != null && activeInfo.isConnected()) {
                    new getXMLAsync().execute(link, Integer.toString(id + 1), blog, blogger);
                }else {
                    Toast.makeText(NewBlogger.this, "Cannot connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(NewBlogger.this, MainActivity.class);
            intent.putExtra("previous", "myBloggers");
            startActivity(intent);
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
        String[] id,title,content, date, links ;
        String[] idRss,titleRss,contentRss, dateRss, linksRss ;
        StringFormatter converter = new StringFormatter();
        private ProgressDialog pDialog;
        DatabaseHelper data = new DatabaseHelper(NewBlogger.this);
        int bloggerId;
        Boolean canBeAdded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewBlogger.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            bloggerId = Integer.parseInt(params[1]);
            XMLParser parser = new XMLParser();
            String xml;
            String URL = params[0]+"/feeds/posts/default";

                xml = parser.getXmlFromUrl(URL);

                try{
                    Log.d("LogAqel xml", xml);
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
                        canBeAdded = true;
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
                            dateRss[i] = dateString;
                            //String idNew = params[1]+"_"+ dateString.substring(0,4)+"_"+dateString.substring(5,7)+"_"+dateString.substring(8,10)+"_1";
                            idRss[i] =  parser.getValue(e, "guid");
                            String titleString = parser.getValue(e, "title");
                            titleRss[i] = titleString;
                            String contentString = parser.getValue(e, "description");
                            contentRss[i] = contentString;
                            linksRss[i] = parser.getValue(e, "link");
                            data.addPost(idRss[i], Integer.parseInt(params[1]), contentRss[i], dateString, titleRss[i], linksRss[i], 0);
                            canBeAdded = true;
                        }
                    }
                    data.updateAsLoaded(bloggerId);
                    data.close();
                    SharedPreferences lastUpdatedTime = getSharedPreferences("lastUpdatedTime", MODE_PRIVATE);
                    SharedPreferences.Editor editor2  = lastUpdatedTime.edit();
                    editor2.putLong("lastUpdatedTime_"+bloggerId, System.currentTimeMillis());
                    editor2.commit();
                    Intent intent = new Intent(NewBlogger.this, MainActivity.class);
                    intent.putExtra("previous", "myBloggers");
                    startActivity(intent);

                }catch (NullPointerException e){

                    e.printStackTrace();
                }


            if (canBeAdded){
                Log.d("LogAqel Can be added", "Yes");
                SharedPreferences lastId = getSharedPreferences("lastId", MODE_PRIVATE);
                data.open();
                int newId = Integer.parseInt(params[1]);
                data.addNewBlogger(newId,params[2], params[3],params[0]);
                SharedPreferences.Editor editor = lastId.edit();
                editor.putInt("lastId", newId);
                editor.commit();
            }else {
                Log.d("LogAqel Can be added", "No");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            TextView tvError = (TextView) findViewById(R.id.tvError);
            tvError.setVisibility(View.VISIBLE);
            if (canBeAdded){
                Toast.makeText(NewBlogger.this, "This blog has been successfully added", Toast.LENGTH_SHORT);
            }else {
                Toast.makeText(NewBlogger.this, "Blog not added. Check the link and internet connection", Toast.LENGTH_SHORT);
            }
            super.onPostExecute(aVoid);
        }



    }


}
