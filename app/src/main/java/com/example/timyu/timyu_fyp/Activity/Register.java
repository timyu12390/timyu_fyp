package com.example.timyu.timyu_fyp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.example.timyu.timyu_fyp.Class.JSONParser;
import com.example.timyu.timyu_fyp.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText editUsername, editPassword, editCPassword, editEmail;
    Button btnReset, btnRegister;

    String username, password, cPassword, email;
    String REGISTER = "http://timyu123.com/include/login.php";

    JSONParser jsonParser=new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = (EditText)findViewById(R.id.txtUsername);
        editPassword = (EditText)findViewById(R.id.txtPassword);
        editCPassword = (EditText)findViewById(R.id.txtCPassword);
        editEmail = (EditText)findViewById(R.id.txtEmail);

        btnReset = (Button)findViewById(R.id.btnReset);
        btnRegister = (Button)findViewById(R.id.btnRegister);

    }

    public void reset_onclick(View view){
        editUsername.setText("");
        editPassword.setText("");
        editCPassword.setText("");
        editEmail.setText("");
    }

    public void registerUser_onclick(View view){
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        cPassword = editCPassword.getText().toString();
        email = editEmail.getText().toString();


        if(username == null || password == null || cPassword == null || email == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setMessage("Please enter the valid data")
                    .setCancelable(false)
                    .setPositiveButton("OK",null);
            AlertDialog alert = builder.create();
            alert.show();
        } else if(password.length() <=5 || password.length() >=12){
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setMessage("The password is less then 6 words or more then 12 words")
                    .setCancelable(false)
                    .setPositiveButton("OK",null);
            AlertDialog alert = builder.create();
            alert.show();

        } else if(!(password.equals(cPassword))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setMessage("The password and confirmation do not match. Please re-enter your password.")
                    .setCancelable(false)
                    .setPositiveButton("OK",null);
            AlertDialog alert = builder.create();
            alert.show();
            System.out.println(password);
            System.out.println(cPassword);

            Log.d("asd", "This is Debug.");
        } else {
            AttemptLogin attemptLogin= new AttemptLogin();
            attemptLogin.execute(username, password, email);
        }

    }

    private class AttemptLogin extends AsyncTask<String,Void,JSONObject> {

        @Override

        protected JSONObject doInBackground(String... args) {
            String email = args[2];
            String password = args[1];
            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("userName", name));
            params.add(new BasicNameValuePair("userPassword", password));
            params.add(new BasicNameValuePair("userEmail",email));

            JSONObject json = jsonParser.makeHttpRequest(REGISTER, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result.getInt("success") == 30){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Register Success")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Can not get any data from server")
                            .setCancelable(false)
                            .setPositiveButton("OK",null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    //Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){

            }

            /*catch (JSONException e) {
                e.printStackTrace();
            }
            */


        }

    }
}
