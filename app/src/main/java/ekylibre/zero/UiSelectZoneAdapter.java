package ekylibre.zero;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UiSelectZoneAdapter extends RecyclerView.Adapter<UiSelectZoneAdapter.ViewHolder> {

    List<String> listZone;

    public UiSelectZoneAdapter(List<String> dataset) {
        this.listZone = dataset;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView zoneImageView;
        TextView zoneNameView;


        ViewHolder(View itemView) {
            super(itemView);

            zoneImageView = itemView.findViewById(R.id.select_zone_item_image);
            zoneNameView = itemView.findViewById(R.id.select_zone_item_name);
            Log.i("MyTag","ViewHolder");

        }


        void display(String item) {
            zoneImageView.setImageResource(R.drawable.action_travel);
            zoneNameView.setText(item);
            Log.i("MyTag","Display item:"+item);

        }


    }

    @NonNull
    @Override
    public UiSelectZoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_zone_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectZoneAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listZone.get(i));
        Log.i("MyTag","Bind item:"+listZone.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listZone.size());
        return listZone.size();
    }
}
