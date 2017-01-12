package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;

import static java.lang.Math.*;
import java.util.Map;



public class RotatingProfileCalculator  extends CentralProfileCalculator {
    public RotatingProfileCalculator(CamShaft camShaft){
        this.camShaft = camShaft;
        Map<String,Double> props = camShaft.getParameters();
        X = props.get("X");
        Y = props.get("Y");
        L = props.get("L");
        Rmin = props.get("Rmin");
    }

    @Override
    protected double getPsi(double Ri) {
      return asin(L*sin(Fi2i)/Ri)- Ps0;
    }
    @Override
    protected double getRi(double Sbi) {
        double Bi = Sbi/L;
        double Fi20 = (pow(L,2) + pow(aw,2) - pow(Rmin,2))/2*L*aw;
        Fi2i =  Fi20+ Bi;
        return sqrt(pow(aw,2) + pow(L,2) - 2 * aw * L
                * cos(Fi2i));
    }

}

