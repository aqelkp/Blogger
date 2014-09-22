package in.aqel.blogger;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.detail_view);
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        //TextView tvContent = (TextView) findViewById(R.id.tvContent);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "karthika.ttf");
        //tvContent.setTypeface(font);
        tvTitle.setTypeface(font);
        tvDate.setText(extras.getString("date"));
        tvTitle.setText(extras.getString("title"));
        //tvContent.setText(extras.getString("content"));
        WebView webview = (WebView) findViewById(R.id.wvContent);
        String htmlContent = "  <style type=\"text/css\"> \n" +
                "   @font-face { \n" +
                "       font-family: MyFont; \n" +
                "       src: url(\"file:///android_asset/karthika.ttf\") \n" +
                "   } \n" +
                "   body { \n" +
                "       font-family: MyFont; \n" +
                "       font-size: 18px; \n" +
                "       text-align: justify; \n line-height: 140%;\n" +
                "   } \n" +
                "  </style> \n" + extras.getString("content");
        webview.loadDataWithBaseURL("file:///android_asset/", htmlContent , "text/html", "UTF-8", "null" );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
}
