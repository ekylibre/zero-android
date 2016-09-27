package ekylibre.zero.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.intervention.InterventionActivity;

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

    /*
    ** Dynamic set of todoList which is use in mainActivity
    ** Read android documentation to understand how convertView works
    **
    ** headerState => it's a date block
    ** messageState => it's a special message block
    */
    @NonNull
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
            getViews(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }
        TodoItem    item = getItem(position);
        if (!item.getHeaderState() && !item.getMessageState())
        {
            setEventBlock(viewHolder, item);
        }
        else if (!item.getMessageState())
        {
            setDateBlock(viewHolder, item);
        }
        else
        {
            setSpecialMessageBlock(viewHolder, item);
        }
        return (convertView);
    }

    private void setSpecialMessageBlock(TodoViewHolder viewHolder, TodoItem item)
    {
        viewHolder.header.setVisibility(View.GONE);
        viewHolder.event.setVisibility(View.GONE);
        viewHolder.hours.setVisibility(View.GONE);
        viewHolder.minutes.setVisibility(View.GONE);
        viewHolder.desc.setVisibility(View.GONE);
        viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.color.invisible));
        viewHolder.layout.setFocusable(true);
        viewHolder.message.setVisibility(View.VISIBLE);
        viewHolder.message.setText(item.getMessageString());
        viewHolder.imageState.setVisibility(View.GONE);
    }

    private void setDateBlock(TodoViewHolder viewHolder, TodoItem item)
    {
        viewHolder.header.setText(item.getDay());
        viewHolder.header.setVisibility(View.VISIBLE);
        viewHolder.event.setVisibility(View.GONE);
        viewHolder.hours.setVisibility(View.GONE);
        viewHolder.minutes.setVisibility(View.GONE);
        viewHolder.desc.setVisibility(View.GONE);
        viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.color.invisible));
        viewHolder.layout.setFocusable(true);
        viewHolder.message.setVisibility(View.GONE);
        viewHolder.imageState.setVisibility(View.GONE);
    }

    private void setEventBlock(TodoViewHolder viewHolder, TodoItem item)
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
        viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.drawable.event_green));
        if (item.getSource() == TodoListActivity.LOCAL_CALENDAR)
            viewHolder.layout.setBackground(getContext().getResources().getDrawable(R.drawable.event_blue));
        viewHolder.layout.setFocusable(false);
        viewHolder.message.setVisibility(View.GONE);
        drawStatusIcon(viewHolder, item);
    }

    private void drawStatusIcon(TodoViewHolder viewHolder, TodoItem item)
    {
        if (item.getState() == null)
            viewHolder.imageState.setVisibility(View.GONE);
        else if (item.getState().equals(InterventionActivity.STATUS_PAUSE))
        {
            viewHolder.imageState.setVisibility(View.VISIBLE);
            viewHolder.imageState.setImageDrawable(getContext().getResources().getDrawable(R
                    .drawable.ic_pause));
        }
        else if (item.getState().equals(InterventionActivity.STATUS_FINISHED))
        {
            viewHolder.imageState.setVisibility(View.VISIBLE);
            viewHolder.imageState.setImageDrawable(getContext().getResources().getDrawable(R
                    .drawable.check));
        }
        else
            viewHolder.imageState.setVisibility(View.GONE);
    }

    private void getViews(TodoViewHolder viewHolder, View convertView)
    {
        viewHolder.layout = (RelativeLayout)convertView.findViewById(R.id.todoItemLayout);
        viewHolder.event = (TextView)convertView.findViewById(R.id.event);
        viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
        viewHolder.hours = (TextView)convertView.findViewById(R.id.startDate);
        viewHolder.minutes = (TextView)convertView.findViewById(R.id.endDate);
        viewHolder.header = (TextView)convertView.findViewById(R.id.todayDate);
        viewHolder.message = (TextView)convertView.findViewById(R.id.message);
        viewHolder.imageState = (ImageView)convertView.findViewById(R.id.state);
    }
}
