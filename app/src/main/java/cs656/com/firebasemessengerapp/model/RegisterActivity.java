package cs656.com.firebasemessengerapp.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import cs656.com.firebasemessengerapp.R;

/**
 * Created by Lenovo on 23-Jul-16.
 */
public class RegisterActivity extends Activity{
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtMobile;
    private EditText txtAdmNo;
    private EditText txtPassword1;
    private EditText txtPassword2;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String txtGender;
    private ProgressDialog m_ProgresDialog;
    private AccessServiceAPI m_AccessServiceAPI;
    String count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtUsername = (EditText)findViewById(R.id.editTextName);
        txtEmail = (EditText)findViewById(R.id.editTextEmail);
        txtMobile = (EditText)findViewById(R.id.editTextMobile);
        txtAdmNo = (EditText)findViewById(R.id.admis_no);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        txtGender = radioSexButton.getText().toString();
        txtPassword1 = (EditText)findViewById(R.id.editTextPassword);
        txtPassword2 = (EditText) findViewById(R.id.editTextConfirmPassword);
        m_AccessServiceAPI = new AccessServiceAPI();
    }

    public void btnBack_Click(View v) {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivityForResult(intent,0);
    }



    public void btnRegister_Click(View v) {
        //Validate input
        if("".equals(txtUsername.getText().toString())) {
            txtUsername.setError("Username is required!");
            return;
        }
        if("".equals(txtAdmNo.getText().toString())) {
            txtUsername.setError("Username is required!");
            return;
        }
        if("".equals(txtEmail.getText().toString())) {
            txtEmail.setError("Email is required!");
            return;
        }
        if("".equals(txtMobile.getText().toString())) {
            if(txtMobile.length()!= 10)
            txtMobile.setError("Mobile No. is required!");
            return;
        }
        if("".equals(txtPassword1.getText().toString())) {
            txtPassword1.setError("Password is required!");
            return;
        }
        if("".equals(txtPassword2.getText().toString())) {
            txtPassword2.setError("Confirm password is required!");
            return;
        }

        if(txtPassword1.getText().toString().equals(txtPassword2.getText().toString())) {
            //exec task register
            new TaskRegister().execute(txtUsername.getText().toString(), txtPassword1.getText().toString(),txtMobile.getText().toString(),txtEmail.getText().toString(),txtGender,txtAdmNo.getText().toString());
        } else {
            txtPassword2.setError("Confirm password not match!");
        }
    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_ProgresDialog = ProgressDialog.show(RegisterActivity.this, "Please wait", "Registration processing...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
//            try{
////                String countString = m_AccessServiceAPI.getJSONStringWithParam_POST(Common.SERVICE_COUNT_URL, postParam);
////                JSONObject countObject = new JSONObject(countString);
////                count = countObject.getString("count");
//            }catch (Exception e) {
//                e.printStackTrace();
//
//            }
            postParam.put("name", params[0]);
            postParam.put("password", params[1]);
            postParam.put("mobile", params[2]);
            postParam.put("email", params[3]);
            postParam.put("gender", params[4]);
            postParam.put("addno", params[5]);



            try{
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(Common.SERVICE_SIGN_UP_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.getInt("result");
            }catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            m_ProgresDialog.dismiss();
            if(integer == Common.RESULT_ERROR) {
                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_LONG).show();
                Intent i = new Intent();
                i.putExtra("mobile", txtMobile.getText().toString());
                i.putExtra("password", txtPassword1.getText().toString());
                setResult(1, i);
                finish();
            }
        }
    }
}