package dreamers.dev.gallerykt.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dreamers.dev.gallerykt.Model.Photo;
import dreamers.dev.gallerykt.R;

/**
 * Created by JoseLuis on 19/04/2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Photo> mList;
    public PhotoAdapter(List<Photo> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);

        ViewHolder vh = new ViewHolder(mList,view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo mPhoto = mList.get(position);
        holder.setItem(mPhoto);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
