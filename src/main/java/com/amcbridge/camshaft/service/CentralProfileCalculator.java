package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;

import static java.lang.Math.*;

import java.util.List;

/**
 * Class for calculating theoretical profile of camshaft.
 */

public abstract class CentralProfileCalculator implements CamShaftCalculator {

    protected CamShaft camShaft;

    protected double Ps0;
    protected double Rmin;
    protected double aw;
    protected double L;
    protected double X;
    protected double Y;
    protected double Fi2i;
    double d;



       /** Parameters {@link CamShaft#motionLaw} and {@link CamShaft#parameters} should be not empty and correct for using this method.
        * The method should work with {@link CamShaft#theoreticalProfile}.
        * It calculates theoretical profile of camshaft by parameters and motion law.
        */
       @Override
    public void calculateProfile() {
        if (camShaft.getCentralProfile() != null) camShaft.getCentralProfile().clear();
        List<Point> motionLaw = camShaft.getMotionLaw();
        if (motionLaw.isEmpty()) {
            return;
        }
           double precision = camShaft.getParameters().get("Precision");
        aw = getPusherLength(new Point(0.0, 0.0), new Point(X, Y));
        Ps0 = getPs0(aw);
           double x = motionLaw.get(0).getX();
        for (int i = 0; i < motionLaw.size() - 1; i++) {
            Point p1 = motionLaw.get(i);
            Point p2 = motionLaw.get(i + 1);
            double k = 0.0;
            if (p2.getY() != p1.getY()) {
                k = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
            }
            double b = p1.getY() - k * p1.getX();
            while (x <= p2.getX()) {
                double Fi = x * PI / 180;
                double Sbi = k*x+b;
                double Ri = getRi(Sbi);
                double Psi = getPsi(Ri);
                camShaft.getCentralProfile().add(new Point(
                        Ri * cos(Fi + Psi),
                        -1 * Ri * sin(Fi + Psi)
                ));
                x += precision;
            }
        }

    }

    /**
     * @param aw - length of pusher.
     * @return Angle between axis of cam and pusher.
     */

    protected double getPs0(double aw) {
        return acos((pow(aw, 2) + pow(Rmin, 2) - pow(L, 2)) / (2 * aw * Rmin));
    }

    /**
     * @param p1 - begin of coordinates.
     * @param p2 - position of cam.
     * @return Method return length of pusher.
     */
    protected double getPusherLength(Point p1, Point p2) {
        return sqrt(pow(p1.getX() - p2.getX(), 2) + pow(p1.getY() - p2.getY(), 2));
    }

    /**
     * This method calculate an angle determines position relatively of axis.
     * @param Ri - current radius of central profile
     * @return double
     */

    protected abstract double getPsi(double Ri);

    /**
     * This method calculate the current radius of central profile.
     * @param Sbi - Position of pusher
     * @return double
     */

    protected abstract double getRi(double Sbi);
}
