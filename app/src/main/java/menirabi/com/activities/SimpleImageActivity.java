package menirabi.com.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import menirabi.com.doggydogapp.Constants;
import menirabi.com.doggydogapp.R;
import menirabi.com.fragments.ImageGridFragment;
import menirabi.com.fragments.ImagePagerFragment;

//import com.nostra13.universalimageloader.sample.R;
//import com.nostra13.universalimageloader.sample.fragment.ImageGalleryFragment;
//import com.nostra13.universalimageloader.sample.fragment.ImageListFragment;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class SimpleImageActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
        Fragment fr;
        String tag;
        int titleRes;
        switch (frIndex) {
            default:
            case ImageGridFragment.INDEX:
                tag = ImageGridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                Constants.setImageArray();
                if (fr == null) {
                    fr = new ImageGridFragment();
                }
                titleRes = R.string.ac_name_image_grid;
                break;
            case ImagePagerFragment.INDEX:
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                Constants.setImageArray();
                if (fr == null) {
                    fr = new ImagePagerFragment();
                }
                fr.setArguments(getIntent().getExtras());
                titleRes = R.string.ac_name_image_pager;
                break;
        }

        setTitle(titleRes);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
    }
}