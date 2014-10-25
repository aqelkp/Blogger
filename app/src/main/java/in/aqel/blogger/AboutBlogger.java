package in.aqel.blogger;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutBlogger extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




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
        TextView Name = (TextView) getView().findViewById(R.id.tvName);
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
        AboutMe.setText(aboutMe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_blogger, container, false);
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_about_blogger, container, false);
    }


}
