package in.aqel.blogger;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.PushService;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "li4FMZCln6gtkgvRGO6BElCg69OrJ5BmJTKs3kqv", "gEoGoeFgLshWUSv4qlRjtpC140wF3Rjq8jugarIT");
        PushService.setDefaultPushCallback(this, PushReciever.class);

        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position){
            case 1:
                Fragment fragment = new AboutBlogger();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
                setTitle("About Me");
                break;
            case 0:
                Fragment fragmentHome = new PlaceholderFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentHome).commit();
                setTitle("Home");
                break;
            case 2:
                Fragment fragmentContacts = new ContactFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentContacts).commit();
                setTitle("Contact Me");
                break;
            case 3:
                Fragment fragmentAboutAqel = new AboutAqelFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragmentAboutAqel).commit();
                setTitle("Aqel Ahammad");
                break;
            case 4:
                super.finish();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Home";

                break;
            case 2:
                mTitle = "About Me";
                Toast.makeText(MainActivity.this, "Second", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.contact_call:
                Intent callIntent = new Intent(android.content.Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "+966 50 657 7642"));
                startActivity(callIntent);
                break;

            case R.id.contact_message:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + "+966 50 657 7642"));
                startActivity(smsIntent);
                break;
            case R.id.contact_facebook:
                Intent fbIntent = getOpenFacebookIntent(MainActivity.this,"faisal.babu.7374");
                startActivity(fbIntent);
                break;
            case R.id.contact_facebook_aqel:
                Intent fbIntentAqel = getOpenFacebookIntent(MainActivity.this,"aqelkp");
                startActivity(fbIntentAqel);
                break;
            case R.id.contact_google:
                openGPlus("114428535690141984682");
                break;
            case R.id.contact_email:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String emailid[] = { "faisalbabuk@gmail.com" };
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailid);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Oorkadavu Blog Feedback");
                emailIntent.setType("plain/text");
                startActivity(emailIntent);
                break;
            case R.id.contact_email_aqel:
                Intent emailIntentaq = new Intent(android.content.Intent.ACTION_SEND);
                String emailAqel[] = { "aqel123@gmail.com" };
                emailIntentaq.putExtra(android.content.Intent.EXTRA_EMAIL, emailAqel);
                emailIntentaq.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Oorkadavu Blog");
                emailIntentaq.setType("plain/text");
                startActivity(emailIntentaq);
            case R.id.contact_reprot_bug:
                Intent emailIntentBug = new Intent(android.content.Intent.ACTION_SEND);
                String emailAqelBug[] = { "aqel123@gmail.com" };
                emailIntentBug.putExtra(android.content.Intent.EXTRA_EMAIL, emailAqelBug);
                emailIntentBug.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Oorkadavu Blog Bug Report");
                emailIntentBug.setType("plain/text");
                startActivity(emailIntentBug);
                break;
            default:
                Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_SHORT).show();

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
