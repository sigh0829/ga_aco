package com.pineislet.graduation.aco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 */

public class TSPSolution {
    /**
     *  所求TSP问题
     * */
    private TSP tsp;

    /**
     *  迭代次数
     * */
    private int count;

    /**
     *  最短行程
     * */
    private double minDistance;

    /**
     *  最短路径
     * */
    private int[] minPath;

    /**
     *  历代最短行程
     * */
    private List<Double> minDistanceList = new ArrayList<>();

    /**
     *  历代最短路径
     * */
    private List<int[]> minPathList = new ArrayList<>();

    public TSPSolution(TSP tsp) {
        this.tsp = tsp;
        count = 0;
        minDistance = Double.MAX_VALUE;
    }

    public void addPath(double distance, int[] path) {
        count++;
        minDistanceList.add(distance);
        minPathList.add(path);
        if (distance < minDistance) {
            minDistance = distance;
            minPath = path;
        }
    }


    public TSP getTsp() {
        return tsp;
    }

    public int getCount() {
        return count;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public int[] getMinPath() {
        return minPath;
    }

    public List<Double> getMinDistanceList() {
        return minDistanceList;
    }

    public List<int[]> getMinPathList() {
        return minPathList;
    }
}
