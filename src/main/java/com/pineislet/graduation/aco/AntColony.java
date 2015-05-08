package com.pineislet.graduation.aco;

import com.pineislet.graduation.ga.Individual;
import com.pineislet.graduation.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 *
 * 蚁群
 */

public class AntColony implements Individual {

    /**
     *  蚁群数量
     * */
    private int m;

    /**
     *  信息启发因子
     * */
    private double alpha;

    /**
     *  期望启发因子
     * */
    private double beta;

    /**
     *  信息素强度
     * */
    private double q;

    /**
     *  信息素挥发系数
     * */
    private double lambda;


    public AntColony(int m, double alpha, double beta, double q, double lambda) {
        this.m = m;
        this.alpha = alpha;
        this.beta = beta;
        this.q = q;
        this.lambda = lambda;
    }

    /**
     *  求解给定的 TSP 问题
     * */
    public TSPSolution solveTSP(final TSP tsp) {
        // 初始化信息素矩阵
        double[][] pheromoneMatrix = new double[tsp.n][tsp.n];
        for (int i = 0; i < tsp.n; i++) {
            for (int j = 0; j < tsp.n; j++) {
                pheromoneMatrix[i][j] = q / tsp.averageDistance;
            }
        }

        // 开始迭代求解
        boolean flag = true;

        TSPSolution solution = new TSPSolution();
        while (flag) {
            // 信息素增量矩阵
            double[][] pheromoneDeltaMatrix = new double[tsp.n][tsp.n];
            // 记录蚂蚁路径
            int[][] paths = new int[m][tsp.n];
            // 记录蚂蚁禁忌表
            boolean[][] bans = new boolean[m][tsp.n];
            // 初始化路径记录与禁忌表
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < tsp.n; j++) {
                    paths[i][j] = -1;
                    bans[i][j] = false;
                }
            }

            // 记录所有蚂蚁的最短行程
            double currentMinDistance = Double.MAX_VALUE;
            int[] currentMinPath = new int[tsp.n];
            // 对所有蚂蚁进行遍历操作
            for (int i = 0; i < m; i++) {
                // 随机初始化蚂蚁位置
                int position = 0;
                // 更新路径
                paths[i][0] = position;
                // 更新禁忌表
                bans[i][position] = true;
                // 遍历城市
                for (int j = 1; j < tsp.n; j++) {
                    // 当前所在位置
                    int currentPosition = paths[i][j - 1];
                    // 计算权重
                    double[] weightArray = new double[tsp.n];
                    for (int k = 0; k < weightArray.length; k++) {
                        if (!bans[i][k]) {
                            weightArray[k] = Math.pow(pheromoneMatrix[currentPosition][k], alpha) /
                                    Math.pow(tsp.distanceMatrix[currentPosition][k], beta);
                        }
                        else {
                            weightArray[k] = 0;
                        }
                    }

                    // 选取下一个城市
                    int nextPosition = MathUtil.roulette(weightArray, bans[i]);
                    // 更新路径
                    paths[i][j] = nextPosition;
                    // 更新禁忌表
                    bans[i][nextPosition] = true;
                }
                // 计算总路程
                double distance = tsp.calcDistance(paths[i]);

                // 释放信息素
                double pheromoneDelta = q / distance;
                for (int j = 0; j < tsp.n - 1; j++) {
                    pheromoneDeltaMatrix[paths[i][j]][paths[i][j + 1]] += pheromoneDelta;
                }
                pheromoneDeltaMatrix[paths[i][tsp.n - 1]][paths[i][0]] += pheromoneDelta;

                // 若该蚂蚁找到较短的路径，则更新路程信息
                if (distance < currentMinDistance) {
                    currentMinDistance = distance;
                    currentMinPath = paths[i];
                }
            }
            // 更新信息素
            for (int i = 0; i < tsp.n; i++) {
                for (int j = 0; j < tsp.n; j++) {
                    // 信息素挥发
                    pheromoneMatrix[i][j] *= 1 - lambda;
                    // 信息素增加
                    pheromoneMatrix[i][j] += pheromoneDeltaMatrix[i][j];
                }
            }
            // 记录本次迭代路程信息
            solution.addPath(currentMinDistance, currentMinPath);

            if (solution.getCount() > 200) {
                flag = false;
            }
        }
        return solution;
    }

    @Override
    public double calcFitness() {
        List<TSPSolution> solutionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TSPSolution solution = solveTSP(TSP.DEFAULT_TSP);
        }



        return 0;
    }

    @Override
    public boolean[] getGene() {
        return new boolean[0];
    }

    @Override
    public Individual createIndividual(boolean[] gene) {
        return null;
    }

}
