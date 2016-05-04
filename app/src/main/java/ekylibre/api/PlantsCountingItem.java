package ekylibre.api;

import java.util.List;

/**
 * Created by antoine on 22/04/16.
 */
public class PlantsCountingItem {
    private List<Integer> mValues;
    private Float mAverage;
    private String mObservations;

    public PlantsCountingItem(List<Integer> values, Float average, String observations){
        mValues = values;
        mAverage = average;
        mObservations = observations;
    }

}
