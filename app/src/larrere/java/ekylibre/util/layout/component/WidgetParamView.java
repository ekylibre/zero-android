package ekylibre.util.layout.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.zero.R;
import ekylibre.zero.home.Zero;
import ekylibre.zero.inter.fragment.InterventionFormFragment.OnFragmentInteractionListener;
import ekylibre.zero.inter.model.GenericItem;


public class WidgetParamView extends ConstraintLayout {

    private static final String TAG = "WidgetParamView";

    // @BindView(R.id.widget_icon) ImageView iconView;
    @BindView(R.id.widget_label) TextView labelView;
    @BindView(R.id.widget_add) MaterialButton addButton;
    @BindView(R.id.widget_chips_group) ChipGroup chipGroup;

    private List<GenericItem> paramList;
    private GenericEntity procedure;
    private String role;

    public WidgetParamView(Context context, OnFragmentInteractionListener listener,
                           GenericEntity procedure, List<GenericItem> paramList, String role) {
        super(context);
        this.paramList = paramList;
        this.procedure = procedure;
        this.role = role;
        init(context, listener);
    }

    public WidgetParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetParamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context, OnFragmentInteractionListener listener) {

        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(inflater).inflate(R.layout.widget_param_layout, this);

        // Bind Butterknife to created view
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

        @StringRes
        final int labelRes = getResources().getIdentifier(procedure.name, "string", Zero.getPkgName());
//
//        @DrawableRes
//        final int iconRes = getResources().getIdentifier("icon_" + type, "drawable", packageName);
////        if (iconRes == 0) {
////            List<String> driverList = Arrays.asList("doer", "driver");
////            if ()
////                iconRes = driverList.contains(type) ? R.drawable.icon_driver
////        }
//
        // iconView.setImageResource(iconRes);
        labelView.setText(labelRes);
        addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(procedure.name, procedure.filter, role));

        for (GenericItem item : paramList) {
            if (item.referenceName.containsKey(procedure.name)) {
                Chip chip = new Chip(context);
                chip.setText(item.name);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    // Remove chip
                    chipGroup.removeView(chip);
                    // Remove current procedure in item referenceName list
                    paramList.get(paramList.indexOf(item)).referenceName.remove(procedure.name);
                    // Check whether to display or not
                    displayOrNot(paramList);
                });
                chipGroup.addView(chip);
            }
        }
        displayOrNot(paramList);
    }

    private void displayOrNot(List<GenericItem> filteredItems) {

        int visibility = View.GONE;

        for (GenericItem item : filteredItems)
            if (item.referenceName.containsKey(procedure.name)) {
                visibility = View.VISIBLE;
                break;
            }

        chipGroup.setVisibility(visibility);
    }

}