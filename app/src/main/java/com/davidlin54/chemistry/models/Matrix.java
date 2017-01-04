package com.davidlin54.chemistry.models;

import com.davidlin54.chemistry.exceptions.InvalidMatrixSizeException;

import java.util.Arrays;

/**
 * Created by David on 2016-11-09.
 */

public class Matrix {
    protected Fraction[][] mMatrix;
    protected int mRows;
    protected int mColumns;

    public Matrix(Fraction[][] matrix) throws InvalidMatrixSizeException {
        // check if column/row = 0
        if (matrix.length == 0 || matrix[0].length == 0) {
            throw new InvalidMatrixSizeException();
        }

        mRows = matrix.length;
        mColumns = matrix[0].length;

        // check if 2d array is rectangular
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != mColumns) {
                throw new InvalidMatrixSizeException();
            }
        }

        mMatrix = matrix;
    }

    public Matrix(Fraction[] matrix) throws InvalidMatrixSizeException {
        // check if column/row = 0
        if (matrix.length == 0) {
            throw new InvalidMatrixSizeException();
        }

        mRows = 1;
        mColumns = matrix.length;

        mMatrix = new Fraction[1][mColumns];
        mMatrix[0] = matrix;
    }

    public Matrix(Matrix toCopy) {
        mRows = toCopy.mRows;
        mColumns = toCopy.mColumns;
        mMatrix = new Fraction[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            mMatrix[i] = Arrays.copyOf(toCopy.mMatrix[i], toCopy.mColumns);
        }
    }

    private boolean isSameSize(Matrix matrix) {
        return mRows == matrix.mRows && mColumns == matrix.mColumns;
    }

    private boolean isSquareMatrix() {
        return mRows == mColumns;
    }

    public Matrix add(Matrix matrix) throws InvalidMatrixSizeException {
        // check if same size, otherwise invalid operation
        if (!isSameSize(matrix)) {
            throw new InvalidMatrixSizeException();
        }
        Fraction[][] result = new Fraction[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result[i][j] = mMatrix[i][j].add(matrix.mMatrix[i][j]);
            }
        }
        return new Matrix(result);
    }

    public Matrix subtract(Matrix matrix) throws InvalidMatrixSizeException {
        // check if same size, otherwise invalid operation
        if (!isSameSize(matrix)) {
            throw new InvalidMatrixSizeException();
        }
        Fraction[][] result = new Fraction[mRows][mColumns];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result[i][j] = mMatrix[i][j].subtract(matrix.mMatrix[i][j]);
            }
        }
        return new Matrix(result);
    }

    private Fraction determinant(Fraction[][] matrixArray) {
        Fraction determinant = new Fraction(0);
        int length = matrixArray.length;
        if (length == 1) {
            return matrixArray[0][0];
        } else if (length == 2) {
            return matrixArray[0][0].multiply(matrixArray[1][1]).subtract(matrixArray[0][1].multiply(matrixArray[1][0]));
        } else {
            for (int i = 0; i < length; i++) {
                int detSign = (int) Math.pow(-1, i);
                Fraction[][] detArray = new Fraction[length - 1][length - 1];
                // left and right side of the current column
                for (int j = 1; j < length; j++) {
                    for (int k = 0; k < length; k++) {
                        if (k != i) {
                            detArray[j - 1][k - (k > i ? 1 : 0)] = matrixArray[j][k];
                        }
                    }
                }

                determinant = determinant.add(new Fraction(detSign).multiply(matrixArray[0][i]).multiply(determinant(detArray)));
            }
        }

        return determinant;
    }

    public Fraction determinant() throws InvalidMatrixSizeException {
        // check if square matrix, otherwise invalid operation
        if (!isSquareMatrix()) {
            throw new InvalidMatrixSizeException();
        }
        return determinant(mMatrix);
    }

    public boolean isInvertible() throws InvalidMatrixSizeException {
        return determinant().equalsZero();
    }

    public Matrix inverse(Fraction determinant) throws InvalidMatrixSizeException {
        // if determinant = 0, then invalid operation
        if (determinant.equalsZero()) {
            return null;
        }

        Fraction[][] copy = new Fraction[mRows][mColumns];
        Fraction[][] result = new Fraction[mRows][mColumns];

        // setup identity matrix
        for (int i = 0; i < mRows; i++) {
            copy[i] = Arrays.copyOf(mMatrix[i], mColumns);
            result[i][i] = new Fraction(1);
        }

        for (int i = 0; i < mRows; i++) {
            // check pivot value
            if (copy[i][i].equalsZero()) {
                for (int j = i + 1; j < mRows; j++) {
                    if (!copy[j][i].equalsZero()) {
                        Fraction[] temp = copy[i];
                        copy[i] = copy[j];
                        copy[j] = temp;
                        break;
                    }
                }
            }

            Fraction pivotValue = copy[i][i];

            // reduce row
            for (int j = 0; j < mRows; j++) {
                copy[i][j] = copy[i][j].divide(pivotValue);
                result[i][j] = result[i][j].divide(pivotValue);
            }

            // eliminate other rows
            for (int j = 0; j < mRows; j++) {
                if (j != i) {
                    Fraction denom = copy[j][i];
                    for (int k = 0; k < mColumns; k++) {
                        copy[j][k] = copy[j][k].subtract((copy[i][k].multiply(denom)));
                        result[j][k] = result[j][k].subtract((result[i][k].multiply(denom)));
                    }
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix inverse() throws InvalidMatrixSizeException {
        Fraction determinant = determinant();
        return inverse(determinant);
    }

    public Matrix multiply(Fraction multiplier) {
        Matrix result = new Matrix(this);

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result.mMatrix[i][j] = result.mMatrix[i][j].multiply(multiplier);
            }
        }

        return result;
    }

    public Matrix multiply(Matrix multiplier) throws InvalidMatrixSizeException {
        // check if cols = multiplier.rows, otherwise invalid operation
        if (mColumns != multiplier.mRows) {
            throw new InvalidMatrixSizeException();
        }

        Fraction[][] result = new Fraction[mRows][multiplier.mColumns];

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < multiplier.mColumns; j++) {
                for (int k = 0; k < mColumns; k++) {
                    result[i][j] = result[i][j].add((mMatrix[i][k].multiply(multiplier.mMatrix[k][j])));
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix rowEchelonForm() throws InvalidMatrixSizeException {
        Fraction[][] result = new Fraction[mRows][mColumns];

        for (int i = 0; i < mRows; i++) {
            result[i] = Arrays.copyOf(mMatrix[i], mColumns);
        }

        int i2 = 0;
        for (int i = 0; i < mRows && i2 < mColumns; i++, i2++) {
            // check pivot value
            if (result[i][i2].equalsZero()) {
                for (int j = i + 1; j < mRows; j++) {
                    if (!result[j][i2].equalsZero()) {
                        Fraction[] temp = result[i];
                        result[i] = result[j];
                        result[j] = temp;
                        break;
                    }
                }
            }

            // recheck pivot value
            if (result[i][i2].equalsZero()) {
                i--;
            } else {
                Fraction pivotValue = result[i][i2];

                // reduce row
                for (int j = 0; j < mColumns; j++) {
                    result[i][j] = result[i][j].divide(pivotValue);
                    result[i][j].reduce();
                }

                // eliminate other rows
                for (int j = 0; j < mRows; j++) {
                    if (j != i) {
                        Fraction denom = result[j][i2];
                        for (int k = 0; k < mColumns; k++) {
                            result[j][k] = result[j][k].subtract(result[i][k].multiply(denom));
                            result[j][k].reduce();
                        }
                    }
                }
            }
        }

        return new Matrix(result);
    }

    protected int rank() {
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if (!mMatrix[i][j].equalsZero()) {
                    break;
                } else if (j == mColumns - 1) {
                    return i;
                }
            }
        }
        return mRows;
    }

    public int rank(boolean isReduced) throws InvalidMatrixSizeException {
        if (isReduced) {
            return rank();
        }
        return rowEchelonForm().rank();
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                result += mMatrix[i][j].getNumerator() + "/" + mMatrix[i][j].getDenominator() + " ";
            }
            result += "\n";
        }

        return result;
    }
}
