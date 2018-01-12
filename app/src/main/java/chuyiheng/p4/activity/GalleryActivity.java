package chuyiheng.p4.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import chuyiheng.p4.R;
import chuyiheng.p4.RealmAdapter;
import chuyiheng.p4.model.PhotoInfo;
import io.realm.Realm;
import io.realm.RealmResults;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(GalleryActivity.this,2));
        Realm realm=Realm.getDefaultInstance();
        RealmResults<PhotoInfo> realmResults=realm.where(PhotoInfo.class).findAll();
        RealmAdapter realmAdapter=new RealmAdapter(GalleryActivity.this,realmResults);
        mRecyclerView.setAdapter(realmAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delterealmdata,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.deleteRealmAll){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All Photo").setMessage("Do you want to delete it?").setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Realm realm=Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.deleteAll();
                                }
                            });

                            Intent intent=new Intent(GalleryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

            builder.create().show();
            return true;
        }


        if(id==R.id.seeFace){

            Realm realm=Realm.getDefaultInstance();
            RealmResults<PhotoInfo> realmResults=realm.where(PhotoInfo.class).isNotEmpty("mFaceRegions").findAll();
            RealmAdapter realmAdapter=new RealmAdapter(GalleryActivity.this,realmResults);
            mRecyclerView.setAdapter(realmAdapter);

        }

        if(id==R.id.seeAll){

            Realm realm=Realm.getDefaultInstance();
            RealmResults<PhotoInfo> realmResults=realm.where(PhotoInfo.class).findAll();
            RealmAdapter realmAdapter=new RealmAdapter(GalleryActivity.this,realmResults);
            mRecyclerView.setAdapter(realmAdapter);

        }

        return super.onOptionsItemSelected(item);
    }


}
