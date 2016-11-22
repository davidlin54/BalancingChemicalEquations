package com.davidlin54.chemistry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by David on 2016-11-21.
 */

public class ChemicalEquation extends Matrix {
    private ChemicalEquation(double[] matrix) throws InvalidMatrixSizeException {
        super(matrix);
    }

    private ChemicalEquation(double[][] matrix) throws InvalidMatrixSizeException {
        super(matrix);
    }

    private ChemicalEquation(Matrix toCopy) {
        super(toCopy);
    }

    public static ChemicalEquation buildEquation(List<String> reactantsString, List<String> productsString) throws InvalidMatrixSizeException {
        Set<Element> elementSet = new LinkedHashSet<>();
        List[] elementAtomsList = new List[reactantsString.size() + productsString.size()];

        int count = 0;

        for (String reactantString : reactantsString) {
            Compound compound = new Compound(reactantString);
            // get set of all elements in the equation
            for (Map.Entry<Element, Integer> element : compound.getElementMap().entrySet()) {
                elementSet.add(element.getKey());
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet) {
                int atoms = compound.getElementMap().get(element) == null ? 0 : compound.getElementMap().get(element);
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
        }

        for (String productString : productsString) {
            Compound compound = new Compound(productString);

            // get set of all elements in the equation
            for (Map.Entry<Element, Integer> element : compound.getElementMap().entrySet()) {
                elementSet.add(element.getKey());
            }

            List<Integer> atomsList = new ArrayList<>();
            for (Element element : elementSet) {
                int atoms = compound.getElementMap().get(element) == null ? 0 : compound.getElementMap().get(element);
                atomsList.add(atoms);
            }

            elementAtomsList[count] = atomsList;
            count++;
        }

        // build the atoms matrix
        double[][] atomsMatrix = new double[elementSet.size()][elementAtomsList.length];
        for (int i = 0; i < elementAtomsList.length; i++) {
            for (int j = 0; j < elementAtomsList[i].size(); j++) {
                atomsMatrix[j][i] = (int) elementAtomsList[i].get(j);
            }
        }

        return new ChemicalEquation(atomsMatrix);
    }

    // get the nullity of the equation
    private int nullity(int rank) {
        return mColumns - rank;
    }

    // the nullspace of the matrix is the coefficients of the compounds
    private double[] nullSpace() throws InvalidMatrixSizeException, BalancedEquationError {
        Matrix rowEchelonForm = rowEchelonForm();
        int rank = rowEchelonForm.rank(true);

        // check if equation cannot be balanced or there are infinite solutions
        if (nullity(rank) == 0) {
            throw new BalancedEquationError(BalancingChemicalEquations.getContext().getString(R.string.balanced_cannot_error));
        } else if (nullity(rank) >= 2) {
            throw new BalancedEquationError(BalancingChemicalEquations.getContext().getString(R.string.balanced_infinite_error));
        }

        double[] nullSpace = new double[rank];
        for (int i = 0; i < rank; i++) {
            nullSpace[i] = rowEchelonForm.mMatrix[i][mColumns - 1];
        }

        return nullSpace;
    }

    public double[] coefficients() throws InvalidMatrixSizeException, BalancedEquationError {
        double[] nullSpace = nullSpace();
        double[] coefficients = new double[nullSpace.length + 1];
        // add the last coefficient to the list
        coefficients[nullSpace.length] = -1;

        double minCoefficient = Double.MAX_VALUE;
        for (int i = 0; i < nullSpace.length; i++) {
            double coefficient = nullSpace[i];
            if (Math.abs(coefficient) < minCoefficient) {
                minCoefficient = coefficient;
            }
            coefficients[i] = coefficient;
        }

        // divide all coefficients by the smallest until all are integers
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] /= minCoefficient;
        }

        boolean isInteger = false;
        while (!isInteger) {
            isInteger = true;
            double lcd = 1;
            for (int i = 0; i < coefficients.length; i++) {
                if (coefficients[i] != Math.rint(coefficients[i])) {
                    lcd /= coefficients[i] - (long) coefficients[i];
                    break;
                }
            }

            for (int i = 0; i < coefficients.length; i++) {
                coefficients[i] *= lcd;
                if (coefficients[i] != Math.rint(coefficients[i])) {
                    isInteger = false;
                }
            }
        }

        return coefficients;
    }
}
