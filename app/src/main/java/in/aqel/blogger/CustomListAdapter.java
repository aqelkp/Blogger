package in.aqel.blogger;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by aqel on 17/9/14.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] id;
    private final String[] title;
    private final String[] content;
    private final String[] date;
    StringFormatter converter = new StringFormatter();
    public CustomListAdapter(Activity context,
                             String[] id, String[] title, String[] content, String[] date) {
        super(context, R.layout.single_list_item, id);
        this.context = context;
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.single_list_item, null, true);
        TextView tvDate = (TextView) rowView.findViewById(R.id.tvDate);
        TextView tvContent = (TextView) rowView.findViewById(R.id.tvContent);
        TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        tvDate.setText(date[position]);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "karthika.TTF");
        tvContent.setTypeface(font);
        String str = content[position];
//        if (str.length() > 300) {
//            str = str.substring(0, Math.min(str.length(), 300)) + "...";
//        }
        tvContent.setText(str);
         tvTitle.setTypeface(font);
        tvTitle.setText(title[position]);

        return rowView;
    }
}
