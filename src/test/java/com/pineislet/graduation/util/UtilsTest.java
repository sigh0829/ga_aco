package com.pineislet.graduation.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created on 15/5/5
 *
 * @author Yasenia
 */

public class UtilsTest {
    @Test
    @Ignore
    public void testRoulette() {
        double[] weightArray = new double[] {
                1000, 1, 1, 2000, 3000, 1, 0, 0
        };

        boolean[] banArray = new boolean[] {
                false, false, true, true, false, false, false, false
        };

        for (int i = 0; i < 10; i++) {
            System.out.println(Utils.roulette(weightArray, banArray));
        }
    }

    @Test
    public void testBinArrayToNumber() throws Exception {
        for (int i = 0; i < 100; i++) {
            long t = (long) (Math.random() * 1000);
            Assert.assertEquals(t, Utils.binArrayToNumber(Utils.numberToBinArray(t, 10)));
        }
    }

    @Test
    public void testEncodeBin() throws Exception {
        for (int i = 0; i < 100; i++) {
            double t = (Math.random() * 1000);
            Assert.assertEquals(t, Utils.decodeBin(Utils.encodeBin(t, 0, 1000, 10), 0, 1000, 10), 1);
        }
    }

    @Test
    public void testDecodeBin() throws Exception {

    }
}
