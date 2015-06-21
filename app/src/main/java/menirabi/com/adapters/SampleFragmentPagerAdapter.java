package menirabi.com.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import menirabi.com.doggydogapp.Constants;
import menirabi.com.doggydogapp.R;
import menirabi.com.fragments.ImageGridFragment;
import menirabi.com.fragments.ImagePagerFragment;

/**
 * Created by Oren on 26/04/2015.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
    public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
    private String tabTitles[] = new String[]{"Tab1", "Tab2"};
    private Context context;
    private Fragment fragmentOne;
    private Fragment fragmentTwo;
    private int[] imageResId = {
            R.mipmap.tab_one,
            R.mipmap.tab_two
    };

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fr;
        String tag;
        Bundle args = new Bundle();
        if (position == 1) {
            tag = ImagePagerFragment.class.getSimpleName();
            Constants.setImageArray();
            if (fragmentOne == null) {
                fragmentOne = new ImagePagerFragment();
            }
            fr = fragmentOne;
            args.putInt(IMAGE_POSITION, position);

        } else {
            tag = ImageGridFragment.class.getSimpleName();
            Constants.setImageArray();
            if (fragmentTwo == null) {
                fragmentTwo = new ImageGridFragment();
            }
            fr = fragmentTwo;
            args.putInt(FRAGMENT_INDEX, position);
        }
        fr.setArguments(args);
        return fr;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //return tabTitles[position];
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
