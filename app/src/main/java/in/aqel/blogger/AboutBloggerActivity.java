package in.aqel.blogger;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class AboutBloggerActivity extends ActionBarActivity {
    String blogger_name_mal, description, phone_number, email, google_plus, facebook,blogger_name_en;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        intent.putExtra("id", id);
                intent.putExtra("blogger_name_en", bloggerNameEn);
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
                intent.putExtra("blog_url", blogUrl);
         */
        setContentView(R.layout.fragment_about_blogger);

        DataCollections collectionData = new DataCollections();
        int images[] = collectionData.images();
        Bundle extras = getIntent().getExtras();
         id = extras.getInt("id");
         blogger_name_mal= extras.getString("blogger_name_mal");
        blogger_name_en =extras.getString("blogger_name_en");
         description = extras.getString("description");
         phone_number = extras.getString("phone_number");
         email = extras.getString("email");
         google_plus = extras.getString("google_plus");
         facebook = extras.getString("facebook");
        getSupportActionBar().setTitle("About " + blogger_name_en);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView Name = (TextView) findViewById(R.id.tvName);
        TextView AboutMe = (TextView) findViewById(R.id.tvDescription);
        Typeface font = Typeface.createFromAsset(getAssets(), "karthika.TTF");
        Name.setTypeface(font);
        AboutMe.setTypeface(font);

        Name.setText(blogger_name_mal);
        AboutMe.setText(description);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_call:
                Intent callIntent = new Intent(android.content.Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                startActivity(callIntent);
                break;

            case R.id.contact_message:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + phone_number));
                startActivity(smsIntent);
                break;
            case R.id.contact_facebook:
                Intent fbIntent = getOpenFacebookIntent(AboutBloggerActivity.this,facebook);
                startActivity(fbIntent);
                break;



            case R.id.contact_google:
                openGPlus(google_plus);
                break;
            case R.id.contact_email:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String emailid[] = { email };
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailid);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Blog Feedback");
                emailIntent.setType("plain/text");
                startActivity(emailIntent);
                break;
            default:
                Toast.makeText(AboutBloggerActivity.this, "Invalid", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    public static Intent getOpenFacebookIntent(Context context, String user) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + user));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + user));
        }
    }



    public void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"+profile+"/posts")));
        }
    }


}
