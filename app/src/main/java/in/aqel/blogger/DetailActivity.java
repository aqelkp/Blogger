package in.aqel.blogger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class DetailActivity extends ActionBarActivity {

    int isFav, bloggerId;
    String blogId,  title, content, dateString, activityBefore, timeStamp, link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (!(activeInfo != null && activeInfo.isConnected())) {
            LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
            adLayout.getLayoutParams().height = 1;
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "karthika.TTF");

        Bundle extras = getIntent().getExtras();
        timeStamp = extras.getString("dateStamp");
        dateString= extras.getString("date");
        title = extras.getString("title");
        content= extras.getString("content");
        bloggerId = extras.getInt("blogger_id");
        blogId = extras.getString("postId");
        Log.d("Blogger id", Integer.toString(bloggerId));
        link = extras.getString("link");
        DatabaseHelper data = new DatabaseHelper(DetailActivity.this);
        data.open();
        //blogId = extras.getInt("id");
        //isFav = extras.getInt("isFav");
        isFav = data.checkIsFav(blogId);
        activityBefore = extras.getString("activityBefore");
        String[] bloggerData = data.getBlogger(bloggerId);

        data.close();
        TextView tvBlogName = (TextView) findViewById(R.id.tvBlogName);
        TextView tvBloggerName = (TextView) findViewById(R.id.tvBlogger);
        tvBloggerName.setTypeface(font);
        tvBlogName.setTypeface(font);

        tvBloggerName.setText(bloggerData[3]);
        tvBlogName.setText(bloggerData[1]);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        int[] blogger_photos = DataCollections_bloggers.blogger_photos;
        int numBloggers = DataCollections_bloggers.bloggerNameEn.length;
        if (bloggerId>numBloggers){
            image.setImageResource(R.drawable.blooger_photo_gen);
        }else{
            image.setImageResource(blogger_photos[bloggerId -1]);
        }
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        //TextView tvContent = (TextView) findViewById(R.id.tvContent);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
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
                "   } img{display: inline; height: auto; max-width: 90%;} " +
                "   body { \n" +
                "       font-family: MyFont; \n" +
            //    "       font-size: 18px; \n" +
                "       text-align: justify; \n line-height: 140%;\n" +
                "   } \n" +
                "  </style> \n" + extras.getString("content");
        webview.loadDataWithBaseURL("file:///android_asset/", htmlContent , "text/html", "UTF-8", "null" );
        Button bViewComplete = (Button) findViewById(R.id.bComments);
        bViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
                if (activeInfo != null && activeInfo.isConnected()) {
                    Intent intent = new Intent (DetailActivity.this, ViewCompletePost.class);
                    intent.putExtra("link", link);
                    intent.putExtra("dateStamp", timeStamp);
                    intent.putExtra("date", dateString);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("bloggerId", bloggerId);
                    intent.putExtra("blogId", blogId);
                    intent.putExtra("isFav", isFav);
                    startActivity(intent);
                }else{
                    Toast.makeText(DetailActivity.this, "Connect to internet to view comments.", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
        Log.d("activity Before", activityBefore);
        if (activityBefore.equals("readLater")){
            menu.getItem(0).setTitle("Mark As Read");

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
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }



    }

}
