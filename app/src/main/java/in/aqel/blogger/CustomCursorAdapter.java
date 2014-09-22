package in.aqel.blogger;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
 
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.single_list_item, parent, false);
 
        return retView;
    }
 
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        Typeface font = Typeface.createFromAsset(context.getAssets(), "karthika.ttf");

        TextView title = (TextView) view.findViewById(R.id.tvTitle);
        title.setTypeface(font);
        title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
 
        TextView content = (TextView) view.findViewById(R.id.tvContent);
        String str = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
        MalalyalamConverter converter = new MalalyalamConverter();
        str = converter.ConvertToMalayalam(Html.fromHtml(str).toString());
        if (str.length() > 300) {
            str = str.substring(0, Math.min(str.length(), 300)) + "...";
        }
        content.setTypeface(font);


        content.setText(str);
        //String strPro = converter.ConvertToMalayalam(content.getText().toString());
        //content.setText(strPro);
        
        TextView date = (TextView) view.findViewById(R.id.tvDate);
        date.setTypeface(font);
        date.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));

        final String strDate = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        final String strContent = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
        final String strTitle = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", strTitle);
                intent.putExtra("content", strContent);
                intent.putExtra("id",cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                intent.putExtra("date", strDate);
                context.startActivity(intent);
            }
        });
    }
}
