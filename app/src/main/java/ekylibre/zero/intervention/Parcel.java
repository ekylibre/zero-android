package ekylibre.zero.intervention;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygon;

import java.util.List;

/*************************************************
 * Created by pierre on 11/14/16.                *
 * ekylibre.zero.intervention for zero-android   *
 ************************************************/

public class Parcel
{
    GeoJsonLayer            layer;
    List<GeoJsonPolygon>    polygons;
    private String          name;
    private int             number;

    public Parcel(GeoJsonLayer layer, List<GeoJsonPolygon> polygons)
    {
        this.layer = layer;
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
