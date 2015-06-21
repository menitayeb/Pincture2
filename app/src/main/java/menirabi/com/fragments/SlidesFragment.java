package menirabi.com.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import menirabi.com.doggydogapp.R;
import menirabi.com.adapters.SampleFragmentPagerAdapter;
import menirabi.com.doggydogapp.SlidingTabLayout;
import menirabi.com.doggydogapp.UILApplication;

//import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlidesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlidesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE = 3;
    private SharedPreferences sp;
    private String userCoverPath;
    public static final String TAG = "SlidesFragment";

    private Toolbar toolbar;
    private RelativeLayout rlImageCircle;
    TextView tvProfileName;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SlidesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlidesFragment newInstance() {
        SlidesFragment fragment = new SlidesFragment();
        return fragment;
    }

    public SlidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slides, container, false);
        Log.i("onCreateView" , "SlidesFragment");
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);

        sp = getActivity().getSharedPreferences("DoggyDog_BGU", Context.MODE_PRIVATE);
        userCoverPath = sp.getString("user_cover_photo",null);

        if(userCoverPath!=null){
            File f = new File(userCoverPath+"/profile_cover.jpg");
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            Drawable drawable = new BitmapDrawable(getResources(), bmp);
            toolbar.setBackground(drawable);
        }
        else{
            toolbar.setBackgroundResource(R.mipmap.coverr);
        }
        //toolbar.setBackgroundResource(0);
        toolbar.findViewById(R.id.edit_myprofile_cover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                getActivity().startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        rlImageCircle = (RelativeLayout)getActivity().findViewById(R.id.rlMyprofileImage);
        tvProfileName = (TextView) getActivity().findViewById(R.id.profile_name);
        tvProfileName.setVisibility(View.VISIBLE);
        tvProfileName.setVisibility(View.VISIBLE);
        rlImageCircle.setVisibility(View.VISIBLE);
        CircleImageView iv = (CircleImageView) getActivity().findViewById(R.id.myProfileImage);
        iv.setImageResource(R.drawable.third_image);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager() ,
                getActivity()));
        viewPager.setOffscreenPageLimit(4);


        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        slidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);

        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.turquoise);
            }
        });
        slidingTabLayout.setViewPager(viewPager);
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onSlideFragmentInteraction("yoyo");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(userCoverPath!=null){
            File f = new File(userCoverPath+"/profile_cover.jpg");
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            Drawable drawable = new BitmapDrawable(getResources(), bmp);
            toolbar.setBackground(drawable);
        }
        else{
            toolbar.setBackgroundResource(R.mipmap.coverr);
        }
        getActivity().findViewById(R.id.edit_myprofile_cover).setVisibility(View.VISIBLE);
        rlImageCircle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setBackgroundResource(0);
        getActivity().findViewById(R.id.edit_myprofile_cover).setVisibility(View.GONE);
        rlImageCircle.setVisibility(View.GONE);
    }


    public void setCoverImage(Bitmap newCoverImage){
        String abspath = saveToInternalSorage(newCoverImage);
        Drawable drawable = new BitmapDrawable(getResources(), newCoverImage);
        sp = getActivity().getSharedPreferences("DoggyDog_BGU", Context.MODE_PRIVATE);
        sp.edit().putString("user_cover_photo", abspath).commit();
        toolbar.setBackground(drawable);
        //toolbar.setBackground(drawable);

    }

    private String saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("Pincture", Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile_cover.jpg");

        FileOutputStream fos = null;
        try {
            // fos = openFileOutput(filename, Context.MODE_PRIVATE);

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
       // toolbar.setBackgroundResource(0);
        rlImageCircle.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onSlideFragmentInteraction(String contain);
    }

}
