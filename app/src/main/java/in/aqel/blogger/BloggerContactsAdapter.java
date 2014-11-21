package in.aqel.blogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BloggerContactsAdapter extends CursorAdapter {

    public BloggerContactsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.blogger_list_single_item, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        int blogger_photos[] = DataCollections_bloggers.blogger_photos;



        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        Typeface font = Typeface.createFromAsset(context.getAssets(), "karthika.TTF");

        TextView blogName = (TextView) view.findViewById(R.id.tvBlogName);
        blogName.setTypeface(font);
        final String blogNameMal = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        blogName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));

        TextView bloggerName = (TextView) view.findViewById(R.id.tvBlogger);
        bloggerName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        final int id  = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
        final String bloggerNameEn = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        final String bloggerNameMal = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
        final String blogNameEn = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        //String blogNameMal = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        final String description = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));
        final String phoneNumber = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)));
        final String gmail = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7)));
        final String googlePlus = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(8)));
        final String facebookId = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(9)));
        final int weight = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(10)));
        final Boolean loadedOrNot = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(11)))>0;
        final String blogUrl = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(12)));

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        int numBloggers = DataCollections_bloggers.bloggerNameEn.length;
        if (id>numBloggers){
            image.setImageResource(R.drawable.blooger_photo_gen);
        }else{
            image.setImageResource(blogger_photos[id -1]);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AboutBloggerActivity.class);
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
                context.startActivity(intent);
            }
        });
    }
}
