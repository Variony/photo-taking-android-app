package chuyiheng.p4.model;


import io.realm.RealmObject;

public class FaceRegion extends RealmObject {
    float x;
    float y;
    float width;
    float height;

    public FaceRegion(){}

    public FaceRegion( float x, float y,float width, float height) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
