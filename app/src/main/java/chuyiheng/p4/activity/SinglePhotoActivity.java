package chuyiheng.p4.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import chuyiheng.p4.R;
import chuyiheng.p4.RealmAdapter;
import chuyiheng.p4.model.FaceRegion;
import chuyiheng.p4.model.PhotoInfo;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SinglePhotoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Realm realm=Realm.getDefaultInstance();
    private String photoPath;
    private RealmResults<PhotoInfo> photoInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo);

        mImageView= (ImageView) findViewById(R.id.singlePhotoImageView);
        Intent intent=getIntent();
        photoPath=intent.getStringExtra(RealmAdapter.PHOTO_PATH);

        photoInfos=realm.where(PhotoInfo.class).equalTo("photoPath", photoPath).findAll();
        RealmList<FaceRegion> faceRegions=photoInfos.first().getFaceRegions();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable= true;
        Bitmap bitmap=BitmapFactory.decodeFile(photoPath, options);

        Bitmap tempBitmap= Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
        Canvas tempCanvas=new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap,0,0,null);

        for(FaceRegion faceRegion:faceRegions){
            tempCanvas.drawRoundRect(new RectF(faceRegion.getX(),faceRegion.getY(),faceRegion.getWidth(),faceRegion.getHeight()),2,2,getPaint());
        }
        mImageView.setImageBitmap(tempBitmap);
    }


    @NonNull
    private Paint getPaint() {
        Paint myRectPaint=new Paint();
        myRectPaint.setStrokeWidth(2);
        myRectPaint.setColor(Color.GREEN);
        myRectPaint.setStyle(Paint.Style.STROKE);
        return myRectPaint;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deletephoto,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.deleteMenuItem){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Delete Photo").setMessage("Do you want to delete it?").setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    photoInfos.deleteFirstFromRealm();
                                    finish();

                                }
                            });
                        }
                    });

            builder.create().show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
