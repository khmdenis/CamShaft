package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;

import static java.lang.Math.*;
import java.util.Map;


public class TranslatingProfileCalculator extends CentralProfileCalculator {

    public TranslatingProfileCalculator(CamShaft camShaft){
        this.camShaft = camShaft;
        Map<String,Double> props = camShaft.getParameters();
        X = props.get("X");
        Rmin = props.get("Rmin");
    }

    @Override
    protected double getPsi(double Ri) {
        double Ymin = sqrt(pow(Rmin,2) + pow(X,2));
        double q = 0.0;
        if (X < 0) q = PI;
        return (q +  atan(Ymin / X));
    }

    @Override
    protected double getRi(double Sbi) {
        double S0 = sqrt(pow(Rmin,2) +  pow(X,2));
        return sqrt(pow((S0 + Sbi),2) +  pow(X,2));
    }

}
