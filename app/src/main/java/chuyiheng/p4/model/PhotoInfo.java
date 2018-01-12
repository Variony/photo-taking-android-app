package chuyiheng.p4.model;


import chuyiheng.p4.baseApplication;
import io.realm.RealmList;
import io.realm.RealmObject;

public class PhotoInfo extends RealmObject {

    String photoPath;
    RealmList<FaceRegion> mFaceRegions;

    public PhotoInfo(){};

    public PhotoInfo(RealmList<FaceRegion> faceRegions, String photoPath) {
        mFaceRegions = faceRegions;
        this.photoPath = photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setFaceRegions(RealmList<FaceRegion> faceRegions) {
        mFaceRegions = faceRegions;
    }

    public RealmList<FaceRegion> getFaceRegions() {
        return mFaceRegions;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
