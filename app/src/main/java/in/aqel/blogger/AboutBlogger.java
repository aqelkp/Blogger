package in.aqel.blogger;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class AboutBlogger extends Fragment {


    public AboutBlogger() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
       /* TextView Name = (TextView) getView().findViewById(R.id.tvName);
        TextView AboutMe = (TextView) getView().findViewById(R.id.textView2);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "karthika.TTF");
        Name.setTypeface(font);
        AboutMe.setTypeface(font);
        //StringFormatter converter = new StringFormatter();
        String name = getResources().getString(R.string.faisalMal);
        String aboutMe = getResources().getString(R.string.aboutMeMal);
        //name = converter.ConvertToMalayalam(name);
        //aboutMe = converter.ConvertToMalayalam(aboutMe);
        Name.setText(name);
        AboutMe.setText(aboutMe);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_blogger, container, false);
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_bloggers_list, container, false);



            ListView list = (ListView) rootView.findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllBloggers();
            if (cur != null) {
                BloggerContactsAdapter customAdapter = new BloggerContactsAdapter(getActivity(), cur);
                for (cur.moveToLast(); !cur.isBeforeFirst(); cur.moveToPrevious()) {

                }
                list.setAdapter(customAdapter);
            }
            data.close();



        return rootView;
    }


}
