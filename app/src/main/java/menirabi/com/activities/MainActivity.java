package menirabi.com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import menirabi.com.adapters.MyAdapter;
import menirabi.com.authenticator.LoginActivity;
import menirabi.com.camera.CameraActivity;
import menirabi.com.doggydogapp.Constants;
import menirabi.com.doggydogapp.R;
import menirabi.com.doggydogapp.RecyclerItemClickListener;
import menirabi.com.doggydogapp.UILApplication;
import menirabi.com.fragments.BlankFragment;
import menirabi.com.fragments.FragmentOne;
import menirabi.com.fragments.NewsFeedFragment;
import menirabi.com.fragments.SlidesFragment;

public class MainActivity extends ActionBarActivity implements SlidesFragment.OnFragmentInteractionListener, FragmentOne.OnFragmentInteractionListener, NewsFeedFragment.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"My Profile", "News Feed", "Pets Nearby", "Activity", "Chat", "Settings", ""};
    int ICONS[] = {R.mipmap.my_profile, R.mipmap.news_feed, R.mipmap.pets_nearby, R.mipmap.activity, R.mipmap.chat, R.mipmap.settings};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Ari Gold";
    String EMAIL = "akash.bangad@android4devs.com";
    int PROFILE = R.drawable.third_image;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private static Fragment firstFragmentHold;
    private List<Fragment> fragmentList;
    private SharedPreferences prefs;
    public static String Tag1 = "first";
    public static String Tag2 = "second";
    public static String Tag3 = "third";
    public static String Tag4 = "fourth";
    public static String Tag5 = "fifth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */

//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        // enable status bar tint
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);

        if(Build.VERSION.SDK_INT >= 21 ){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primaryDark));
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        /*
        * handling login*/
        prefs = getSharedPreferences("DoggyDog_BGU", MODE_PRIVATE);
        boolean isLogged = prefs.getBoolean(getString(R.string.isLogged), false);
        //isLogged = false;
        if (!isLogged) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(mainIntent);

                }
            }, 0);
            MainActivity.this.finish();
        }


        /*First Fragment*/
        Fragment fragment;
        fragment = new SlidesFragment();
        firstFragmentHold = fragment;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        /*hold a list of fragments*/
        fragmentList = new ArrayList<Fragment>();


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        UILApplication.initImageLoader(MainActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        /*
        * Camera OnClickListener*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       System.gc();
                                       Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                                       startActivity(cameraIntent);
                                   }
                               }
        );


        /*Navigation Drawer*/
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Drawer.closeDrawers();
                        Fragment fragment;
                        switch (position) {
                            case 1:
                                fragment = SlidesFragment.newInstance();
                                break;
                            case 2:
                                fragment = NewsFeedFragment.newInstance();
                                break;
                            case 3:
                            case 4:
                            case 5:
                                fragment = BlankFragment.newInstance();
                                break;
                            default:
                                fragment = SlidesFragment.newInstance();
                                break;
                        }
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (fragmentManager.findFragmentByTag(fragment.getClass().getName()) != null) {
                            String f = fragment.getClass().getName();
                            currentFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
                            fragmentList.add(currentFragment);
                            fragmentTransaction.replace(R.id.container, currentFragment, fragment.getClass().getName());
                        } else {
                            String f = fragment.getClass().getName();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            currentFragment = fragment;
                            fragmentList.add(currentFragment);
                            fragmentTransaction.add(R.id.container, currentFragment, fragment.getClass().getName());
                        }
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                })
        );


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    return true;

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }

        });




        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        Drawer.setDrawerTitle(Gravity.START, "something");

        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        Constants.setImageArray();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onBackPressed(){
        finish();
    }


    public void onSlideFragmentInteraction(String contain) {
        Toast.makeText(MainActivity.this, "onSlideFragmentInteraction", Toast.LENGTH_LONG).show();
    }

    public void onFragmentInteraction(String contain) {
        Toast.makeText(MainActivity.this, "onFragmentInteraction", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewsFragmentInteraction(String content) {
        Toast.makeText(MainActivity.this, "onNewsFragmentInteraction", Toast.LENGTH_LONG).show();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==3){
            if(resultCode==RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {android.provider.MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                final BitmapFactory.Options options = new BitmapFactory.Options();

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 300, 180);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;

                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath,options);
                yourSelectedImage = Bitmap.createScaledBitmap(yourSelectedImage, 400, 500, false);

                SlidesFragment fragment = (SlidesFragment) getSupportFragmentManager().findFragmentByTag("menirabi.com.fragments.SlidesFragment");
                fragment.setCoverImage(yourSelectedImage);
            }
        }
    }

    @Override
    public void onBlankFragmentInteraction(String s) {
        Toast.makeText(MainActivity.this, "onBlankFragmentInteraction", Toast.LENGTH_LONG).show();
    }
}