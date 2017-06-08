package patrimoine.wcs.fr.toulousemonuments;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import patrimoine.wcs.fr.toulousemonuments.models.Fields;
import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

public class DescriptionActivity extends AppCompatActivity implements DescriptionFragment.OnFragmentInteractionListener {

    private TextView mTextViewTitle;
    private ImageView mImageViewDescriptionMain;
    private TextView mTextViewDescription;
    private TextView mTextviewScore;
    private ImageView mImageViewEtoile;
    private ImageView mImageViewEtoileScore;

    private SpiceManager mSpiceManager;
    private int mPosition;
    private MonumentModel mMonument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intentFromMainActivity = getIntent();
        mPosition = intentFromMainActivity.getIntExtra(MainActivity.POSITION, -1);

        mTextViewTitle = (TextView) findViewById(R.id.textViewLieu);
        mTextViewDescription = (TextView) findViewById(R.id.textViewDescription);
        mTextviewScore = (TextView) findViewById(R.id.textViewScore);
        mImageViewDescriptionMain = (ImageView) findViewById(R.id.imageViewDescriptionMain);
        mImageViewEtoile = (ImageView) findViewById(R.id.imageViewMonEtoile);
        mImageViewEtoileScore = (ImageView) findViewById(R.id.imageViewRating);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_description:
                        DescriptionFragment desciptionFragment = new DescriptionFragment();
                        loadFragment(desciptionFragment);
                        return true;
                    case R.id.navigation_picture:
                        return true;
                    case R.id.navigation_comment:
                        CommentFragment commentFragment = new CommentFragment();
                        loadFragment(commentFragment);
                        return true;
                }
                return false;
            }
        });

        mSpiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        performRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    private void performRequest() {

        MonumentRequest monumentRequest = new MonumentRequest();
        mSpiceManager.execute(monumentRequest,MainActivity.CACHE, DurationInMillis.ONE_WEEK, new DescriptionActivity.MonumentRequestListener());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class MonumentRequestListener implements RequestListener<MonumentModel> {


        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(MonumentModel monument) {
            mMonument = monument;
            DescriptionFragment desciptionFragment = new DescriptionFragment();
            loadFragment(desciptionFragment);

        }
    }

    private void loadFragment(BaseFragment f) {
        f.setmMonument(mMonument);
        f.setmPosition(mPosition);
        getFragmentManager().beginTransaction().replace(R.id.framLayoutDescription, f).commit();
    }
}



