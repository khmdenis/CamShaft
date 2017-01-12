import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.Point;
import com.amcbridge.camshaft.service.CamShaftCalculator;
import com.amcbridge.camshaft.service.PracticalProfileCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TestPracticalProfileCalculator {
    private CamShaft camShaft = CamShaft.newInstance();
    private CamShaftCalculator practicalProfileCalculator;

    @Before
    public void setup() {
        camShaft.getParameters().put("d", 1.0);
        practicalProfileCalculator = new PracticalProfileCalculator(camShaft);
    }

    @Test
    public void testCalculateProfile() {
        camShaft.getCentralProfile().add(new Point(1.0, 2.0));
        camShaft.getCentralProfile().add(new Point(2.0, 1.0));
        camShaft.getCentralProfile().add(new Point(2.0, -1.0));
        camShaft.getCentralProfile().add(new Point(1.0, -2.0));
        camShaft.getCentralProfile().add(new Point(-1.0, -2.0));
        camShaft.getCentralProfile().add(new Point(-2.0, -1.0));
        camShaft.getCentralProfile().add(new Point(-2.0, 1.0));
        camShaft.getCentralProfile().add(new Point(-1.0, 2.0));
        practicalProfileCalculator.calculateProfile();
        assertTrue(camShaft.getOuterCurve().size()==camShaft.getCentralProfile().size());
        assertTrue(camShaft.getInnerCurve().size()==camShaft.getCentralProfile().size());
    }
}
