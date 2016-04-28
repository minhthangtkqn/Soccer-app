package com.example.tlds.testwebservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Matchs_View extends AppCompatActivity {

    private String Username;
    ListView listView;
    List<Match> matches = new ArrayList<Match>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchs__view);

        Intent caller = getIntent();
        Bundle pack = caller.getBundleExtra(LoginActivity.KEY_BUNDLE_USER);
        Username = pack.getString(LoginActivity.KEY_USER);

        listView = (ListView)findViewById(R.id.listMatch);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ViewMatches().execute("http://minhthangtkqn-001-site1.1tempurl.com/matches.php");
            }
        });


    }

    class ViewMatches extends AsyncTask<String, Integer, String>{



        @Override
        protected String doInBackground(String... params) {
            return LoginActivity.readContentFromURL(params[0]);

        }


        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){
                    JSONObject match = array.getJSONObject(i);

                    Match tran = new Match(match.getString("field_id").toString(), match.getString("maximum_players").toString(), match.getString("price").toString());
                    matches.add(tran);
                    listView.setAdapter(new CustomListAdapter(Matchs_View.this, matches));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
