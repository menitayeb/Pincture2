package menirabi.com.camera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import menirabi.com.adapters.CustomAdapter;
import menirabi.com.doggydogapp.CustomDialog;
import menirabi.com.doggydogapp.R;

public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    public static final int HANDLER_DIALOG_CANCEL = 32;
    public static final int HANDLER_DIALOG_SAVE = 33;
    Preview preview;
    Camera camera;
    Activity act;
    Context ctx;
    Dialog dialog;
    CustomDialog cdd;
    private byte[] pictureData;
    Bitmap imageDecode;
    Button buttonClick1;
    Button buttonClick2;
    Button buttonClick3;
    Button buttonClick6;
    String def = "defaullt";
    private int barksType = 0;
    Context context;
    public Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case HANDLER_DIALOG_SAVE:
                    new SaveImageTask().execute(pictureData);
                    break;
                case HANDLER_DIALOG_CANCEL:
                    break;
            }
        }
    };

    private final ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };
    //some comments

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);
        context = getBaseContext();

        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);

        final ShutterCallback shutterCallback = new ShutterCallback() {
            public void onShutter() {
                Log.d(TAG, "onShutter'd");
            }
        };

        // Dog Bark Sounds
        buttonClick1 = (Button) findViewById(R.id.buttonClick1);
        buttonClick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

//                sound.playShortResource(R.raw.dogbark);
                final MediaPlayer mp;
                barksType = getSharedPreferences("DoggyDog_BGU", MODE_PRIVATE).getInt(def, 0);
                switch (barksType) {
                    case 0:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.bark1);
//                        Toast.makeText(getApplicationContext(),"this is 1", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.bark2);
//                        Toast.makeText(getApplicationContext(),"this is 2", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.bark3);
//                        Toast.makeText(getApplicationContext(),"this is 3", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.bark1);
//                        Toast.makeText(getApplicationContext(),"this is default", Toast.LENGTH_LONG).show();
                        break;
                }

                CountDownTimer cntr_aCounter = new CountDownTimer(600, 100) {
                    public void onTick(long millisUntilFinished) {

                        mp.start();
                    }

                    public void onFinish() {
                        //code fire after finish
                        mp.stop();
                    }
                };cntr_aCounter.start();



                mp.start();

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.buttonClick4);

        //CustomAdapter<CharSequence> adapter = CustomAdapter.createFromResource(this,R.array.bark_array,R.layout.spinner_ddd);

        CustomAdapter<String> adapter = new CustomAdapter<String>(this,
                R.layout.spinner_ddd, new String[] {"Sample 1", "Sample 2","Sample 3"});


       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.bark_array, R.layout.spinner_ddd);
     //   adapter.setDropDownViewResource(R.layout.spinner_ddd);
        spinner.setPopupBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_color_transparent)));
        spinner.setBackgroundResource(R.mipmap.menu_opt);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences prefs = getSharedPreferences("DoggyDog_BGU", MODE_PRIVATE);
                prefs.edit().putInt(def, position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btnCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreview();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreview();
            }
        });
        buttonClick6 = (Button) findViewById(R.id.buttonClick6);
        buttonClick6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                List<String> pList = camera.getParameters().getSupportedFlashModes();
                Camera.Parameters p =camera.getParameters();
                if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    if (pList.contains(Camera.Parameters.FLASH_MODE_TORCH) && (android.os.Build.MANUFACTURER.contains("htc"))) {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    }
                    else
                    if (pList.contains(Camera.Parameters.FLASH_MODE_ON)) {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                    camera.setParameters(p);
                    Toast.makeText(context,"Flash On",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"No flash on device",Toast.LENGTH_LONG).show();
                }
            }
        });


        buttonClick3 = (Button) findViewById(R.id.buttonClick3);
        buttonClick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent mainIntent = new Intent(CameraActivity.this, AndroidVideoCaptureExample.class);
                CameraActivity.this.startActivity(mainIntent);
            }
        });

        buttonClick2 = (Button) findViewById(R.id.buttonClick2);
        buttonClick2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
                int rotation = CameraActivity.this.getWindowManager().getDefaultDisplay().getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
                    case Surface.ROTATION_90: degrees = 90; break; //Landscape left
                    case Surface.ROTATION_180: degrees = 180; break;//Upside down
                    case Surface.ROTATION_270: degrees = 270; break;//Landscape right
                }
                int rotate = (info.orientation - degrees + 360) % 360;

//STEP #2: Set the 'rotation' parameter
                Camera.Parameters params = camera.getParameters();
                params.setRotation(rotate);

                List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
                int max = 0;
                int index = 0;

                for (int i = 0; i < supportedSizes.size(); i++){
                    Size s = supportedSizes.get(i);
                    int size = s.height * s.width;
                    if (size > max) {
                        index = i;
                        max = size;
                    }
                }
                Camera.Size sizePicture = (supportedSizes.get(index));
                params.setPictureSize(sizePicture.width, sizePicture.height);

                camera.setParameters(params);
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);

               // dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);

               // dialog.setContentView(R.layout.custom_dialog);
//                CustomDialog cdd = new CustomDialog(CameraActivity.this);
//                cdd.show();
                //findViewById(R.id.previewImage).setVisibility(View.VISIBLE);
                //findViewById(R.id.btnCancle).setVisibility(View.VISIBLE);
                //findViewById(R.id.btnSave).setVisibility(View.VISIBLE);

            }
        });


//        		buttonClick2.setOnLongClickListener(new View.OnLongClickListener(){
//        			@Override
//        			public boolean onLongClick(View arg0) {
//        				camera.autoFocus(new Camera.AutoFocusCallback(){
//        					@Override
//        					public void onAutoFocus(boolean arg0, Camera arg1) {
//        						camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//        					}
//        				});
//        				return true;
//        			}
//        		});
    }

    private void showPreview() {
        findViewById(R.id.previewImage).setVisibility(View.GONE);
        findViewById(R.id.btnCancle).setVisibility(View.GONE);
        findViewById(R.id.btnSave).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //new SaveImageTask().execute(data);
            pictureData = data;
//            Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
            try{
                imageDecode = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageDecode = Bitmap.createScaledBitmap(imageDecode, 800, 800, false);
                cdd = new CustomDialog(CameraActivity.this, imageDecode);
                cdd.setHandler(mHandler);
                cdd.show();
                imageDecode = null;
            }
            catch (OutOfMemoryError E){
                Toast.makeText(getBaseContext(), "Out Of Memory, still working on it.." , Toast.LENGTH_LONG).show();
            }

           // findViewById(R.id.previewImage).setVisibility(View.VISIBLE);

            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath()+"/PincturePhothos");
                sdCard.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(sdCard, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }





}