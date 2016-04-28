package com.example.tlds.testwebservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Sign_Up extends AppCompatActivity {

    Button btnRegister;
    EditText editUser, editEmail, editPass, editCFPass, editDistrict, editPhone, editCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        btnRegister = (Button)findViewById(R.id.btnRegister);

        editUser = (EditText)findViewById(R.id.editUser);
        editCFPass = (EditText)findViewById(R.id.editCfpass);
        editPass = (EditText)findViewById(R.id.editPass);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editDistrict = (EditText)findViewById(R.id.editDistrict);
        editPhone = (EditText)findViewById(R.id.editPhone);
        editCity = (EditText)findViewById(R.id.editCity);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editUser.getText().toString().isEmpty() && !editPass.getText().toString().isEmpty() && !editCFPass.getText().toString().isEmpty() && !editEmail.getText().toString().isEmpty() && !editDistrict.getText().toString().isEmpty())
                {
                    if(editUser.getText().toString().length() < 4){
                        editUser.setError("Username too short");
                        return;
                    }

                    if (editPass.getText().toString().length() < 6)
                    {
                        editPass.setError("Password too short");
                        return;
                    }
                    if(!editCFPass.getText().toString().equals(editPass.getText().toString()))
                    {
                        editCFPass.setError("Password doesn't match");
                        return;
                    }

                    register();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter complete information", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void register(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getJSONCheckUser().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
            }
        });
    }

    private String makePostRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);

        // Các tham số truyền
        List nameValuePair = new ArrayList(6);
//        nameValuePair.add(new BasicNameValuePair("POST",            "POST"));

        nameValuePair.add(new BasicNameValuePair("username",        editUser.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("password",        editPass.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("email",           editEmail.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("phone_number",    editPhone.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("districts",       editDistrict.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("city",            editCity.getText().toString()));


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kq;
    }



    private static String readContentFromURL(String theUrl)
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


    class getJSONCheckUser extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readContentFromURL(params[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            try {

                //So sánh với dữ liệu
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){
                    //Mỗi profile là một tài khoản
                    JSONObject profile = array.getJSONObject(i);

                    //Nếu bị trùng thì quay trỏ lại việc nhập tài khoản
                    if(editUser.getText().toString().equals(profile.getString("username").toString())) {
                        Toast.makeText(Sign_Up.this, editUser.getText().toString() + " user already exists", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                //Nếu không trùng tài khoản, đăng nó lên server
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SendData().execute("http://minhthangtkqn-001-site1.1tempurl.com/dk.php");
                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        class SendData extends AsyncTask<String, Integer, String>{

            @Override
            protected String doInBackground(String... params) {
                return makePostRequest(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
