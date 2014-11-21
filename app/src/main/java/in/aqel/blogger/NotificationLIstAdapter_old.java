package in.aqel.blogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationLIstAdapter_old extends CursorAdapter {

    public NotificationLIstAdapter_old(Context context, Cursor c) {
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
        //final int isFav = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(7)));

        final String strDate = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        //final String strContent = Html.fromHtml(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)))).toString();
        final String strContent = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        final String strTitle = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));
        final String postId = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        final int id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
        final int blogger_id = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2)));
        final String timeStamp = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        final String link = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(10)));
        final int isFav = cursor.getInt(cursor.getColumnIndex("isFav"));

        Typeface font = Typeface.createFromAsset(context.getAssets(), "karthika.TTF");

        TextView title = (TextView) view.findViewById(R.id.tvTitle);
        title.setTypeface(font);
        title.setText(strTitle);

        TextView content = (TextView) view.findViewById(R.id.tvContent);
        /*
        String str = strContent;

        str = str.replaceAll("<a href(.*?)\\>","");
        str = str.replaceAll("<img(.*?)\\>","");

       // str = Html.fromHtml(str).toString();
        if (str.length() > 500) {
            str =  str.substring(0, Math.min(str.length(), 500)) + "...";

        }
        content.setTypeface(font);


        content.setText(Html.fromHtml(str));
        //String strPro = converter.ConvertToMalayalam(content.getText().toString());
        //content.setText(strPro);
*/
        final String strShort = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(9)));
        content.setText(strShort);
        String blogger_name = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7)));

       TextView date = (TextView) view.findViewById(R.id.tvDate);
        long shift_timezone= (long) (5.5*3600*1000); // shifts from GMT to IST timezone.
        //assuming all users are in India. too lazy to find dedicted fn.
        Date dateb = null;
        String dateString = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));

        try {
            //2014-10-10T21:56:00.000+03:00

            dateString = dateString.substring(0, dateString.length()-6);
            dateb = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss").parse(dateString);
            dateString = (String) DateUtils.getRelativeTimeSpanString(dateb.getTime() + shift_timezone);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        final String dateToBePassed = dateString;
        date.setText(blogger_name+ " wrote on " +dateString);


        //final int a = cursor.getInt(cursor.getColumnIndex("isFav"));



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", strTitle);
                intent.putExtra("content", strContent);
                intent.putExtra("id", id);
                intent.putExtra("date", dateToBePassed);
                intent.putExtra("dateStamp", timeStamp);
                intent.putExtra("postId", postId);
                intent.putExtra("isFav", isFav);
                intent.putExtra("link", link);
                intent.putExtra("blogger_id", blogger_id);
                intent.putExtra("activityBefore", "notifications");
                context.startActivity(intent);
            }
        });

    }
}
