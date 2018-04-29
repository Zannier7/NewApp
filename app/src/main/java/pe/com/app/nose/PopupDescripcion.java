package pe.com.app.nose;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import static pe.com.app.nose.R.*;

public class PopupDescripcion extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.popup_descripcion);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

    }
}
