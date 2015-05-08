package com.pineislet.graduation.aco;

import com.pineislet.graduation.ga.Individual;
import com.pineislet.graduation.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 *
 * 蚁群
 */

public class AntColony implements Individual {

    public static final int M_BIN_LENGTH = 8;
    public static final int Q_BIN_LENGTH = 10;
    public static final int ALPHA_BIN_LENGTH = 10;
    public static final int BETA_BIN_LENGTH = 10;
    public static final int LAMBDA_BIN_LENGTH = 9;
    /**
     *  蚁群数量
     * */
    private int m;

    /**
     *  信息素强度
     * */
    private double q;

    /**
     *  信息启发因子
     * */
    private double alpha;

    /**
     *  期望启发因子
     * */
    private double beta;

    /**
     *  信息素挥发系数
     * */
    private double lambda;

    /**
     *  构造方法
     * */
    public AntColony(int m, double q, double alpha, double beta, double lambda) {
        this.m = m;
        this.q = q;
        this.alpha = alpha;
        this.beta = beta;
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
                    int nextPosition = Utils.roulette(weightArray, bans[i]);
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

            if (solution.getCount() > 100) {
                flag = false;
            }
        }
        return solution;
    }

    @Override
    public double calcFitness() {
//        List<TSPSolution> solutionList = new ArrayList<>();
//        double minDistanceSum = 0;
//        for (int i = 0; i < 5; i++) {
//            TSPSolution solution = solveTSP(TSP.DEFAULT_TSP);
//            minDistanceSum += solution.getMinDistance();
//        }
//        return 1 / (minDistanceSum / 5 - 426);
        // TODO 测试时只算一次，实际运行去多次平均
        return  1 / (solveTSP(TSP.DEFAULT_TSP).getMinDistance() / 5 - 426);
    }

    @Override
    public boolean[] getGene() {
        boolean[] mBinArray = Utils.encodeGray(Utils.numberToBinArray(m, M_BIN_LENGTH));
        boolean[] qBinArray = Utils.encodeGray(Utils.numberToBinArray((long) q, Q_BIN_LENGTH));
        boolean[] alphaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (alpha * 100), ALPHA_BIN_LENGTH));
        boolean[] betaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (beta * 100), BETA_BIN_LENGTH));
        boolean[] lambdaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (lambda * 100), LAMBDA_BIN_LENGTH));

        return Utils.concat(mBinArray, qBinArray, alphaBinArray, betaBinArray, lambdaBinArray);
    }

    @Override
    public Individual createIndividual(boolean[] gene) {
        boolean[] mBinArray = Arrays.copyOfRange(gene, 0, M_BIN_LENGTH);
        boolean[] qBinArray = Arrays.copyOfRange(gene, M_BIN_LENGTH, M_BIN_LENGTH + Q_BIN_LENGTH);
        boolean[] alphaBinArray = Arrays.copyOfRange(gene,
                M_BIN_LENGTH + Q_BIN_LENGTH,
                M_BIN_LENGTH + Q_BIN_LENGTH + ALPHA_BIN_LENGTH);
        boolean[] betaBinArray = Arrays.copyOfRange(gene,
                M_BIN_LENGTH + Q_BIN_LENGTH + ALPHA_BIN_LENGTH,
                M_BIN_LENGTH + Q_BIN_LENGTH + ALPHA_BIN_LENGTH + BETA_BIN_LENGTH);
        boolean[] lambdaBinArray = Arrays.copyOfRange(gene,
                M_BIN_LENGTH + Q_BIN_LENGTH + ALPHA_BIN_LENGTH + BETA_BIN_LENGTH,
                M_BIN_LENGTH + Q_BIN_LENGTH + ALPHA_BIN_LENGTH + BETA_BIN_LENGTH + LAMBDA_BIN_LENGTH);


        int m = (int) Utils.binArrayToNumber(Utils.decodeGray(mBinArray));
        double q = Utils.binArrayToNumber(Utils.decodeGray(qBinArray));
        double alpha = Utils.binArrayToNumber(Utils.decodeGray(alphaBinArray)) / 100.0;
        double beta = Utils.binArrayToNumber(Utils.decodeGray(betaBinArray)) / 100.0;
        double lambda = Utils.binArrayToNumber(Utils.decodeGray(lambdaBinArray)) / 100.0;

        if (lambda > 1) {
            System.out.println(lambda);
        }
        return new AntColony(m, q, alpha, beta, lambda);
    }


    /**
     *  getter
     * */
    public int getM() {
        return m;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getQ() {
        return q;
    }

    public double getLambda() {
        return lambda;
    }

}
