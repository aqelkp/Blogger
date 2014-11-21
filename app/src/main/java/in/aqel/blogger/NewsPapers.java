package in.aqel.blogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class NewsPapers extends Fragment implements View.OnClickListener {
    public NewsPapers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_papers, container, false);
        ImageButton ibManorama = (ImageButton) view.findViewById(R.id.ibManorama);
        ImageButton ibMadyamam = (ImageButton) view.findViewById(R.id.ibMadhyamam);
        ImageButton ibMathrbhoomi = (ImageButton) view.findViewById(R.id.ibMathrbhoomi);
        ImageButton ibDeshabhimani = (ImageButton) view.findViewById(R.id.ibDeshabhimani);
        ImageButton ibSubrabhatham = (ImageButton) view.findViewById(R.id.ibSuprabhatham);
        ImageButton ibKeralaKaumuthi = (ImageButton) view.findViewById(R.id.ibKaumudi);
        ImageButton ibChandrika = (ImageButton) view.findViewById(R.id.ibChandrika);
        ImageButton ibJanmaBhoomi = (ImageButton) view.findViewById(R.id.ibJanmaBhoomi);
        ImageButton ibKVartha = (ImageButton) view.findViewById(R.id.ibKVartha);
        ImageButton ibVeekshanam = (ImageButton) view.findViewById(R.id.ibVeekshanam);
        ImageButton ibSiraj = (ImageButton) view.findViewById(R.id.ibSiraj);
        ibChandrika.setOnClickListener(this);
        ibJanmaBhoomi.setOnClickListener(this);
        ibKVartha.setOnClickListener(this);
        ibVeekshanam.setOnClickListener(this);
        ibSiraj.setOnClickListener(this);
        ibManorama.setOnClickListener(this);
        ibMadyamam.setOnClickListener(this);
        ibMathrbhoomi.setOnClickListener(this);
        ibDeshabhimani.setOnClickListener(this);
        ibSubrabhatham.setOnClickListener(this);
        ibKeralaKaumuthi.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        Intent intent = new Intent(getActivity(), NewsPaperLoading.class);
        switch (buttonId){
            case R.id.ibManorama:
                intent.putExtra("link", "http://m.manoramaonline.com/");
                intent.putExtra("name", "Malayala Manorama");
                startActivity(intent);
                break;
            case R.id.ibMathrbhoomi:
                intent.putExtra("link", "http://m.mathrubhumi.com/");
                intent.putExtra("name", "Mathrubhumi");
                startActivity(intent);
                break;
            case R.id.ibMadhyamam:
                intent.putExtra("link", "http://www.madhyamam.com/");
                intent.putExtra("name", "Madhyamam");
                startActivity(intent);
                break;
            case R.id.ibDeshabhimani:
                intent.putExtra("link", "http://www.deshabhimani.com/");
                intent.putExtra("name", "Deshabhimani");
                startActivity(intent);
                break;
            case R.id.ibSuprabhatham:
                intent.putExtra("link", "http://suprabhaatham.com/");
                intent.putExtra("name", "Suprabhaatham");
                startActivity(intent);
                break;
            case R.id.ibKaumudi:
                intent.putExtra("link", "http://news.keralakaumudi.com/");
                intent.putExtra("name", "Kerala Kaumudi");
                startActivity(intent);
                break;
            case R.id.ibChandrika:
                intent.putExtra("link", "http://www.chandrikadaily.com/");
                intent.putExtra("name", "Chandrika");
                startActivity(intent);
                break;
            case R.id.ibSiraj:
                intent.putExtra("link", "http://www.sirajlive.com/");
                intent.putExtra("name", "Siraj");
                startActivity(intent);
                break;
            case R.id.ibJanmaBhoomi:
                intent.putExtra("link", "http://www.janmabhumidaily.com/");
                intent.putExtra("name", "Janmabhumi");
                startActivity(intent);
                break;
            case R.id.ibKVartha:
                intent.putExtra("link", "www.kvartha.com");
                intent.putExtra("name", "KVartha");
                startActivity(intent);
                break;
            case R.id.ibVeekshanam:
                intent.putExtra("link", "http://veekshanam.com/");
                intent.putExtra("name", "Veekshanam");
                startActivity(intent);
                break;

        }
    }
}
