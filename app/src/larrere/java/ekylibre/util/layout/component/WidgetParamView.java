package ekylibre.util.layout.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.util.antlr4.Grammar;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.util.query_language.DSL;
import ekylibre.zero.R;
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.fragment.InterventionFormFragment.OnFragmentInteractionListener;
import ekylibre.zero.inter.model.GenericItem;


public class WidgetParamView extends ConstraintLayout {

    @BindView(R.id.widget_label) TextView labelView;
//    @BindView(R.id.widget_icon) ImageView iconView;
    @BindView(R.id.widget_add) MaterialButton addButton;
    @BindView(R.id.widget_chips_group) ChipGroup chipGroup;

    private static String role;

    public WidgetParamView(Context context, OnFragmentInteractionListener listener,
                           GenericEntity entity, List<GenericItem> paramList) {
        super(context);
        role = entity.name;
        init(context, listener, entity.name, entity.filter, paramList);
    }

    public WidgetParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetParamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public WidgetParamView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void init(Context context, OnFragmentInteractionListener listener, @Type String type,
                     String filter, List<GenericItem> paramList) {

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
//
//        @DrawableRes
//        final int iconRes = getResources().getIdentifier("icon_" + type, "drawable", packageName);
////        if (iconRes == 0) {
////            List<String> driverList = Arrays.asList("doer", "driver");
////            if ()
////                iconRes = driverList.contains(type) ? R.drawable.icon_driver
////        }
//
        labelView.setText(labelRes);
//        iconView.setImageResource(iconRes);

        addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(type, filter));

        // Old way (canopy parser)
//        List<String> requiredAbilities = DSL.parse(filter);

        List<String> requiredAbilities = Grammar.parse(filter);


        outer: for (GenericItem item : paramList) {
            if (item.isSelected && item.abilities != null) {
                // Check all abilities are satified
                Log.e("View", "Role [" + role + "] required abilities = " + requiredAbilities + " / item abilities = " + Arrays.asList(item.abilities));
                for (String requiredAbility : requiredAbilities)
                    if (!Arrays.asList(item.abilities).contains(requiredAbility))
                        continue outer;
                // Do things with matching item
                for (String referenceName : item.referenceName) {
                    Log.e("View", "ref name = " + referenceName + " / procedure role = " + role);
                    if (referenceName.equals(role)) {
                        Chip chip = new Chip(context);
                        chip.setText(item.name);
                        chip.setCloseIconVisible(true);
                        chip.setOnCloseIconClickListener(v -> {
                            chipGroup.removeView(chip);
                            paramList.get(paramList.indexOf(item)).isSelected = false;
                            displayOrNot(paramList, filter);
                        });
                        chipGroup.addView(chip);
                    }
                }
            }
        }
        displayOrNot(paramList, filter);
    }

    private void displayOrNot(List<GenericItem> paramList, String filter) {

        List<String> requiredAbilities = DSL.parse(filter);

        boolean itemCounter = false;
        outer: for (GenericItem item : paramList) {
            if (item.abilities != null) {
                // Check all abilities are satified
                for (String requiredAbility : requiredAbilities)
                    if (!Arrays.asList(item.abilities).contains(requiredAbility))
                        continue outer;
                if (item.isSelected) {
                    itemCounter = true;
                    break;
                }
            }
        }
        chipGroup.setVisibility(itemCounter ? View.VISIBLE : View.GONE);
    }

}