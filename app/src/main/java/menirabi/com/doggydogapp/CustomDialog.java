package menirabi.com.doggydogapp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Oren on 21/06/2015.
 */
public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {
    public Bitmap image;
    public Activity c;
    public Dialog d;
    public Button yes, no;

    public CustomDialog(Activity a, Bitmap image) {
        super(a);
        // TODO Auto-generated constructor stub
        this.image = image;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        View v = findViewById(R.id.previewImageDialog);
        v.setBackground(new BitmapDrawable(getContext().getResources(), image));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //c.finish();
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}