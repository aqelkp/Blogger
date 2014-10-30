package in.aqel.blogger;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FavouritesFragment extends Fragment {


    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllFavourites();
        if (cur.getCount()>0){
            rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
            ListView list = (ListView) rootView.findViewById(R.id.listView);
            CustomCursorAdapter customAdapter = new CustomCursorAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
        }else{
            rootView = inflater.inflate(R.layout.favoutrites_null, container, false);

        }

        data.close();
        return rootView;
    }


}
