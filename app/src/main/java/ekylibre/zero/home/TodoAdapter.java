package ekylibre.zero.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.home.TodoItem;
import ekylibre.zero.home.TodoViewHolder;

/**************************************
 * Created by pierre on 7/11/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/

public class TodoAdapter extends ArrayAdapter<TodoItem>
{

    public TodoAdapter(Context context, List<TodoItem> todoList)
    {
        super(context, 0, todoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        TodoViewHolder viewHolder = (TodoViewHolder) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new TodoViewHolder();
            viewHolder.event = (TextView)convertView.findViewById(R.id.event);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
            viewHolder.hours = (TextView)convertView.findViewById(R.id.startDate);
            viewHolder.minutes = (TextView)convertView.findViewById(R.id.endDate);
            convertView.setTag(viewHolder);
        }
        TodoItem    item = getItem(position);
        viewHolder.event.setText(item.getEvent());
        viewHolder.hours.setText(item.getStartDate());
        viewHolder.minutes.setText(item.getEndDate());
        viewHolder.desc.setText(item.getDesc());

        return (convertView);
    }
}
