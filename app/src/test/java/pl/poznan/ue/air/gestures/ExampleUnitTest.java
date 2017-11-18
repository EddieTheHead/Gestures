package pl.poznan.ue.air.gestures;


import org.junit.Test;

import static org.junit.Assert.*;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void  javaMl_isWorking() throws Exception{
        double[] values = new double[] { 1, 2, 3};
        Instance instance = new DenseInstance(values);
        assertEquals(1.0, instance.value(1));
        assertEquals(2.0, instance.value(2));
        assertEquals(3.0, instance.value(3));
    }

}