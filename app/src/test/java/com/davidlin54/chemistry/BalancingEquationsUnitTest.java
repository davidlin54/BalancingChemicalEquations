package com.davidlin54.chemistry;

import com.davidlin54.chemistry.exceptions.BalancedEquationException;
import com.davidlin54.chemistry.exceptions.InvalidMatrixSizeException;
import com.davidlin54.chemistry.exceptions.ProductException;
import com.davidlin54.chemistry.exceptions.ReactantException;
import com.davidlin54.chemistry.models.ChemicalEquation;
import com.davidlin54.chemistry.models.Compound;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BalancingEquationsUnitTest  {
    @Test
    public void testCorrectEquations() throws InvalidMatrixSizeException, BalancedEquationException, ReactantException, ProductException {
        final String[] reactants = {
                "Fe + Cl2",
                "KMnO4 + HCl",
                "K4Fe(CN)6 + H2SO4 + H2O",
                "C6H5COOH + O2",
                "K4Fe(CN)6 + KMnO4 + H2SO4",
                "(C6H5)CH3 + KMnO4 + H2SO4"
        };

        final String[] products = {
                "FeCl3",
                "KCl + MnCl2 + H2O + Cl2",
                "K2SO4 + FeSO4 + (NH4)2SO4 + CO",
                "CO2 + H2O",
                "KHSO4 + Fe2(SO4)3 + MnSO4 + HNO3 + CO2 + H2O",
                "(C6H5)COOH + K2SO4 + MnSO4 + H2O"
        };

        final int[][] coefficients = {
                {2, 3, 2},
                {2, 16, 2, 2, 8, 5},
                {1, 6, 6, 2, 1, 3, 6},
                {2, 15, 14, 6},
                {10, 122, 299, 162, 5, 122, 60, 60, 188},
                {5, 6, 9, 5, 3, 6, 14}
        };

        for (int i = 0; i < reactants.length; i++) {
            final ChemicalEquation chemicalEquation = ChemicalEquation.buildEquation(reactants[i].replace(" ", "").split("\\+"), products[i].replace(" ", "").split("\\+"));
            chemicalEquation.balance();

            Iterator<Map.Entry<Compound, Integer>> balancedReactants = chemicalEquation.getReactants().entrySet().iterator();
            Iterator<Map.Entry<Compound, Integer>> balancedProducts = chemicalEquation.getProducts().entrySet().iterator();

            for (int j = 0; j < chemicalEquation.getReactants().size(); j++) {
                assertEquals((int) balancedReactants.next().getValue(), coefficients[i][j]);
            }

            for (int j = 0; j < chemicalEquation.getProducts().size(); j++) {
                assertEquals((int)balancedProducts.next().getValue(), coefficients[i][j + chemicalEquation.getReactants().size()]);
            }
        }
    }

    @Test
    public void testInfiniteSolutions() throws InvalidMatrixSizeException {
        final String[] reactants = {
                "H2O + K",
                "I4O9"
        };

        final String[] products = {
                "H2 + O2 + K2O",
                "I2O6 + I2 + O2"
        };


        for (int i = 0; i < reactants.length; i++) {
            try {
                final ChemicalEquation chemicalEquation = ChemicalEquation.buildEquation(reactants[i].replace(" ", "").split("\\+"), products[i].replace(" ", "").split("\\+"));
                chemicalEquation.balance();

                throw new AssertionError("Equation has infinite solutions, expected exception");
            } catch (BalancedEquationException e) {
                assertEquals(e.getExceptionType(), BalancedEquationException.BalancedEquationExceptionType.INFINITE_SOLUTIONS);
            } catch (ProductException e) {
                e.printStackTrace();
            } catch (ReactantException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testNoSolutions() throws InvalidMatrixSizeException {
        final String[] reactants = {
                "K2",
                "H2O + O2"
        };

        final String[] products = {
                "O",
                "HNO3"
        };

        for (int i = 0; i < reactants.length; i++) {
            try {
                final ChemicalEquation chemicalEquation = ChemicalEquation.buildEquation(reactants[i].replace(" ", "").split("\\+"), products[i].replace(" ", "").split("\\+"));
                chemicalEquation.balance();

                throw new AssertionError("Equation cannot be balanced, expected exception");
            } catch (BalancedEquationException e) {
                assertEquals(e.getExceptionType(), BalancedEquationException.BalancedEquationExceptionType.CAN_NOT_BALANCE);
            } catch (ProductException e) {
                e.printStackTrace();
            } catch (ReactantException e) {
                e.printStackTrace();
            }
        }
    }
}