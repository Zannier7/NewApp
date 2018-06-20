package pe.com.app.nose.Interfaces;

import java.util.List;

import pe.com.app.nose.Entidad.Route;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
