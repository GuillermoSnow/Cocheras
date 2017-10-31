package com.robpercival.maplocationdemo.Activity.Container;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robpercival.maplocationdemo.Activity.Container.Fragment.CocheraFragment;
import com.robpercival.maplocationdemo.Activity.Container.Fragment.ServiciosFragment;
import com.robpercival.maplocationdemo.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerCocheraActivity extends AppCompatActivity {
    CocheraFragment cocheraFrag;
    ServiciosFragment serviciosFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_cochera);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.setDefaultTab(R.id.detalleCochera);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.detalleCochera:
                        CocheraFragment cocheraFragment = new CocheraFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_Cocheras, cocheraFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.detalleServicios:
                        ServiciosFragment serviciosFragment = new ServiciosFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_Cocheras, serviciosFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;

                }
            }
        });

    }
}
