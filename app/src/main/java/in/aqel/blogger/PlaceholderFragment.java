package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SharedPreferences xmlStore = getActivity().getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
        if (xmlStore.getInt("loadedInt", 0) == 0){
            ConnectivityManager connMgr =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isConnected()) {
                new getXMLAsync().execute();
            }else{
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }else {
            ListView list = (ListView) rootView.findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllData(2);
            if (cur != null) {
                CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
                list.setAdapter(customAdapter);
            }
            data.close();
        }
        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new getXMLAsync().execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
        String[] id,title,content, date ;
        StringFormatter converter = new StringFormatter();
        private ProgressDialog pDialog;
        DatabaseHelper data = new DatabaseHelper(getActivity());

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
            xmlStore = getActivity().getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
            XMLParser parser = new XMLParser();
            String xml = null;


                SharedPreferences xmlStore = getActivity().getSharedPreferences("xmlStore", Context.MODE_PRIVATE);
            ConnectivityManager connMgr =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
                if (activeInfo != null && activeInfo.isConnected()) {
                    xml = parser.getXmlFromUrl(URL);
                    if (!xml.equals("sorry")) {
//                    Log.d("xml", xml);
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

                        for (int i = nl.getLength()-1; i >= 0; i--) {
                            Element e = (Element) nl.item(i);

                            id[i] = parser.getValue(e, KEY_ID);
                            String titleString = parser.getValue(e, KEY_TITLE);
                            title[i] = titleString;
                            String contentString = parser.getValue(e, KEY_CONTENT);
                            content[i] = contentString;
                            String dateString = parser.getValue(e, KEY_DATE);
                            date[i] = dateString;
                            data.addPost(id[i],2 ,content[i], dateString, title[i]);
                            Log.d("date", title[i]);
                        }
                        data.close();
                    }
                }else{
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                }



            Log.d("reached","here");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            //CustomListAdapter adapter = new
             //       CustomListAdapter(getActivity(), id, title, title, date);
            ListView list=(ListView) getView().findViewById(R.id.listView);

            data.open();
            Cursor cur = data.getAllData(2);
            if (cur != null) {
                CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
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

