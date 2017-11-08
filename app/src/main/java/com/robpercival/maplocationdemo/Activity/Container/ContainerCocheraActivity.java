package com.robpercival.maplocationdemo.Activity.Container;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robpercival.maplocationdemo.Activity.Container.Fragment.CocheraFragment;
import com.robpercival.maplocationdemo.Activity.Container.Fragment.ServiciosFragment;
import com.robpercival.maplocationdemo.Model.Cochera;
import com.robpercival.maplocationdemo.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerCocheraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_cochera);


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.setDefaultTab(R.id.detalleCochera);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            Cochera cochera= (Cochera) getIntent().getSerializableExtra("Cochera");
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.detalleCochera:
                            CocheraFragment cocheraFragment = new CocheraFragment();
                            cocheraFragment.setCochera(cochera);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_Cocheras, cocheraFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                        break;
                    case R.id.detalleServicios:
                        ServiciosFragment serviciosFragment = new ServiciosFragment();
                        serviciosFragment.setCochera(cochera);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_Cocheras, serviciosFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        break;

                   /* getSupportFragmentManager().beginTransaction().replace(R.id.container_Cocheras, serviciosFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();*/
                    /*Don't call ft.addToBackStack(null) when you add the Fragment in onCreate.
                    That tells the FragmentManger that you have another state BEFORE that fragment that you want to be able to jump back to.*/
                }
            }
        });

    }
}
