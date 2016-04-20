package dreamers.dev.gallerykt;

import android.Manifest;
import android.app.SearchManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dreamers.dev.gallerykt.Adapter.PhotoAdapter;
import dreamers.dev.gallerykt.Model.Photo;
import dreamers.dev.gallerykt.Network.ApiServiceDescriptor;
import dreamers.dev.gallerykt.Network.RetrofitSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Gallery example just for Knowlegde-transfer
 *
 * This project uses Retrofit to make the HTTP calls and also Picasso for the image Loading
 * Other Libraries used are RecyclerView and CardView
 * http://frogermcs.github.io/instamaterial-recyclerview-animations-done-right/
 * http://sapandiwakar.in/pull-to-refresh-for-android-recyclerview-or-any-other-vertically-scrolling-view/
 * http://developer.android.com/intl/es/training/permissions/requesting.html
 * icon
 * https://www.iconfinder.com/icons/728946/file_format_gallery_image_photo_photography_picture_icon#size=256
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView        mRecyclerView;
    private GridLayoutManager   mGridLayoutManager;
    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private RecyclerView.Adapter adapter;
    private List<Photo>         mList = new ArrayList<Photo>();
    //variables to check the network
    private ConnectivityManager connManager;
    private NetworkInfo activeNetwork;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =24601;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mRefresh);
        mSwipeRefreshLayout.setEnabled(true);
        mGridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        connManager =(ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        activeNetwork = connManager.getActiveNetworkInfo();

        if(activeNetwork != null){
            askPermission();
            callServer();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(activeNetwork != null){
                    updateView();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.error_network_msg,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateView() {
        clearData();
        callServer();
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private void clearData() {
        //http://stackoverflow.com/questions/29978695/remove-all-items-from-recyclerview
        int size = this.mList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mList.remove(0);
            }
            //notifyAll();
            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    private void askPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void callServer() {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<List<Photo>> response = RetrofitSingleton.apiService.getPhotos();
        response.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                mList = response.body();
                adapter = new PhotoAdapter(mList);
                mRecyclerView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"we cannot connect to the cloud, try again swiping down",Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    // saving the state of the activity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saving the state of the List
        outState.putParcelableArrayList("photos", (ArrayList<? extends Parcelable>) mList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //restoring the information
        if(savedInstanceState != null) {
            mList.clear();
            mList = savedInstanceState.getParcelableArrayList("photos");
            adapter = new PhotoAdapter(mList);
            mRecyclerView.setAdapter(adapter);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.gallery_icon).setView(R.layout.about).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
