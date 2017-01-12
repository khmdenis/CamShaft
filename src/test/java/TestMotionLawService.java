import com.amcbridge.camshaft.model.Point;
import com.amcbridge.camshaft.service.MotionLawService;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public class TestMotionLawService {

    @Test
    public void testCalculateMotionLaw() {
        List<Point> actual = MotionLawService.calculateMotionLaw("x+1", 90.0);
        assertThat(actual, hasItems(
                new Point(0.0, 1.0),
                new Point(90.0, 91.0),
                new Point(180.0, 181.0),
                new Point(270.0, 271.0),
                new Point(360.0, 361.0)
        ));


    }

    @Test
    public void testReadMotionLawFromFile() throws FileNotFoundException {
        File file = new File("src\\test\\java\\test_file.txt");
        List<Point> actual = MotionLawService.readMotionLawFromFile(file);
        assertThat(actual, hasItems(
                new Point(0.0, 0.0),
                new Point(1.0, 1.0),
                new Point(2.0, 2.0)
        ));
    }

    @Test
    public void testWriteMotionLawToFile() throws IOException {
        File file = new File("src\\test\\java\\output.txt");
        List<Point> motionLaw = Arrays.asList(
                new Point(0.0, 0.0),
                new Point(1.0, 1.0)
        );
       MotionLawService.writeMotionLawToFile(file,motionLaw);
       assertTrue(file.length()>0);
        file.delete();
    }
}
