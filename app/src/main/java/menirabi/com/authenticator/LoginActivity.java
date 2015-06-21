package menirabi.com.authenticator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.widget.LoginButton;

import java.util.Arrays;

import menirabi.com.activities.MainActivity;
import menirabi.com.doggydogapp.R;
import menirabi.com.doggydogapp.UILApplication;

/**
 * Created by Oren on 09/03/2015.
 */
public class LoginActivity extends FragmentActivity {
    ProgressDialog dialog;

    private String newAccessToken;
    private String temp;
    public static final String URL = "";
    private UILApplication mainApp;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
       // InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(((EditText) findViewById(R.id.edtEmail)),InputMethodManager.SHOW_FORCED);


        prefs = getSharedPreferences("DoggyDog_BGU", MODE_PRIVATE);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvEmail = ((TextView) findViewById(R.id.edtEmail)).getText().toString();
                String tvPass = ((TextView) findViewById(R.id.edtPass)).getText().toString();
                new AsyncCaller().execute(URL, tvEmail, tvPass);
            }
        });

        findViewById(R.id.link_to_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent =  new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(registerIntent);
                LoginActivity.this.finish();
            }
        });

        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //prefs.edit().putBoolean(getString(R.string.isLogged), true).commit();
            }
        });


//        LoginButton registerButton = (LoginButton) findViewById(R.id.link_to_register);
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent registerIntent =  new Intent(LoginActivity.this, SignUp.class);
//                startActivity(registerIntent);
//                LoginActivity.this.finish();
//            }
//        });


    }
    private class AsyncCaller extends AsyncTask<String, Void, String[]>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();

        }
        @Override
        protected String[] doInBackground(String... params) {
            //String content = HttpManager.getData(params[0],"username", "password");
            String content[] = {"Meni","Tayeb"};
            return content;
        }

        @Override
        protected void onPostExecute(final String[] result) {
            super.onPostExecute(result);
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    prefs.edit().putString("user", result[0]).commit();
                    prefs.edit().putString("pass", result[1]).commit();
                    prefs.edit().putBoolean(getString(R.string.isLogged), true).commit();
                    LoginActivity.this.startActivity(mainIntent);
                    LoginActivity.this.finish();
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
