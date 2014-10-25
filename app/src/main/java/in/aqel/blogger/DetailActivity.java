package in.aqel.blogger;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    int isFav, bloggerId;
    String blogId,  title, content, dateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        DatabaseHelper data = new DatabaseHelper(DetailActivity.this);
        data.open();
        //blogId = extras.getInt("id");
        //isFav = extras.getInt("isFav");
        blogId = extras.getString("postId");
        isFav = data.checkIsFav(blogId);
        //Log.d("id", Integer.toString(data.getId(extras.getString("postId"))));

        data.close();
        dateString= extras.getString("date");
        title = extras.getString("title");
        content= extras.getString("content");
        bloggerId = extras.getInt("blogger_id");
        //isFav = extras.getInt("isFav");
        //Log.d("blogId", Integer.toString(blogId));
        Log.d("isFav On Activity", Integer.toString(isFav));
        Log.d("isFav On Extras", Integer.toString(extras.getInt("isFav")));
        setContentView(R.layout.detail_view);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        //TextView tvContent = (TextView) findViewById(R.id.tvContent);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "karthika.TTF");
        //tvContent.setTypeface(font);
        tvTitle.setTypeface(font);
        tvDate.setText(extras.getString("date"));
        tvTitle.setText(extras.getString("title"));

        //tvContent.setText(extras.getString("content"));
        WebView webview = (WebView) findViewById(R.id.wvContent);
        String htmlContent = "  <style type=\"text/css\"> \n" +
                "   @font-face { \n" +
                "       font-family: MyFont; \n" +
                "       src: url(\"file:///android_asset/karthika.TTF\") \n" +
                "   } \n" +
                "   body { \n" +
                "       font-family: MyFont; \n" +
            //    "       font-size: 18px; \n" +
                "       text-align: justify; \n line-height: 140%;\n" +
                "   } \n" +
                "  </style> \n" + extras.getString("content");
        webview.loadDataWithBaseURL("file:///android_asset/", htmlContent , "text/html", "UTF-8", "null" );
        //DatabaseHelper data  = new DatabaseHelper(this);
        //data.open();
        //data.updatePostAsRead(extras.getString("id"));
        //data.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_view, menu);
        if (isFav == 0) {
            menu.getItem(1).setIcon(android.R.drawable.btn_star_big_off);
        }else if (isFav == 1){
            menu.getItem(1).setIcon(android.R.drawable.btn_star_big_on);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favourite) {
            if (isFav == 0) {
                item.setIcon(android.R.drawable.btn_star_big_on);
                isFav = 1;
                DatabaseHelper data = new DatabaseHelper(DetailActivity.this);
                data.open();
                data.addToFavourites(blogId, isFav);
                data.close();
                Log.d("isFav Saved", Integer.toString(isFav));
            }else if (isFav == 1){
                item.setIcon(android.R.drawable.btn_star_big_off);
                isFav = 0;
                DatabaseHelper data = new DatabaseHelper(DetailActivity.this);
                data.open();
                data.addToFavourites(blogId, isFav);
                data.close();
                Log.d("isFav Saved", Integer.toString(isFav));
            }
            return true;
        }else if(id== R.id.action_read_later){
          //  (String blogId, int bloggerId, String content, String date , String title)
            DatabaseHelper data = new DatabaseHelper(DetailActivity.this);
            data.open();
            data.addToReadLater(blogId, bloggerId, content, dateString, title);
            data.close();
        }
        return super.onOptionsItemSelected(item);
    }
}
