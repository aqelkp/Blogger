package in.aqel.blogger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class BookSellers extends Fragment implements View.OnClickListener{
    public BookSellers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_sellers, container, false);
        // Inflate the layout for this fragment
        ImageButton ib1 = (ImageButton) view.findViewById(R.id.ib1);
        ib1.setOnClickListener(this);
        ImageButton ib2 = (ImageButton) view.findViewById(R.id.ib2);
        ib2.setOnClickListener(this);
        ImageButton ib3 = (ImageButton) view.findViewById(R.id.ib3);
        ib3.setOnClickListener(this);
        ImageButton ib4 = (ImageButton) view.findViewById(R.id.ib4);
        ib4.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        String url = null;
        switch (buttonId){
            case R.id.ib1:
                url = "http://www.indulekha.com/";
                break;
            case R.id.ib2:
                url = "http://onlinestore.dcbooks.com/";
                break;
            case R.id.ib3:
                url = "http://www.keralabookstore.com/";
                break;
            case R.id.ib4:
                url = "http://buy.mathrubhumi.com/books/";
                break;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
