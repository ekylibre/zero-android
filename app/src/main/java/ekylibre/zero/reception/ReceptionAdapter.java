package ekylibre.zero.reception;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.home.TodoItem;
import ekylibre.zero.home.TodoListActivity;
import ekylibre.zero.home.TodoViewHolder;
import ekylibre.zero.intervention.InterventionActivity;

/**************************************
 * Created by pierre on 7/11/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/

public class ReceptionAdapter extends ArrayAdapter<ReceptionDataModel>
{

    public ReceptionAdapter(Context context, List<ReceptionDataModel> receptionDataModels)
    {
        super(context, 0, receptionDataModels);
    }

    /*
    ** Dynamic set of ReceptionDataModels which is use in receptionActivity
    ** Read android documentation to understand how convertView works
    **
    */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_reception_act, parent, false);
        }

        TextView productinfo = (TextView) convertView.findViewById(R.id.ProductinfoId);
        TextView clientname = (TextView) convertView.findViewById(R.id.ClientNameId);
        TextView receptiondate = (TextView) convertView.findViewById(R.id.ReceptiondateId);

        ReceptionDataModel itemDataModel = super.getItem(position);

        receptiondate.setText(itemDataModel.getReceived_at());


        return (convertView);
    }
}






//        ReceptionViewHolder viewHolder = (ReceptionViewHolder) convertView.getTag();
//        if (viewHolder == null)
//        {
//            viewHolder = new ReceptionViewHolder();
//            getViews(viewHolder, convertView);
//            convertView.setTag(viewHolder);
//        }
//
//        ReceptionDataModel item = super.getItem(position);
//        viewHolder.event.setText(item.getReceived_at());


//        return (convertView);
//    }
//
//    public class ReceptionViewHolder {
//        public TextView         event;
//    }
//
//
//
//    private void getViews(ReceptionViewHolder viewHolder, View convertView)
//    {
//        viewHolder.event = convertView.findViewById(R.id.event);
//    }