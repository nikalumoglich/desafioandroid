package co.tiozao.desafioandroid.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.tiozao.desafioandroid.R;
import co.tiozao.desafioandroid.controller.utils.StringUtils;
import co.tiozao.desafioandroid.model.ShotModel;

public class ShotHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView titleTextView;

    @BindView(R.id.created_at)
    TextView createdAtTextView;

    @BindView(R.id.view_count)
    TextView viewCountTextView;

    @BindView(R.id.image)
    ImageView image;

    View view;

    public ShotHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

        this.view = view;
    }

    public void setShot(ShotModel shot) {
        image.setImageDrawable(null);

        if(shot.images != null && shot.images.teaser != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage(shot.images.normal, image);
        }

        viewCountTextView.setText(
                String.format(
                        StringUtils.stringFromResource(R.string.label_views, viewCountTextView), shot.views_count));

        titleTextView.setText(shot.title);

        String formattedDate = StringUtils.dateFormatted(shot.created_at,
                StringUtils.stringFromResource(R.string.date_format, createdAtTextView));

        createdAtTextView.setText(String.format(
                StringUtils.stringFromResource(R.string.label_date, createdAtTextView), formattedDate));
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}