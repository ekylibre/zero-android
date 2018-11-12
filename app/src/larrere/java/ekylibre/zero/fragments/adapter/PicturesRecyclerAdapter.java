package ekylibre.zero.fragments.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    class ViewHolder extends RecyclerView.ViewHolder{

        Context context;
        ImageView imageView;
        Uri pictureUri;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.picture);
            itemView.setOnLongClickListener(view -> {
                picturesList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                if (getItemCount() == 0)
                    picturesRecycler.setVisibility(View.GONE);
                return true;
            });
        }

        void display(Uri item) {
            pictureUri = item;

            Picasso.get().load(item).resize(256, 256).centerCrop().into(imageView,
                new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        dr.setCornerRadius(10);
                        dr.setAntiAlias(true);
                        imageView.setImageDrawable(dr);
                    }
                    @Override
                    public void onError(Exception e) {
                        imageView.setImageResource(R.drawable.ic_ekylibre);
                    }
                });
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
