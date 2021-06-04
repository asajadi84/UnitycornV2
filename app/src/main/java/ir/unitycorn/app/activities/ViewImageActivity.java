package ir.unitycorn.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.R;

public class ViewImageActivity extends AppCompatActivity {

    PhotoView thePhoto;
    String imageId;
    SharedPreferences settingsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        imageId = getIntent().getExtras().getString("imageId");

        super.onCreate(savedInstanceState);

        Locale farsi = new Locale("fa", "IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_view_image);

        setTitle(imageId);

        thePhoto = findViewById(R.id.photo_view);
        thePhoto.setImageResource(getApplicationContext().getResources().getIdentifier(
                imageId,
                "drawable",
                getApplicationContext().getPackageName()
        ));

        Typeface iranSans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Toasty.Config.getInstance()
                .setToastTypeface(iranSans)
                .allowQueue(false)
                .apply();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_image_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save_in_gallery){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                AlertDialog permissionAlertDialog = new AlertDialog.Builder(this)
                        .setMessage(getResources().getString(R.string.image_activity_permission_alert_dialog_message))
                        .setPositiveButton(getResources().getString(R.string.image_activity_permission_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ViewImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.image_activity_permission_alert_dialog_negative_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toasty.error(getApplicationContext(), getResources().getString(R.string.image_activity_permission_access_denied)).show();
                            }
                        }).show();

                if (settingsPrefs.getBoolean("dark_mode", false)) {
                    permissionAlertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

                    permissionAlertDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+getResources().getString(R.string.image_activity_permission_alert_dialog_message)+"</font>"));
                    permissionAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    permissionAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.normalTextColorDark));

                }else{
                    permissionAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    permissionAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
            else{
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveThePic();
                    }
                });
                Toasty.success(getApplicationContext(), getResources().getString(R.string.image_activity_permission_access_granted) + "\n"+
                        "/DCIM/Unitycorn/"+imageId+".png", Toast.LENGTH_LONG, true).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveThePic();
                    }
                });
                Toasty.success(getApplicationContext(), getResources().getString(R.string.image_activity_permission_access_granted) + "\n"+
                        "/DCIM/Unitycorn/"+imageId+".png", Toast.LENGTH_LONG, true).show();
            }

        }else{
            Toasty.error(getApplicationContext(), getResources().getString(R.string.image_activity_permission_access_denied)).show();
        }
    }

    private void saveThePic(){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), getApplicationContext().getResources().getIdentifier(
                imageId,
                "drawable",
                getApplicationContext().getPackageName()
        ));
        File destinationFolder = new File(Environment.getExternalStorageDirectory()+"/DCIM/Unitycorn");
        File imageInFolder = new File(destinationFolder.getAbsolutePath()+"/"+imageId+".png");

        //Make image file appear in gallery app
        MediaScannerConnection.scanFile(this, new String[] { imageInFolder.getPath() }, new String[] { "image/png" }, null);

        if(!destinationFolder.exists()){
            destinationFolder.mkdir();
        }

        if(!imageInFolder.exists()){
            try {
                imageInFolder.createNewFile();
            }catch (IOException e){}
        }

        try {

            FileOutputStream outStream = new FileOutputStream(imageInFolder);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
