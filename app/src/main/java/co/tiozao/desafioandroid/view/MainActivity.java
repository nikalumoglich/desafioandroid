package co.tiozao.desafioandroid.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.tiozao.desafioandroid.R;
import co.tiozao.desafioandroid.controller.ImageLoaderController;
import co.tiozao.desafioandroid.controller.rest.RetrofitController;
import co.tiozao.desafioandroid.controller.rest.ShotController;
import co.tiozao.desafioandroid.model.ShotModel;
import co.tiozao.desafioandroid.view.adapter.ShotAdapter;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.tiozao.desafioandroid.controller.Config.SHOTS_PER_PAGE;

public class MainActivity extends AppCompatActivity {

    //Object to handle HTTP requests
    ShotController shotController;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    //RecyclerView Adapter
    ShotAdapter adapter;

    //This var is used to avoid trying to load data while a request is being processed
    boolean mIsLoading;

    GridLayoutManager mLayoutManager;

    //In order to chache images to disk, we need permission to write data on external storage
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermissions();
    }

    //Setup Activity initial state and initialize needed objects instances
    void setupActivity() {
        RetrofitController.initialize();

        ImageLoaderController.initialize(MainActivity.this);

        shotController = new ShotController();

        adapter = new ShotAdapter(MainActivity.this, null);

        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(ShotModel item) {
                Intent intent = new Intent(MainActivity.this, ShotDetailsActivity.class);
                intent.putExtra(ShotDetailsActivity.SHOT_EXTRA_BUNDLE_IDENTIFIER, item);

                MainActivity.this.startActivity(intent);
            }
        });

        mLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setAdapter(adapter);

        //This is needed to avoid any flick effect when updating adapter data
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsLoading)
                    return;
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    loadData(shotController.getNextPageURL());
                    swipeContainer.setRefreshing(true);
                }
            }
        };

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData(null);
            }
        });

        recyclerView.setOnScrollListener(mScrollListener);

        loadData(null);
    }

    //Check if have permission to write on external storage, thus enabled to do image caching
    void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);

        } else {
            setupActivity();
        }
    }

    //Regardless of having permission to write data to external storage, the app proceed to main screen
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                setupActivity();
                return;
            }
        }
    }

    //Fetch data from webserver, the url parameter can be null
    void loadData(final String url) {
        mIsLoading = true;

        Call<List<ShotModel>> call;

        if(url != null) {
            call = shotController.getService().getShots(url);
        }
        else {
            adapter.clear();

            call = shotController.getService().getShots(SHOTS_PER_PAGE);
        }

        call.enqueue(new Callback<List<ShotModel>>() {

            @Override
            public void onResponse(Call<List<ShotModel>> call, Response<List<ShotModel>> response) {
                if (response.isSuccessful()) {

                    Headers responseHeaders = response.headers();
                    String links = responseHeaders.get("Link");

                    if(links != null) {
                        String nextPageLink;

                        Matcher m = Pattern.compile("\\<(.*?)\\>; rel=\"next\"").matcher(links);
                        while(m.find()) {
                            nextPageLink = m.group(1);
                            shotController.setNextPageURL(nextPageLink);
                            break;
                        }
                    }

                    if(url != null){
                        for (ShotModel shot : response.body()) {
                            adapter.appendItem(shot);
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        }
                    } else {
                        adapter.setItems(response.body());

                        adapter.notifyDataSetChanged();
                    }

                } else {
                    // error response, no access to resource?
                }

                mIsLoading = false;

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ShotModel>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());

                mIsLoading = false;

                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_order_by_comments) {
            adapter.sortBy(new ShotModel.ComparatorByComments());
            adapter.notifyDataSetChanged();
            return true;
        }
        else if (id == R.id.action_order_by_recent) {
            adapter.sortBy(new ShotModel.ComparatorByRecent());
            adapter.notifyDataSetChanged();
            return true;
        }
        else if (id == R.id.action_order_by_views) {
            adapter.sortBy(new ShotModel.ComparatorByViews());
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
