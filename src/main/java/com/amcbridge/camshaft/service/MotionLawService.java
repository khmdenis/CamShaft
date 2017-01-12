package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Static class for operate motion law.
 */

public class MotionLawService {
    private MotionLawService(){
        throw new AssertionError();
    }

    /**
     * Method calculate list of point for motion law.
     * It calculate every value of y in every step.
     *
     * @param function - String with expression motion law .
     *                 Example: "sin(x)*180";
     * @param step     - step of x. After calculating of point x increase on the step value
     *                 Result is writing in  {@link CamShaft#motionLaw} field.
     *                 Calculating begins in point x = 0 and stops in point x = 360.
     */
    public static List<Point> calculateMotionLaw(String function, double step) {
        final double START = 0.0;
        final double END = 360.0;
        List<Point> motionLaw = new ArrayList<>();
        Expression e = new ExpressionBuilder(function).variables("x").build();
        for (double x = START; x <= END; x += step) {
            e.setVariable("x", x);
            Point p = new Point(x, Double.valueOf(e.evaluate()));
            motionLaw.add(p);
        }
        return  motionLaw;
    }

    /**
     *
     * @param file - The data is writing into this file.
     * @param motionLaw - Writing into file data.
     * @return file with motion law data.
     * @throws IOException
     */
    public static File writeMotionLawToFile(File file, List<Point> motionLaw) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Point p : motionLaw) {
                fileWriter.write(p.getX() + " " + p.getY() + "\n");
            }
            fileWriter.flush();
        }
        return file;
    }

    /**
     *
     * @param file - The data is reading from this file.
     * @return List of point for motion law.
     * @throws FileNotFoundException
     */
    public static List<Point> readMotionLawFromFile(File file) throws FileNotFoundException {
        List<Point> motionLaw = new ArrayList<>();
        try (Scanner scanner = new Scanner(file).useLocale(Locale.UK)) {
            while (scanner.hasNext()) {
                motionLaw.add(new Point(scanner.nextDouble(), scanner.nextDouble()));
            }
        }
        return motionLaw;
    }

}
