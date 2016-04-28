package com.example.tlds.testwebservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile_Information extends AppCompatActivity {

    private String Username;
    TextView txtUsername, txtEmail, txtPhone;
    Button btnMatchList, btnMyMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__information);

        Intent intentCaller = getIntent();
        Bundle pack = intentCaller.getBundleExtra(LoginActivity.KEY_BUNDLE_USER);
        Username = pack.getString(LoginActivity.KEY_USER);

        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtPhone = (TextView)findViewById(R.id.txtPhone);

        btnMatchList = (Button)findViewById(R.id.btnMatchList);
        btnMyMatch = (Button)findViewById(R.id.btnMyMatch);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getJSon_Info().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
            }
        });

        btnMatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Information.this, Matchs_View.class);

                Bundle bundle = new Bundle();
                bundle.putString(LoginActivity.KEY_USER, txtUsername.getText().toString());
                intent.putExtra(LoginActivity.KEY_BUNDLE_USER, bundle);

                startActivity(intent);
            }
        });
    }



    class getJSon_Info extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            return LoginActivity.readContentFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){
                    JSONObject profile = array.getJSONObject(i);

                    if(Username.equals(profile.getString("username").toString())){
                        txtPhone.setText(profile.getString("phone_number").toString());
                        txtEmail.setText(profile.getString("email").toString());
                        txtUsername.setText(profile.getString("username").toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
