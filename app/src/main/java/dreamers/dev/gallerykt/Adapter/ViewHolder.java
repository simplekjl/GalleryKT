package dreamers.dev.gallerykt.Adapter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import dreamers.dev.gallerykt.Model.Photo;
import dreamers.dev.gallerykt.R;

/**
 * Created by JoseLuis on 19/04/2016.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView    mTitle;
    private ImageView   mImageView;
    private Context     mContext;

    public ViewHolder(List<Photo> mList, View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mImageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public void setItem(Photo mPhoto) {
        mTitle.setText(mPhoto.getTitle());
        String[] sites = {"http://lorempixel.com/image_output/fashion-q-c-640-480-7.jpg",
                "http://lorempixel.com/image_output/people-q-c-640-480-8.jpg",
                "http://lorempixel.com/image_output/people-q-c-640-480-9.jpg",
                "http://lorempixel.com/image_output/animals-q-c-640-480-10.jpg",
                "http://lorempixel.com/image_output/technics-q-c-640-480-10.jpg",
                "http://lorempixel.com/image_output/nature-q-c-640-480-3.jpg"};

        Random mRandom = new Random();

        Picasso.with(mContext).load(sites[mRandom.nextInt(5)])
                .placeholder(R.drawable.default_placeholder)
                .into(mImageView);

    }
}
