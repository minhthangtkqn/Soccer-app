package com.example.tlds.testwebservice;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnShowPassword;
    EditText inputPassword, inputUserName;
    TextView signUp;
    Toast successLogin, failLogin;
    CheckBox rememberMe;

    public static final String KEY_BUNDLE_USER = "user package";
    public static final String KEY_USER = "user";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER_ME = "remember me";

    public void login(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getJSONLogin().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnShowPassword = (Button)findViewById(R.id.btnShowPassword);
        inputPassword = (EditText)findViewById(R.id.editPassword);
        inputUserName = (EditText)findViewById(R.id.editUserName);
        signUp = (TextView)findViewById(R.id.signUp);
        rememberMe = (CheckBox)findViewById(R.id.rememberMe);

        successLogin = Toast.makeText(LoginActivity.this, "Login Success!!!", Toast.LENGTH_SHORT);
        failLogin = Toast.makeText(LoginActivity.this, "Login Failed (+_+)", Toast.LENGTH_SHORT);


        //Nhấn giữ để hiển thị password
        btnShowPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        inputPassword.setInputType(inputPassword.getInputType() ^ InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(getApplicationContext(), Sign_Up.class);
                startActivity(signUpActivity);
            }
        });
    }

    public static String readContentFromURL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    class getJSONLogin extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readContentFromURL(params[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            try {
                if(!validate())
                    return;
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){

                    //Mỗi profile là một tài khoản
                    JSONObject profile = array.getJSONObject(i);

                    //So sánh với CSDL để đăng nhập
                    if(inputUserName.getText().toString().equals(profile.getString("username").toString() )
                            &&  inputPassword.getText().toString().equals(profile.getString("password").toString() ) )
                    {
                        successLogin.show();
                        savingPreferences();
                        openProfileActivity();
                        break;
                    }
                    if(i == array.length()-1)
                    {
                        failLogin.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void openProfileActivity(){
            Intent profile_information = new Intent(LoginActivity.this, Profile_Information.class);

            Bundle bundle = new Bundle();
            bundle.putString(KEY_USER, inputUserName.getText().toString());
            profile_information.putExtra(KEY_BUNDLE_USER, bundle);

            startActivity(profile_information);
        }

        private boolean validate(){                 //kiem tra dieu kien cua ten tai khoan va mat khau
            boolean value = true;
            if(inputUserName.getText().toString().trim().isEmpty()){
                inputUserName.setError("Enter UserName");
                value = false;
            }
            else
                if(inputPassword.getText().toString().isEmpty() || inputPassword.getText().length() <5){
                    inputPassword.setError("Password too short");
                    value = false;
                }
            return value;
        }

        public void savingPreferences(){
            SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String usernameSave = inputUserName.getText().toString();
            String passSave = inputPassword.getText().toString();
            boolean bChk = rememberMe.isChecked();

            if(!bChk){
                editor.clear();
            }
            else
            {
                editor.putString(KEY_USER, usernameSave);
                editor.putString(KEY_PASSWORD, passSave);
                editor.putBoolean(KEY_REMEMBER_ME, bChk);
            }
            editor.commit();

        }
    }
}
