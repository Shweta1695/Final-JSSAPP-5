package cs656.com.firebasemessengerapp.model;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs656.com.firebasemessengerapp.R;
import cs656.com.firebasemessengerapp.ui.ChatActivity;
import cs656.com.firebasemessengerapp.ui.ChatListActivity;

/**
 * Created by user on 27-05-2017.
 */



    /**
     * A login screen that offers login via email/password.
     */
    public class LoginActivity extends Activity{

        private EditText mobile;
        private EditText password;
        private AccessServiceAPI m_ServiceAccess;
        private ProgressDialog progressDialog;
        int lid;
        String uname=null,id=null;
        private CheckBox remember;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_sign_in);

            mobile = (EditText)findViewById(R.id.editTextMobile);
            password = (EditText)findViewById(R.id.editTextPassword);
            remember=(CheckBox)findViewById(R.id.checkBox);

            remember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = mobile.getText().toString();
                    String userPass = password.getText().toString();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("key_name1",userName);
                    editor.putString("key_name2",userPass);
                    editor.commit();
                }
            });
            m_ServiceAccess = new AccessServiceAPI();

        }

        public void btnLogin_Click(View view){
            if ("".equals(mobile.getText().toString())) {
                mobile.setError("Username is required!");
                return;
            }
            if ("".equals(password.getText().toString())) {
                password.setError("Password is required!");
                return;
            }
            new TaskLogin().execute(mobile.getText().toString(),password.getText().toString());
        }

        public void btnGoRegister_Click(View v) {
           Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivityForResult(intent,1);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1)
            {
                mobile.setText(data.getStringExtra("mobile"));
                password.setText(data.getStringExtra("password"));

            }
        }
        public class TaskLogin extends AsyncTask<String,Void,Integer>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Open progress dialog during login
                progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);
            }

            @Override
            protected Integer doInBackground(String... params) {
                Map<String,String> param  = new HashMap<>();


                param.put("mobile",params[0]);
                param.put("password",params[1]);

                JSONObject jsonObject;

                try
                {
                    jsonObject = m_ServiceAccess.convertJSONString2Obj(m_ServiceAccess.getJSONStringWithParam_POST(Common.SERVICE_API_URL,param));

                    //JSONObject userObject = jsonObject.getJSONObject("user");
//                        uname = userObject.getString("name");
//                        id = userObject.getString("mythri_id");
//                        id = "Mythri_16_"+id;
//                        lid = userObject.getInt("login_id");

                    return jsonObject.getInt("result");
                }
                catch (Exception e){
                    return Common.RESULT_ERROR;
                }

            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                if(Common.RESULT_SUCCESS == result){
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(), ChatListActivity.class);

                        startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

