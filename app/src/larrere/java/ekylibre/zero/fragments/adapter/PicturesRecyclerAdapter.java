package ekylibre.zero.fragments.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;

import static ekylibre.zero.fragments.ObservationFormFragment.picturesRecycler;

public class PicturesRecyclerAdapter extends RecyclerView.Adapter<PicturesRecyclerAdapter.ViewHolder> {

    private final List<Uri> picturesList;

    public PicturesRecyclerAdapter(List<Uri> items) {
        picturesList = items;
    }

    /**
     * The item ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        ImageView imageView;
        Uri pictureUri;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(this);
        }

        void display(Uri item) {
            pictureUri = item;

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), item);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            dr.setCornerRadius(10);
            dr.setAntiAlias(true);
            imageView.setImageDrawable(dr);
        }

        @Override
        public void onClick(View v) {
            itemView.postDelayed(() -> {
                picturesList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                if (getItemCount() == 0)
                    picturesRecycler.setVisibility(View.GONE);
                }, 200);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.display(picturesList.get(position));
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }
}
