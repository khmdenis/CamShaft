package com.amcbridge.camshaft.service;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.CamShaftType;

public class CommonCamShaftCalculator implements CamShaftCalculator{

    public static CamShaftCalculator build(CamShaft camShaft){
        return new CommonCamShaftCalculator(camShaft);
    }

        private CamShaftCalculator theoreticalProfileCalculator;
        private CamShaftCalculator practicalProfileCalculator;
        private CommonCamShaftCalculator(CamShaft camShaft){
            switch(camShaft.getType()){
                case ROTATING: theoreticalProfileCalculator = new RotatingProfileCalculator(camShaft);
                case TRANSLATING:  theoreticalProfileCalculator = new TranslatingProfileCalculator(camShaft);
            }
            practicalProfileCalculator =  new PracticalProfileCalculator(camShaft);
        }
        @Override
        public void calculateProfile() {
            theoreticalProfileCalculator.calculateProfile();
            practicalProfileCalculator.calculateProfile();
        }
}
