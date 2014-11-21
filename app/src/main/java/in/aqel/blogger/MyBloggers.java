package in.aqel.blogger;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class MyBloggers extends Fragment {

    public MyBloggers() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId){
            case R.id.action_addNew:
                Intent intent = new Intent(getActivity(), NewBlogger.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.my_bloggers, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view;
        DatabaseHelper data = new DatabaseHelper(getActivity());
        data.open();
        Cursor cur = data.getAllMyBloggers();
        if (cur.getCount()>0){
            view = inflater.inflate(R.layout.fragment_bloggers_list, container, false);
            ListView list = (ListView) view.findViewById(R.id.listView);
            BloggerCursorAdapter customAdapter = new BloggerCursorAdapter(getActivity(), cur);
            list.setAdapter(customAdapter);
        }else{
            view = inflater.inflate(R.layout.favoutrites_null, container, false);
            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvHeader.setText("You haven't add any blogger to My Bloggers");
            tvMessage.setText("Click on the + button on the action bar to add new bloggers.");
        }
        if (cur != null) {

        }
        data.close();



        return view;
    }


}
