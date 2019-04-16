package ekylibre.util.layout.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.zero.R;
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.fragment.InterventionFormFragment.OnFragmentInteractionListener;
import ekylibre.zero.inter.model.SimpleSelectableItem;


public class WidgetParamView extends ConstraintLayout {

    @BindView(R.id.widget_label) TextView labelView;
    @BindView(R.id.widget_icon) ImageView iconView;
    @BindView(R.id.widget_add) MaterialButton addButton;
    @BindView(R.id.widget_chips_group) ChipGroup chipGroup;

    public WidgetParamView(Context context, OnFragmentInteractionListener listener,
                           @Type String paramType, List<SimpleSelectableItem> paramList) {
        super(context);
        init(context, listener, paramType, paramList);
    }

    public WidgetParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetParamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WidgetParamView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, OnFragmentInteractionListener listener, @Type String type,
                     List<SimpleSelectableItem> paramList) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(inflater).inflate(R.layout.widget_param_layout, this);
        ButterKnife.bind(this, view);

//        if(attrs == null)
//            return;

//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WidgetParamView);
//        String label = ta.getString(R.styleable.WidgetParamView_label);
//        int iconId = ta.getResourceId(R.styleable.WidgetParamView_icon, R.drawable.icon_calendar);
//
//        TextView labelView = view.findViewById(R.id.widget_label);
//        labelView.setText(label);
//
//        ImageView iconView = view.findViewById(R.id.widget_icon);
//        iconView.setImageResource(iconId);
//
//        ta.recycle();

        final String packageName = context.getPackageName();

        @StringRes
        final int labelRes = getResources().getIdentifier(type, "string", packageName);

        @DrawableRes
        final int iconRes = getResources().getIdentifier("icon_" + type, "drawable", packageName);

        labelView.setText(labelRes);
        iconView.setImageResource(iconRes);

        addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(type));

        for (SimpleSelectableItem item : paramList) {
            if (item.type.equals(type) && item.isSelected) {
                Chip chip = new Chip(context);
                chip.setText(item.label);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    chipGroup.removeView(chip);
                    paramList.get(paramList.indexOf(item)).isSelected = false;
                    displayOrNot(paramList, type);
                });
                chipGroup.addView(chip);
            }
        }
        displayOrNot(paramList, type);
    }

    private void displayOrNot(List<SimpleSelectableItem> paramList, @Type String type) {
        boolean itemCounter = false;
        for (SimpleSelectableItem item : paramList)
            if (item.type.equals(type) && item.isSelected) {
                itemCounter = true;
                break;
            }
        chipGroup.setVisibility(itemCounter ? View.VISIBLE : View.GONE);
    }

}