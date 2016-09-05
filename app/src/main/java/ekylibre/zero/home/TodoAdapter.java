package ekylibre.zero.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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
            viewHolder.layout = (RelativeLayout)convertView.findViewById(R.id.todoItemLayout);
            viewHolder.event = (TextView)convertView.findViewById(R.id.event);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
            viewHolder.hours = (TextView)convertView.findViewById(R.id.startDate);
            viewHolder.minutes = (TextView)convertView.findViewById(R.id.endDate);
            viewHolder.header = (TextView)convertView.findViewById(R.id.todayDate);
            convertView.setTag(viewHolder);
        }
        TodoItem    item = getItem(position);
        if (!item.getHeaderState())
        {
            viewHolder.event.setText(item.getEvent());
            viewHolder.hours.setText(item.getStartDate());
            viewHolder.minutes.setText(item.getEndDate());
            viewHolder.desc.setText(item.getDesc());
            viewHolder.header.setVisibility(View.GONE);
            viewHolder.event.setVisibility(View.VISIBLE);
            viewHolder.hours.setVisibility(View.VISIBLE);
            viewHolder.minutes.setVisibility(View.GONE);
            viewHolder.desc.setVisibility(View.VISIBLE);
            viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.color.basic_green));
            if (item.getSource() == TodoListActivity.LOCAL_CALENDAR)
                viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.color.basic_blue));

        }
        else
        {
            viewHolder.header.setText(item.getDay());
            viewHolder.header.setVisibility(View.VISIBLE);
            viewHolder.event.setVisibility(View.GONE);
            viewHolder.hours.setVisibility(View.GONE);
            viewHolder.minutes.setVisibility(View.GONE);
            viewHolder.desc.setVisibility(View.GONE);
            viewHolder.layout.setBackground(null);
        }
        return (convertView);
    }
}
