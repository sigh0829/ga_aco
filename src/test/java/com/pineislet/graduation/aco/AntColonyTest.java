package com.pineislet.graduation.aco;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created on 15/5/5
 * @author Yasenia
 */

public class AntColonyTest {
    private AntColony antColony;

    @Before
    public void before() throws IOException {
        antColony = new AntColony(30, 1, 3, 150, 0.3);
    }

    @Test
    public void testSolveTSP() throws Exception {
        TSP tsp = TSP.DEFAULT_TSP;
        System.out.println(tsp.calcDistance(new int[]{
                0, 21, 7, 25, 30, 27, 2, 35, 34, 19,
                1, 28, 20, 15, 49, 33, 29, 8, 48, 9,
                38, 32, 44, 14, 43, 41, 39, 18, 40, 12,
                24, 13, 23, 42, 6, 22, 47, 5, 26, 50,
                45, 11, 46, 17, 3, 16, 36, 4, 37, 10, 31}));
        TSPSolution solution = antColony.solveTSP(tsp);
        System.out.println("\t历史迭代最短距离：" + solution.getMinDistance());
        System.out.print("\t历史迭代最短路径：");
        for (int position : solution.getMinPath()) {
            System.out.print(position + " ");
        }
        System.out.println();
        for (int i = 0; i < solution.getCount(); i++) {
            System.out.println("第" + i + "次迭代");
            System.out.println("\t最短距离：" + solution.getMinDistanceList().get(i));
            System.out.print("\t最短路径：");
            int[] minPath = solution.getMinPathList().get(i);
            for (int position : minPath) {
                System.out.print(position + " ");
            }
            System.out.println();
        }
    }
}