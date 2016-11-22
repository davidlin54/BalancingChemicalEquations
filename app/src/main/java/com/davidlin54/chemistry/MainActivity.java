package com.davidlin54.chemistry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainPresenterImpl impl = new MainPresenterImpl();
        List<String> reactant = new ArrayList<>();
        reactant.add("KI");
        reactant.add("KClO3");
        reactant.add("HCl");
        List<String> product = new ArrayList<>();
        product.add("I2");
        product.add("H2O");
        product.add("KCl");
        impl.balanceEquation(reactant, product);
    }
}
