package menirabi.com.authenticator;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

import menirabi.com.activities.MainActivity;
import menirabi.com.doggydogapp.R;
import menirabi.com.doggydogapp.UILApplication;

public class ConnectSocialNetwork extends Activity
{
    public static final String              FACEBOOK_POST_DESCRIPTION = "postFacebookDescription";
    public static final String              FACEBOOK_POST_LINK        = "postFacebookLink";
    public static final String              FACEBOOK_POST_IMAGE       = "postFacebookImage";
    private static final List<String>       PERMISSIONS               = Arrays.asList("email");

    private String                          newAccessToken;

    private ProgressDialog                  m_ProgressDialog          = null;

    private UILApplication mainApp;
    private static Activity                 activity;

    private Session                         session;
    private UiLifecycleHelper               uiHelper;

    private PendingIntent                   pandingIntent;
    private Thread.UncaughtExceptionHandler onRuntimeError            = new Thread.UncaughtExceptionHandler()
    {
        public void uncaughtException(Thread thread, Throwable ex)
        {
            Log.i("Caught UnHandeld Exception", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis(), pandingIntent);
            System.exit(2);
        }
    };

    private Session.StatusCallback          statusCallback            = new Session.StatusCallback()
    {
        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            if (state.isOpened())
            {
                // if (checkPermissions())
                // {
                // Log.d("Facebook", "Facebook have PUBLICH_ACTIONS session");
                // make request to the /me API

                newAccessToken = session.getAccessToken();

                Request.newMeRequest(session, new Request.GraphUserCallback()
                {
                    // callback after Graph
                    // API response with
                    // user object
                    @Override
                    public void onCompleted(GraphUser user, Response response)
                    {
                        try
                        {
                            mainApp.setFbGraphUser(user);

                            Editor e = mainApp.getSp().edit();
                            e.putString(UILApplication.FACEBOOK_USER_ID, user.getId());
                            e.putString(UILApplication.FACEBOOK_USER_NAME, user.getFirstName());
                            e.putString(UILApplication.FACEBOOK_ACCESS_TOKEN, newAccessToken);
                            e.putString(UILApplication.FACEBOOK_USER_EMAIL, user.getProperty("email").toString());
                            e.commit();

                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            mainApp.setFbGraphUser(null);

                            Editor e1 = mainApp.getSp().edit();
                            e1.putString(UILApplication.FACEBOOK_USER_ID, "");
                            e1.putString(UILApplication.FACEBOOK_USER_NAME, "");
                            e1.putString(UILApplication.FACEBOOK_ACCESS_TOKEN, "");
                            e1.putString(UILApplication.FACEBOOK_USER_EMAIL, "");
                            e1.commit();

                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    }
                }).executeAsync();
                // }
                // else
                // {
                // Log.d("Facebook", "Facebook NOT have PUBLICH_ACTIONS session");
                // facebookLoginFailed();
                // }
            }
            else if (state.isClosed())
            {
                // Log.d("Facebook", "Facebook NOT have PUBLICH_ACTIONS session");
                facebookLoginFailed();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        mainApp = ((UILApplication) getApplication());
        activity = this;

        if (mainApp.isEnableErrorRestart())
        {
            pandingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, new Intent(ConnectSocialNetwork.this, MainActivity.class), getIntent().getFlags());
            Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
        }

        try
        {
            if (!getIntent().getExtras().getString(FACEBOOK_POST_DESCRIPTION).equals(""))
            {
                uiHelper = new UiLifecycleHelper(this, statusCallback);
                uiHelper.onCreate(savedInstanceState);

                postToFacebookUsingDialog();
                return;
            }
        }
        catch (Exception e)
        {
        }

        session = new Session(this);
        Session.setActiveSession(session);

        try
        {
            uiHelper = new UiLifecycleHelper(this, statusCallback);
            uiHelper.onCreate(savedInstanceState);
        }
        catch (Exception ignored)
        {
        }

        LoginButton loginButton = new LoginButton(activity);
        loginButton.setReadPermissions(PERMISSIONS);
        loginButton.performClick();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            uiHelper.onResume();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            uiHelper.onPause();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try
        {
            uiHelper.onDestroy();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // uiHelper.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback()
        {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data)
            {
                Log.e("Login Facebook", String.format("Error: %s", error.toString()));

                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data)
            {
                Log.i("Login Facebook", "Success!");

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedState)
    {
        super.onSaveInstanceState(savedState);
        try
        {
            uiHelper.onSaveInstanceState(savedState);
        }
        catch (Exception ignored)
        {
        }
    }

    private void facebookLoginFailed()
    {
        try
        {
            m_ProgressDialog.dismiss();
        }
        catch (Exception e)
        {
        }

        session = new Session(this);
        Session.setActiveSession(session);

        // Toast.makeText(getApplicationContext(), getString(R.string.facebook_failed_connect), Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void postToFacebookUsingDialog()
    {
        try
        {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setDescription(getIntent().getExtras().getString(FACEBOOK_POST_DESCRIPTION)).setLink(getIntent().getExtras().getString(FACEBOOK_POST_LINK)).setPicture(getIntent().getExtras().getString(FACEBOOK_POST_IMAGE)).build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

}
