package in.aqel.blogger;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class CategoryViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Delete this activity
        /*setContentView(R.layout.activity_category_view);
        int oneDay = 1000*3600*24;
        Bundle extras = getIntent().getExtras();
        final String categName = extras.getString("categ_name");
        final int categ = extras.getInt("categ");
        SharedPreferences parsedCategNotif = getSharedPreferences("parsedCategNotif", MODE_PRIVATE);
        int parsedOrNot = parsedCategNotif.getInt("parsedCategNotifOrNot"+ categName, 0);
        Long lastParsed = parsedCategNotif.getLong("parsedNotifTime"+categName,0);
        if (parsedOrNot == 0){
            new parseNotifications().execute(categName, Integer.toString(categ));
            //Log.d(DataCollections_bloggers.blogger_photos[i], "Yes");
        }else if (System.currentTimeMillis()> lastParsed + oneDay){
            Log.d("AqelLog", "Load again");
            new parseNotifications().execute(categName, Integer.toString(categ));
        }else{
            //Log.d("Categ", "No");
            ListView list = (ListView) findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(this);
            data.open();
            Cursor cur = data.getAllCategData(categ);
            CustomCursorAdapter customAdapter = new CustomCursorAdapter(this, cur);
            list.setAdapter(customAdapter);
            data.close();
        }*/

    }

    public class parseNotifications extends AsyncTask<String, Void, Void> {
        private ProgressDialog pDialog;
        int categ;
        String categName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CategoryViewActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            /*categ  = Integer.parseInt(params[1]);
            categName = params[0];
            Log.d("Log Parse", "parsing");
            SharedPreferences parsedCategNotif = getSharedPreferences("parsedCategNotif", MODE_PRIVATE);
            final int lastParsedNum = parsedCategNotif.getInt("lastParsedNum"+ categName, 0);
            final SharedPreferences.Editor editor = parsedCategNotif.edit();
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("posts_test");
           query.whereEqualTo("categ", categ);
            query.whereGreaterThan("notification_number", lastParsedNum);
            query.setLimit(20);
            // Sorts the results in descending order by the score field
            query.orderByDescending("date");

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (e == null) {
                        Log.d("here", "Its here");
                        editor.putInt("parsedNotif", 1);
                        editor.commit();
                        Log.d("score", "Retrieved " + userList.size() + " posts");
                        DatabaseHelper data = new DatabaseHelper(CategoryViewActivity.this);

                        int new_notif_num = lastParsedNum;
                        for (int i=0; i<userList.size(); i++){
                            ParseObject parseList = userList.get(i);
                            try{
                                data.open();
                                if (new_notif_num < parseList.getInt("notification_number")){
                                    new_notif_num = parseList.getInt("notification_number");
                                }

                                Log.d("notification Number", Integer.toString(parseList.getInt("notification_number")));
                                //String content  = Html.fromHtml(
                                // parseList.getString("content")).toString();
                                String content = parseList.getString("content");
                                Log.d("content", content);

                                data.addNotifications(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("blogger_name"), parseList.getString("link"));
                                data.addPost(parseList.getString("post_id"), parseList.getInt("blog_id"), content, parseList.getString("date"), parseList.getString("title"), parseList.getString("link"), parseList.getInt("categ"));
                                //data.close();
                                ListView list = (ListView) findViewById(R.id.listView);
                                //DatabaseHelper data = new DatabaseHelper(CategoryViewActivity.this);
                                //data.open();
                                Cursor cur = data.getAllCategData(categ);
                                Log.d("here", "Here");
                                if (cur!= null) {
                                    CustomCursorAdapter customAdapter = new CustomCursorAdapter(CategoryViewActivity.this, cur);
                                    list.setAdapter(customAdapter);
                                }else{
                                    Log.d("Null", "Null Pointer");
                                }
                                data.close();
                                pDialog.dismiss();

                            }catch (NullPointerException es){
                                es.printStackTrace();
                            }

                        }

                        editor.putInt("parsedCategNotifOrNot"+ categName, new_notif_num);
                        editor.putLong("parsedNotifTime"+categName, System.currentTimeMillis());
                        editor.commit();
                    } else {
                        e.printStackTrace();
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



        }
    }

}
