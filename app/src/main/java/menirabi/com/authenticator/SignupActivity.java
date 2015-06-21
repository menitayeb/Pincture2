package menirabi.com.authenticator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.widget.LoginButton;

import java.util.Arrays;

import menirabi.com.activities.MainActivity;
import menirabi.com.doggydogapp.R;


public class SignupActivity extends ActionBarActivity {
    public static final String URL = "";
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        prefs = getSharedPreferences("DoggyDog_BGU", MODE_PRIVATE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tvEmail = ((EditText) findViewById(R.id.edtFullName)).getText().toString();
                String tvPass = ((EditText) findViewById(R.id.nickname)).getText().toString();
                new AsyncCaller().execute(URL, tvEmail, tvPass);
            }
        });

    }



    private class AsyncCaller extends AsyncTask<String, Void, String[]>
    {
        ProgressDialog pdLoading = new ProgressDialog(SignupActivity.this);

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
            Intent mainIntent = new Intent(SignupActivity.this,MainActivity.class);
            prefs.edit().putString("user", result[0]).commit();
            prefs.edit().putString("pass", result[1]).commit();
            prefs.edit().putBoolean(getString(R.string.isLogged), true).commit();
            SignupActivity.this.startActivity(mainIntent);
            SignupActivity.this.finish();
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(setIntent);
        SignupActivity.this.finish();
    }
}
