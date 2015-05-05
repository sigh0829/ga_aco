package com.pineislet.graduation.util;

import org.junit.Test;

/**
 * Created on 15/5/5
 *
 * @author Yasenia
 */

public class RouletteTest {
    @Test
    public void testRoulette() {
        double[] weightArray = new double[] {
                1000, 1, 1, 2000, 3000, 1, 0, 0
        };

        boolean[] banArray = new boolean[] {
                false, false, true, true, false, false, false, false
        };

        for (int i = 0; i < 10; i++) {
            System.out.println(Roulette.roulette(weightArray, banArray));
        }

    }

}
