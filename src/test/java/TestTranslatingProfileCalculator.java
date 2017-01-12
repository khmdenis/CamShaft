import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.CamShaftType;
import com.amcbridge.camshaft.model.Point;
import com.amcbridge.camshaft.service.CamShaftCalculator;
import com.amcbridge.camshaft.service.CommonCamShaftCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

public class TestTranslatingProfileCalculator {
    private CamShaft camShaft = CamShaft.newInstance();
    private CamShaftCalculator camShaftCalculator;
    @Before
    public void setup(){
        camShaft.setType(CamShaftType.TRANSLATING);
        Map<String,Double> props = new HashMap<>();
        props.put("X", 75.0);
        props.put("d", 22.0);
        props.put("Rmin", 30.5);
        props.put("Precision",0.5);
        camShaft.setParameters(props);
        camShaft.getMotionLaw().addAll(Arrays.asList(
                new Point(0.0, 0.0),
                new Point (180.0, 70.0),
                new Point (360.0, 70.0)

        ));
        camShaftCalculator = CommonCamShaftCalculator.build(camShaft);
    }
    @Test
    public void testCalculateProfile(){
        camShaftCalculator.calculateProfile();
        assertFalse(camShaft.getCentralProfile().isEmpty());
    }
}
