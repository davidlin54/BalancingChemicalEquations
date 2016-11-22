package com.davidlin54.chemistry;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by David on 2016-11-15.
 */

public class MainPresenterImpl implements MainPresenter {

    @Override
    public void balanceEquation(List<String> reactantsString, List<String> productsString) {
        try {
            ChemicalEquation chemicalEquation = ChemicalEquation.buildEquation(reactantsString, productsString);
            double[] coeff = chemicalEquation.coefficients();

            for (int i = 0; i < coeff.length; i++) {
                Log.i("read54", coeff[i]+"");
            }
        } catch (InvalidMatrixSizeException e) {
            e.printStackTrace();
        } catch (BalancedEquationError balancedEquationError) {
            balancedEquationError.printStackTrace();
        }
    }

}
