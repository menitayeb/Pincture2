package menirabi.com.doggydogapp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import menirabi.com.camera.CameraActivity;

/**
 * Created by Oren on 21/06/2015.
 */
public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {
    public Bitmap image;
    private Handler mHandler;
    public Activity c;
    public Dialog d;
    public ImageView yes, no;

    public CustomDialog(Activity a, Bitmap image) {
        super(a);
        // TODO Auto-generated constructor stub
        this.image = image;
        this.c = a;
    }

    public void setHandler(Handler handler){
        this.mHandler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (ImageButton) findViewById(R.id.btn_yes);
        no = (ImageButton) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        View v = findViewById(R.id.previewImageDialog);
        v.setBackground(new BitmapDrawable(getContext().getResources(), image));

    }

    @Override
    public void onClick(View v) {
        Message msg = new Message();
        switch (v.getId()) {
            case R.id.btn_yes:
                msg.what = CameraActivity.HANDLER_DIALOG_SAVE;
                mHandler.sendMessage(msg);
                //c.finish();
                dismiss();
                break;

            case R.id.btn_no:
                msg.what = CameraActivity.HANDLER_DIALOG_CANCEL;
                mHandler.sendMessage(msg);
                dismiss();
                break;

            default:
                break;
        }
    }
}