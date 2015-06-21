package menirabi.com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import menirabi.com.adapters.CardViewAdapter;
import menirabi.com.doggydogapp.NewsFeedData;
import menirabi.com.doggydogapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String URL = "";
    public static final String TAG = "NewsFeedFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout rlToolBarImages;
    TextView tvProfileName;

    private List<NewsFeedData> persons;
    private RecyclerView rv;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        return fragment;
    }

    public NewsFeedFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        rv=(RecyclerView)rootView.findViewById(R.id.rv);

        rlToolBarImages = (RelativeLayout) getActivity().findViewById(R.id.rlToolbarImages);
        tvProfileName = (TextView) getActivity().findViewById(R.id.profile_name);
        tvProfileName.setVisibility(View.GONE);
        rlToolBarImages.setVisibility(View.VISIBLE);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)getActivity().findViewById(R.id.tool_bar);
//        rlToolBarImages.setVisibility(View.VISIBLE);
//        String s = "";
    }

    @Override
    public void onPause() {
        super.onPause();
        rlToolBarImages.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.OnFragmentInteractionListener(uri);
//        }
//    }


    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new NewsFeedData("Emma Wilson", "23 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lavery Maiss", "25 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));
        persons.add(new NewsFeedData("Lillie Watts", "35 years old", R.drawable.third_image));


    }

    private void initializeAdapter(){
        CardViewAdapter adapter = new CardViewAdapter(persons);
        rv.setAdapter(adapter);
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
        public void onNewsFragmentInteraction(String content);
    }

}
