package org.firstinspires.ftc.teamcode.subsystems;

public class lookUp {
    private double[][] data = new double[0][2];

    public lookUp() {
    }

    public lookUp(double[][] measurements) {
        setData(measurements);
    }

    public void setData(double[][] measurements) {
        if (measurements == null) {
            throw new IllegalArgumentException("Measurements cannot be null");
        }

        double[][] replacement = new double[measurements.length][2];
        for (int i = 0; i < measurements.length; i++) {
            double[] row = measurements[i];
            if (row == null || row.length != 2
                    || isInvalidValue(row[0]) || isInvalidValue(row[1])) {
                throw new IllegalArgumentException(
                        "Each row must contain a finite, nonnegative distance and RPM");
            }
            if (i > 0 && row[0] <= replacement[i - 1][0]) {
                throw new IllegalArgumentException("Distances must be strictly increasing");
            }
            replacement[i][0] = row[0];
            replacement[i][1] = row[1];
        }
        data = replacement;
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public double getRPM(double distance) {
        if (isInvalidValue(distance)) {
            throw new IllegalArgumentException("Distance must be finite and nonnegative");
        }
        if (isEmpty()) {
            return 0;
        }
        if (distance <= data[0][0]) {
            return data[0][1];
        }

        for (int i = 1; i < data.length; i++) {
            if (distance <= data[i][0]) {
                double fraction = (distance - data[i - 1][0])
                        / (data[i][0] - data[i - 1][0]);
                return data[i - 1][1] * (1 - fraction) + data[i][1] * fraction;
            }
        }
        return data[data.length - 1][1];
    }

    private static boolean isInvalidValue(double value) {
        return Double.isNaN(value) || Double.isInfinite(value) || value < 0;
    }
}
