package ekylibre.zero.inter.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;


public class ParamType {

    public static final String DRIVER = "driver";
    public static final String TRACTOR = "tractor";
    public static final String SOWER = "sower";

    private ParamType(@Type String type) {
        System.out.println("ParamType :" + type);
    }

    // Declare the @ StringDef for these constants:
    @StringDef({DRIVER, TRACTOR, SOWER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}

    public static void main(String[] args) {
        ParamType paramType = new ParamType(TRACTOR);
    }
}
