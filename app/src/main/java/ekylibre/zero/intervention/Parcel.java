package ekylibre.zero.intervention;


import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygon;

import java.util.List;

/*************************************************
 * Created by pierre on 11/14/16.                *
 * ekylibre.zero.intervention for zero-android   *
 ************************************************/

public class Parcel
{
    GeoJsonLayer layer;
    List<GeoJsonPolygon>    polygons;
    private String          name;
    private int             number;

    public Parcel(GeoJsonLayer layer, List<GeoJsonPolygon> polygons)
    {
        this.layer = layer;
        layer.addLayerToMap();
        this.polygons = polygons;
    }

    public String getName()
    {
        return (name);
    }

    public int getNumber()
    {
        return (number);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }
}
