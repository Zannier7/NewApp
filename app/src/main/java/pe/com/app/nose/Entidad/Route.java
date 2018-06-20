package pe.com.app.nose.Entidad;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pe.com.app.nose.Model.Distance;
import pe.com.app.nose.Model.Duration;


public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
