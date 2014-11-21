package in.aqel.blogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment implements View.OnClickListener{
     public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO Delete this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button bStory = (Button) view.findViewById(R.id.bStory);
        Button bPoem = (Button) view.findViewById(R.id.bPoem);
        Button bExperience = (Button) view.findViewById(R.id.bExperience);
        Button bRecentStory = (Button) view.findViewById(R.id.bRecent);
        Button bTravel = (Button) view.findViewById(R.id.bTravelExperience);
        Button bOthers = (Button) view.findViewById(R.id.bOther);
        bStory.setOnClickListener(this);
        bPoem.setOnClickListener(this);
        bExperience.setOnClickListener(this);
        bRecentStory.setOnClickListener(this);
        bTravel.setOnClickListener(this);
        bOthers.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getActivity(), CategoryViewActivity.class);
        switch (v.getId()){
            case R.id.bStory:
                Log.d("hi", "hey");
                intent.putExtra("categ", 1);
                intent.putExtra("categ_name", "Stories");
                startActivity(intent);
                break;
            case R.id.bPoem:
                Log.d("hi", "hey");
                intent.putExtra("categ", 2);
                intent.putExtra("categ_name", "Poems");
                startActivity(intent);
                break;
            case R.id.bExperience:
                Log.d("hi", "hey");
                intent.putExtra("categ", 3);
                intent.putExtra("categ_name", "Experiences");
                startActivity(intent);
                break;
            case R.id.bRecent:
                Log.d("hi", "hey");
                intent.putExtra("categ", 4);
                intent.putExtra("categ_name", "Recent Stories");
                startActivity(intent);
                break;
        }

    }
}
