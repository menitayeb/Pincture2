package menirabi.com.doggydogapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


//import com.rabitodo.meni.sample.R;

/**
 * Example widget provider
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class UILWidgetProvider extends AppWidgetProvider {

    private static DisplayImageOptions displayOptions;

    static {
        displayOptions = DisplayImageOptions.createSimple();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UILApplication.initImageLoader(context);

        final int widgetCount = appWidgetIds.length;
        for (int i = 0; i < widgetCount; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        ImageSize minImageSize = new ImageSize(70, 70); // 70 - approximate size of ImageView in widget
        ImageLoader.getInstance()
                .loadImage(Constants.IMAGES[0], minImageSize, displayOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        views.setImageViewBitmap(R.id.image_left, loadedImage);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                });
        ImageLoader.getInstance()
                .loadImage(Constants.IMAGES[1], minImageSize, displayOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        views.setImageViewBitmap(R.id.image_right, loadedImage);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                });
    }
}