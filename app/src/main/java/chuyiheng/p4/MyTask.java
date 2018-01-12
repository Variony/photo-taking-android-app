package chuyiheng.p4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;

import chuyiheng.p4.activity.GalleryActivity;
import chuyiheng.p4.activity.MainActivity;
import chuyiheng.p4.model.FaceRegion;
import chuyiheng.p4.model.PhotoInfo;
import io.realm.Realm;

public class MyTask extends AsyncTask<String,Integer,Void> {

    Context mContext;

    public MyTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        getAllPhoto(params[0]);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        MainActivity.mProgressDialog.setProgress(MainActivity.count++);
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        if(MainActivity.count==MainActivity.mPaths.size()) {

            MainActivity.mProgressDialog.dismiss();
            Intent intent = new Intent(mContext, GalleryActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }

    public void getAllPhoto(String photoPath){
            Realm mRealm=Realm.getDefaultInstance();

            ArrayList<FaceRegion> faceRegions=getFaceRegions(photoPath);

            mRealm.beginTransaction();
            PhotoInfo photoInfo=mRealm.createObject(PhotoInfo.class);
            photoInfo.setPhotoPath(photoPath);

            for(FaceRegion faceRegion: faceRegions) {
                photoInfo.getFaceRegions().add(mRealm.copyToRealm(faceRegion));
            }

            publishProgress();
            mRealm.commitTransaction();

    }


    public ArrayList<FaceRegion> getFaceRegions(String photoPath){
        ArrayList<FaceRegion> faceRegionRealmList=new ArrayList<>();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        Bitmap bitmap=BitmapFactory.decodeFile(photoPath, options);

        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        double ratio = (double) x / (double) y;
        Bitmap newBitMap = Bitmap.createScaledBitmap(bitmap, (int) Math.round(720 * ratio), 720, false);

        FaceDetector faceDetector = new  FaceDetector.Builder(mContext).setTrackingEnabled(false).build();
        if(!faceDetector.isOperational()){
            Toast.makeText(mContext, "Could not set up the face detector!", Toast.LENGTH_SHORT).show();
            return null;
        }

        Frame frame=new Frame.Builder().setBitmap(newBitMap).build();
        SparseArray<Face> faces=faceDetector.detect(frame);

        for(int i=0; i<faces.size();i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x ;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            FaceRegion faceRegion=new FaceRegion(x1*y/720,y1*y/720,x2*y/720,y2*y/720);
            faceRegionRealmList.add(faceRegion);
        }

        faceDetector.release();
        return faceRegionRealmList;
    }

}
