package in.aqel.blogger;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class ReadLaterFragment extends Fragment {


    public ReadLaterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllReadLater();
        if (cur.getCount() >0){
            view = inflater.inflate(R.layout.fragment_favourites, container, false);
            ListView list = (ListView) view.findViewById(R.id.listView);
            ReadLaterAdapter customAdapter = new ReadLaterAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
        }else {
            view = inflater.inflate(R.layout.favoutrites_null, container, false);
            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvHeader.setText("You dont have any Read Later posts.");
            tvMessage.setText("To add a post to Read Later tab, go to Menu and click on Read Later");
        }
        data.close();
        return view;
    }


}
