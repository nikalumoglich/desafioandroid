package co.tiozao.desafioandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.tiozao.desafioandroid.R;
import co.tiozao.desafioandroid.controller.utils.StringUtils;
import co.tiozao.desafioandroid.model.ShotModel;

public class ShotDetailsActivity extends AppCompatActivity {

    public static final String SHOT_EXTRA_BUNDLE_IDENTIFIER = "SHOT_EXTRA_BUNDLE_IDENTIFIER";

    @BindView(R.id.title)
    TextView titleTextView;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.view_count)
    TextView viewCountTextView;

    @BindView(R.id.comment_count)
    TextView commentCountTextView;

    @BindView(R.id.created_at)
    TextView createdAtTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        ShotModel shotModel = (ShotModel)i.getSerializableExtra(SHOT_EXTRA_BUNDLE_IDENTIFIER);

        if(shotModel != null) {
            setShot(shotModel);
        }
    }

    void setShot (final ShotModel shot) {
        if(shot.title != null) {
            titleTextView.setText(shot.title);
        }

        image.setImageDrawable(null);

        if(shot.images != null && shot.images.teaser != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage(shot.images.teaser, image);
        }

        if(shot.description != null) {
            description.setText(Html.fromHtml(shot.description));
        }

        viewCountTextView.setText(
                String.format(
                        StringUtils.stringFromResource(R.string.label_views, viewCountTextView), shot.views_count));
        commentCountTextView.setText(
                String.format(
                        StringUtils.stringFromResource(R.string.label_comments, commentCountTextView), shot.comments_count));

        String formattedDate = StringUtils.dateFormatted(shot.created_at,
                StringUtils.stringFromResource(R.string.date_format, createdAtTextView));

        createdAtTextView.setText(String.format(
                StringUtils.stringFromResource(R.string.label_date, createdAtTextView), formattedDate));
    }
}
