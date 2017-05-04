package com.example.timyu.timyu_fyp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timyu.timyu_fyp.Class.JSONParser;
import com.example.timyu.timyu_fyp.Class.User;
import com.example.timyu.timyu_fyp.Class.UserManager;
import com.example.timyu.timyu_fyp.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText editPassword, editName;

    String LOGIN = "http://timyu123.com/include/login.php";

    JSONParser jsonParser = new JSONParser();

    String TAG = "test";



    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editName=(EditText)findViewById(R.id.editName);
        editPassword=(EditText)findViewById(R.id.editPassword);


    }

    public void login_onclick(View view){
        AttemptLogin attemptLogin= new AttemptLogin();
        attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),"");
    }


    public  void  register_onclick(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }

    private class AttemptLogin extends AsyncTask<String,Void,JSONObject> {

        @Override

        protected JSONObject doInBackground(String... args) {
            String password = args[1];
            String name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("userName", name));
            params.add(new BasicNameValuePair("userPassword", password));

            JSONObject json = jsonParser.makeHttpRequest(LOGIN, "POST", params);

            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if(result.optInt("success",10) == 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Your username or password is not correct")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(result.getString("userName") != null && result.getString("userEmail") != null){


                    User user = new User(result.getInt("Uid"), result.getString("userName"),result.getString("userEmail"));
                    UserManager.getInstance().setUser(getApplicationContext(), user);
                    Log.e("",""+UserManager.getInstance().getUser().getId());
                    System.out.println("231231232121222311");
                    Log.d(TAG, UserManager.getInstance().getUser().getId()+"");

                    finish();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Can not get any data from server")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Log.d(TAG, "onPostExecute: "+e);
            }

            /*catch (JSONException e) {
                e.printStackTrace();
            }
            */


        }

    }

}

