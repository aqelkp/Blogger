package in.aqel.blogger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloggersList extends Fragment {

    public BloggersList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_bloggers_list, container, false);


        SharedPreferences firstLogin = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        if (!firstLogin.getBoolean("LoadedOrNot", false)) {
            new storeBloggerDetails().execute();
        } else {

            ListView list = (ListView) rootView.findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllBloggers();
            if (cur != null) {
                BloggerCursorAdapter customAdapter = new BloggerCursorAdapter(getActivity(), cur);
                for (cur.moveToLast(); !cur.isBeforeFirst(); cur.moveToPrevious()) {
                    Log.d("string", cur.getString(cur.getColumnIndex(cur.getColumnName(1))));

                }
                Log.d("cursor", "cursor not null");
                list.setAdapter(customAdapter);
            }
            data.close();


        }
        return rootView;
    }


    public class storeBloggerDetails extends AsyncTask<Void, Void,Void>{
        private ProgressDialog pDialog;
        String bloggerNameEn[] = {"Basheer Vallikunnu","Faisal Babu", "Akbar Ali"};
        String bloggerNameMal[]= {"ബഷീർ വല്ലിക്കുന്ന് ", "ഫൈസൽ ബാബു", "അക്ബർ അലി "};
        String blogNameEn[] = {"Vallikunnu", "Oorkadavu", "Chaliyar"};
        String blogNameMal[] = {"വല്ലിക്കുന്ന് ", "ഊർക്കടവ് ", "ചാലിയാർ"};
        String description[] = {"സീരിയസ്സായി പറഞ്ഞാല്‍ സൗമ്യനും സല്‍സ്വഭാവിയും സുന്ദരനുമായ ചെറുപ്പക്കാരന്‍ , ഡീസന്റ് പാര്‍ട്ടി. :)",
                "എന്നെക്കുറിച്ച് ഞാനെന്ത് പറഞ്ഞാലും നിങ്ങള്‍ പറയും, \"അവന്റെയൊരു പൊങ്ങച്ചം\" എന്ന് . വല്ലതും പറയാതിരുന്നാലോ?, \"അവന്റെയൊരു ജാഡ\" എന്നും... എന്ത് തോന്നിയാലും ഞാന്‍ ‍പറയാം, ജീവിതത്തിന്‍റെ രണ്ടറ്റവും കൂട്ടിമുട്ടിക്കാന്‍ പ്രവാസ ജീവിതം നയിക്കുന്ന അനേകരില്‍ ഒരാളായി സൗദിയിലെ കുന്‍ഫുദ എന്ന കൊച്ചു പട്ടണത്തില്‍ പ്രവാസിയായി ഞാനും ......",
                "മലപ്പുറം കോഴിക്കോട് ജില്ലകള്‍ അതിര്‍ത്തി പങ്കിടുന്ന ചാലിയാര്‍ തീരത്ത്, ഹരിത മനോഹരമായ ഒരു വാഴക്കാടന്‍ ഉള്‍ഗ്രാമത്തില്‍ ജനനം. കളിച്ചും ചിരിച്ചും അല്ലലില്ലാതെ വളര്‍ന്ന ബാല്യ കൗമാരങ്ങള്‍ക്കുമേൽ ജീവിത യാഥാര്‍ത്ഥ്യങ്ങളുടെ ഉഷ്ണക്കാറ്റ് വീശിത്തുടങ്ങിയപ്പോള്‍ പ്രവാസത്തിന്റെ യാന്ത്രിക ജീവിതത്തിലേക്ക് വഴുതിപ്പോയ യൗവ്വനം. ഇപ്പോള്‍ സൗദി അറേബ്യയില്‍ ജീവിതത്തിന്‍റെ ഭാഗധേയം തേടി പ്രവാസം തുടരുന്നു. "
        };
        String phoneNumber[] = {"+966 50 255 9726","+966506577642","+966508636322"};
        String gmail[] = {"vallikkunnu@gmail.com", "faisalbabuk@gmail.com","akbar.ali.3538@facebook.com"};
        String googlePlus[] = {"115687498578590743703","114428535690141984682", "103805942713504922361"};
        String facebookId[] = { "vallikkunnu" ,"faisal.babu.7374","akbar.ali.3538"};
        int weight[] = {90,80,75};
        String blogUrl[] = {"http://www.vallikkunnu.com", "http://oorkkadavu.blogspot.in", "http://chaliyaarpuzha.blogspot.in"};

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper data= new DatabaseHelper(getActivity());
            data.open();
            for (int i = 0; i < bloggerNameEn.length; i++){
                data.addBlogger(bloggerNameEn[i], bloggerNameMal[i], blogNameEn[i], blogNameMal[i], description[i], phoneNumber[i], gmail[i],
                        googlePlus[i], facebookId[i], weight[i], 0 ,blogUrl[i]);
            }
            data.close();
            SharedPreferences firstLogin = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = firstLogin.edit();
            editor.putBoolean("LoadedOrNot", true);
            editor.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            ListView list = (ListView) getView().findViewById(R.id.listView);
            DatabaseHelper data = new DatabaseHelper(getActivity());
            data.open();
            Cursor cur = data.getAllBloggers();
            if (cur != null) {
                BloggerCursorAdapter customAdapter = new BloggerCursorAdapter(getActivity(), cur);
                list.setAdapter(customAdapter);
            }
            data.close();

        }


    }
}

