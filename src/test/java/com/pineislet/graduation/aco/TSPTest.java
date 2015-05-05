package com.pineislet.graduation.aco;

import org.junit.Test;

/**
 * Created on 15/5/5
 * @author Yasenia
 */

public class TSPTest {

    @Test
    public void testLoadData() throws Exception {
        TSP tsp = TSP.loadTSPByInputStream(TSPTest.class.getClassLoader().getResourceAsStream("ALL-tsp/eil51.tsp"));
        for (int i = 0; i < tsp.n; i++) {
            System.out.println(i + " " + tsp.xArray[i] + " " + tsp.yArray[i]);
        }
    }
}