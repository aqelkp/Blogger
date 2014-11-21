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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class NotificationsAdatpter extends CursorAdapter {
    Context contextThis;
    public NotificationsAdatpter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.notifications_single_item, parent, false);

        return retView;
    }

    String[] blogger = new String[8];
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        contextThis = context;
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        //KEY_ROWID, KEY_BLOGGER_ID, KEY_BLOG_NAME, KEY_DATE, KEY_TITLE, KEY_READ_OR_NOT, "blogger_name",
        // KEY_CATEGORY};
        final int notif_id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
        final int blogger_id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(1)));



        final String blogName = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
        final String strDate = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        final String strTitle = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        final int readOrNot = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(5)));

        final String bloggerName = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)));
        final String category = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7)));
        Typeface font = Typeface.createFromAsset(context.getAssets(), "karthika.TTF");

        final TextView title = (TextView) view.findViewById(R.id.tvTitle);
        TextView content = (TextView) view.findViewById(R.id.tvContent);
        title.setTypeface(font);
        title.setText(strTitle);
        String toBePrinted = "<b>"+ bloggerName + "</b>" + " wrote a " + category + " on " +
                "<b>"+ blogName + "</b>"+ " on " + strDate;
        content.setText(Html.fromHtml(toBePrinted));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        if (readOrNot== 1) {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.not_read));
        }else{
            linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper data = new DatabaseHelper(context);
                data.open();
                blogger = data.getBlogger(blogger_id);
                data.close();
                if (readOrNot== 0) {
                    Log.d("LogAq id loaded", Integer.toString(blogger_id));
                    ConnectivityManager connMgr =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
                    if (activeInfo != null && activeInfo.isConnected()) {
                        new getXMLAsync().execute(blogger[4],Integer.toString(blogger_id), Integer.toString(notif_id), strTitle);
                    }else {
                        Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent intent = new Intent(contextThis, PlaceHolderActivity.class);
                    intent.putExtra("id", Integer.parseInt(blogger[5]));
                    intent.putExtra("isFav", Integer.parseInt(blogger[6]));
                    intent.putExtra("loadedOrNot", true);
                    intent.putExtra("blogger_name_en", blogger[0]);
                    intent.putExtra("blogger_name_mal", blogger[1]);
                    intent.putExtra("blog_name_en", blogger[2]);
                    intent.putExtra("blog_name_mal", blogger[3]);
                    intent.putExtra("previous", "notifications");
                    contextThis.startActivity(intent);
                }
            }
        });

    }

    public class getXMLAsync extends AsyncTask<String, Void, Void> {
        // XML node keys
        static final String KEY_ITEM = "entry"; // parent node
        static final String KEY_ID = "id";
        static final String KEY_TITLE = "title";
        static final String KEY_CONTENT = "content";
        static final String KEY_DATE = "published";
        SharedPreferences xmlStore;
        String[] id, title, content, date, links;
        String[] idRss, titleRss, contentRss, dateRss, linksRss, post;
        StringFormatter converter = new StringFormatter();
        private ProgressDialog pDialog;
        DatabaseHelper data = new DatabaseHelper(contextThis);
        int bloggerId, notifId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(contextThis);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            bloggerId = Integer.parseInt(params[1]);
            notifId = Integer.parseInt(params[2]);
            xmlStore = contextThis.getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
            XMLParser parser = new XMLParser();
            String xml;
            String URL = params[0] + "/feeds/posts/default";
            Log.d("url", URL);
            SharedPreferences xmlStore = contextThis.getSharedPreferences("xmlStore", Context.MODE_PRIVATE);

            xml = parser.getXmlFromUrl(URL);

            try {
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
                    id[i] = parser.getValue(e, KEY_ID);
                    Log.d("id", id[i]);
                    String titleString = parser.getValue(e, KEY_TITLE);
                    title[i] = titleString;
                    String contentString = parser.getValue(e, KEY_CONTENT);
                    content[i] = contentString;
                    links[i] = parser.getLink(e);

                    data.addPost(id[i], Integer.parseInt(params[1]), content[i], dateString, title[i], links[i], 0);
                    Log.d("date", params[1]);
                }
                if (nl.getLength() < 2) {
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
                        idRss[i] = parser.getValue(e, "guid");
                        String titleString = parser.getValue(e, "title");
                        titleRss[i] = titleString;
                        String contentString = parser.getValue(e, "description");
                        contentRss[i] = contentString;
                        linksRss[i] = parser.getValue(e, "link");
                        data.addPost(idRss[i], Integer.parseInt(params[1]), contentRss[i], dateString, titleRss[i], linksRss[i], 0);
                    }
                }
                data.updateAsLoaded(bloggerId);
                data.markNotifAsRead(notifId);
                post = data.getThePost(params[3]);
                Log.d("LogAq here", "its ater "+ params[3]);
                data.close();
                SharedPreferences lastUpdatedTime = contextThis.getSharedPreferences("lastUpdatedTime", contextThis.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = lastUpdatedTime.edit();
                editor2.putLong("lastUpdatedTime_" + bloggerId, System.currentTimeMillis());
                editor2.commit();
                Intent intent = new Intent(contextThis, PlaceHolderActivity.class);
                //Here is the error
                Boolean loadedOrNot = Integer.parseInt(blogger[7])>0;
                intent.putExtra("id", Integer.parseInt(blogger[5]));
                intent.putExtra("isFav", Integer.parseInt(blogger[6]));
                intent.putExtra("loadedOrNot", true);
                intent.putExtra("blogger_name_en", blogger[0]);
                intent.putExtra("blogger_name_mal", blogger[1]);
                intent.putExtra("blog_name_en", blogger[2]);
                intent.putExtra("blog_name_mal", blogger[3]);
                intent.putExtra("previous", "notifications");
                contextThis.startActivity(intent);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            Log.d("reached", "here");

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            try{


            }catch (NullPointerException e){
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }


}
