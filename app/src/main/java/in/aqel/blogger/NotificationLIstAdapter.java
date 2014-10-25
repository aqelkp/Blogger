package in.aqel.blogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NotificationLIstAdapter extends CursorAdapter {

    public NotificationLIstAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.notifications_single_list, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        Typeface font = Typeface.createFromAsset(context.getAssets(), "karthika.TTF");

        TextView title = (TextView) view.findViewById(R.id.tvTitle);
        title.setTypeface(font);
        title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));

        TextView content = (TextView) view.findViewById(R.id.tvContent);
        String str = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        str = str.replaceAll("<a href(.*?)\\>"," ");
        str = str.replaceAll("<img(.*?)\\>"," ");

        Log.d("string", str);
        StringFormatter converter = new StringFormatter();
        str = Html.fromHtml(str).toString();
        if (str.length() > 300) {
            str =  str.substring(0, Math.min(str.length(), 300)) + "...";

        }
        content.setTypeface(font);


        content.setText(Html.fromHtml(str));
        //String strPro = converter.ConvertToMalayalam(content.getText().toString());
        //content.setText(strPro);

        String blogger_name = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7)));

        TextView date = (TextView) view.findViewById(R.id.tvDate);
        final String dateString = converter.ConvertDate(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
        date.setText(blogger_name+ " wrote on " +dateString);


        //final int isFav = cursor.getInt(cursor.getColumnIndex("isFav"));
        //final int a = cursor.getInt(cursor.getColumnIndex("isFav"));
        final int isFav = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(7)));
        final String strDate = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        final String strContent = Html.fromHtml(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)))).toString();
        final String strTitle = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));
        final String postId = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        final int id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
        final int blogger_id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2)));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", strTitle);
                intent.putExtra("content", strContent);
                intent.putExtra("id", id);
                intent.putExtra("date", dateString);
                intent.putExtra("postId", postId);
                intent.putExtra("isFav", isFav);
                intent.putExtra("blogger_id", blogger_id);
                context.startActivity(intent);
            }
        });
    }
}
