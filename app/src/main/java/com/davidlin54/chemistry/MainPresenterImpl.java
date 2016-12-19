package com.davidlin54.chemistry;

import com.davidlin54.chemistry.models.ChemicalEquation;

import java.util.List;

/**
 * Created by David on 2016-11-15.
 */

public class MainPresenterImpl implements MainPresenter {

    @Override
    public void balanceEquation(List<String> reactantsString, List<String> productsString) {
        try {
            ChemicalEquation chemicalEquation = ChemicalEquation.buildEquation(reactantsString, productsString);
            chemicalEquation.balance();
        } catch (InvalidMatrixSizeException e) {
            e.printStackTrace();
        } catch (BalancedEquationError balancedEquationError) {
            balancedEquationError.printStackTrace();
        }
    }

}
