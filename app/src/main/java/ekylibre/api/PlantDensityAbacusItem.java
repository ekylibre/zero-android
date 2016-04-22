package ekylibre.api;

import ekylibre.zero.ZeroContract;

/**
 * Created by antoine on 21/04/16.
 */
public class PlantDensityAbacusItem {

    private float mSeedingDensityValue;
    private int mPlantsCount;

    public PlantDensityAbacusItem(float seedingDensityValue, int plantsCount){
        mSeedingDensityValue = seedingDensityValue;
        mPlantsCount = plantsCount;
    }
}
