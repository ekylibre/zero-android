package ekylibre.zero.reception;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
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

public class ReceptionAdapter extends ArrayAdapter<List>
{

    public ReceptionAdapter(Context context, ArrayList<List> resultset)
    {
        super(context, 0, resultset);
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

        List list = super.getItem(position);
        Log.i("MyTag","date " +list.get(0));
        Log.i("MyTag","reception number " +list.get(1));
        Log.i("MyTag","name " +list.get(2));


        receptiondate.setText((CharSequence) list.get(0));
        productinfo.setText((CharSequence) list.get(1));
        clientname.setText((CharSequence) list.get(2));

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
