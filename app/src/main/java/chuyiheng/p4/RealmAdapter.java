package chuyiheng.p4;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import chuyiheng.p4.activity.SinglePhotoActivity;
import chuyiheng.p4.model.PhotoInfo;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RealmAdapter extends RealmRecyclerViewAdapter<PhotoInfo, RealmAdapter.ViewHolder> {

    public static final String PHOTO_PATH = "photo_path";

    public RealmAdapter(Context context, OrderedRealmCollection<PhotoInfo> data) {
        super(context, data,true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.list_picture,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoInfo photoInfo=getData().get(position);
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoInfo.getPhotoPath()), 180, 180);
        holder.mImageView.setImageBitmap(bitmap);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.photoThumbImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           Intent intent=new Intent(context, SinglePhotoActivity.class);
            intent.putExtra(PHOTO_PATH,getData().get(getPosition()).getPhotoPath());
           context.startActivity(intent);
        }
    }
}
