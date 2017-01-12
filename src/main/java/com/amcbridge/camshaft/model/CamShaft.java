package com.amcbridge.camshaft.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Special class for contain data about camshaft.
 */

public class CamShaft {
    /**
     * Type of pusher.
     */
    private CamShaftType type;
    /**
     * Parameters of cam shaft
     */
    private Map<String, Double> parameters;
    /**
     * Motion law of pusher
     */
    private List<Point> motionLaw;
    /**
     * Theoretical cam shaft profile
     */
    private List<Point> centralProfile;
    /**
     * Outer curve of practical profile
     */
    private List<Point> outerCurve;
    /**
     * Inner curve of practical profile
     */
    private List<Point> innerCurve;

    public void setInnerCurve(List<Point> innerCurve) {
        this.innerCurve = innerCurve;
    }

    public List<Point> getInnerCurve() {
        return innerCurve;
    }

    /**
     * Default constructor initialize all lists.
     */
    private CamShaft() {
        setParameters(new HashMap<>());
        setMotionLaw(new ArrayList<>());
        setCentralProfile(new ArrayList<>());
        setOuterCurve(new ArrayList<>());
        setInnerCurve(new ArrayList<>());
    }

    /**
     * @return New instance of CamShaft
     */
    public static CamShaft newInstance() {
        return new CamShaft();
    }

    /**
     * @return type of cam shaft (CamShaftType)
     */
    public CamShaftType getType() {
        return type;
    }

    /**
     * @param type = type of cam shaft
     */
    public void setType(CamShaftType type) {
        this.type = type;
    }

    /**
     * @return Map contains parameters of cam shaft
     */

    public Map<String, Double> getParameters() {
        return parameters;
    }

    /**
     * @param parameters - Map contains parameters of cam shaft
     */

    public void setParameters(Map<String, Double> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return List contains points of motion law
     */

    public List<Point> getMotionLaw() {
        return motionLaw;
    }

    /**
     * @param motionLaw - List contains points of motion law
     */

    public void setMotionLaw(List<Point> motionLaw) {
        this.motionLaw = motionLaw;
    }

    /**
     * @return List contains points of theoretical profile of cam shaft
     */

    public List<Point> getCentralProfile() {
        return centralProfile;
    }

    /**
     * @param centralProfile - List contains points of theoretical profile of cam shaft
     */

    public void setCentralProfile(List<Point> centralProfile) {
        this.centralProfile = centralProfile;
    }

    /**
     * @return List contains points of practical profile of cam shaft
     */

    public List<Point> getOuterCurve() {
        return outerCurve;
    }

    /**
     * @param outerCurve - List contains points of practical profile of cam shaft
     */

    public void setOuterCurve(List<Point> outerCurve) {
        this.outerCurve = outerCurve;
    }
}
