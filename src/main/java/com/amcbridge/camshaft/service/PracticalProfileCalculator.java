package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;

import java.util.List;

import static java.lang.Math.*;

/**
 * Class for calculating practical profile of camshaft.
 */
public class PracticalProfileCalculator implements CamShaftCalculator {

    private CamShaft camShaft;
    private double d;

    public PracticalProfileCalculator(CamShaft camShaft) {
        this.camShaft = camShaft;
        d = camShaft.getParameters().get("d");
    }

    /**
     * Parameters {{@link CamShaft#centralProfile} should be not empty for using this method.
     * The method should work with {@link CamShaft#outerCurve} and {@link CamShaft#innerCurve}
     * It calculates theoretical profile of camshaft by theoretical profile of camshaft.
     */
    @Override
    public void calculateProfile() {

        if (camShaft.getOuterCurve() != null) {
            camShaft.getOuterCurve().clear();
            camShaft.getInnerCurve().clear();
        }
        List<Point> centralProfile = camShaft.getCentralProfile();
        for (int i = 0; i < centralProfile.size() - 2; i++) { calcPoints(i,i+2,i+1); }
        calcPoints(centralProfile.size()-2,0,centralProfile.size()-1);
        calcPoints(centralProfile.size()-1,1,0);
    }

private void calcPoints(int i1, int i2, int i3){
    double x;
    double y;
    Point p1 = camShaft.getCentralProfile().get(i1);
    Point p2 = camShaft.getCentralProfile().get(i2);
    Point p3 = camShaft.getCentralProfile().get(i3);
    double x1 = p3.getX();
    double y1 = p3.getY();
    //Ax + By + C = 0
    double A = p1.getY() - p2.getY();
    double B = p2.getX() - p1.getX();
    //Quadratic equation
    double Ax = 1 + pow(B / A, 2);
    double Bx = -2 * x1 - 2 * B / A * B / A * x1;
    double Cx = pow(x1, 2) + pow(B / A, 2) * pow(x1, 2) - pow(d / 2, 2);
    //Discriminant
    double D = pow(Bx, 2) - 4 * Ax * Cx;
    x = (p2.getY() <= p1.getY()) ? (-Bx + sqrt(D)) / (2 * Ax) : (-Bx - sqrt(D)) / (2 * Ax);
    y = B / A * (x - x1) + y1;
    camShaft.getOuterCurve().add(new Point(x, y));
    x = (p2.getY() >= p1.getY()) ? (-Bx + sqrt(D)) / (2 * Ax) : (-Bx - sqrt(D)) / (2 * Ax);
    y = B / A * (x - x1) + y1;
    camShaft.getInnerCurve().add(new Point(x, y));
}
}
