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

    public static final int MAX_LOOP_COUNT = 100;

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
     *
     *  @param tsp 给定的tsp问题
     *  @return 求得的解
     * */
    public TSPSolution solveTSP(final TSP tsp) {
        TSPSolution solution = new TSPSolution();
        // 初始化信息素矩阵
        double[][] pheromoneMatrix = new double[tsp.n][tsp.n];
        for (int i = 0; i < tsp.n; i++) {
            for (int j = 0; j < tsp.n; j++) {
                pheromoneMatrix[i][j] = q / tsp.averageDistance * tsp.n;
            }
        }
        // 开始迭代求解
        for (int loopCount = 0; loopCount < MAX_LOOP_COUNT; loopCount++) {
            // 信息素增量矩阵
            double[][] pheromoneDeltaMatrix = new double[tsp.n][tsp.n];
            // 记录蚂蚁路径
            int[][] paths = new int[m][tsp.n];
            // 记录蚂蚁禁忌表
            boolean[][] tabus = new boolean[m][tsp.n];
            // 初始化路径记录与禁忌表
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < tsp.n; j++) {
                    paths[i][j] = -1;
                    tabus[i][j] = false;
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
                tabus[i][position] = true;
                // 遍历城市
                for (int j = 1; j < tsp.n; j++) {
                    // 当前所在位置
                    int currentPosition = paths[i][j - 1];
                    // 计算权重
                    double[] weightArray = new double[tsp.n];
                    for (int k = 0; k < weightArray.length; k++) {
                        if (!tabus[i][k]) {
                            weightArray[k] = Math.pow(pheromoneMatrix[currentPosition][k], alpha) /
                                    Math.pow(tsp.distanceMatrix[currentPosition][k], beta);
                        }
                        else {
                            weightArray[k] = 0;
                        }
                    }
                    // 选取下一个城市
                    int nextPosition = Utils.roulette(weightArray, tabus[i]);
                    // 更新路径
                    paths[i][j] = nextPosition;
                    // 更新禁忌表
                    tabus[i][nextPosition] = true;
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
        return  1 / (solveTSP(TSP.DEFAULT_TSP).getMinDistance() - 426);
    }

    @Override
    public List<boolean[]> getGenome() {
        List<boolean[]> genome = new ArrayList<>();

        boolean[] mBinArray = Utils.encodeGray(Utils.numberToBinArray(m, M_BIN_LENGTH));
        boolean[] qBinArray = Utils.encodeGray(Utils.numberToBinArray((long) q, Q_BIN_LENGTH));
        boolean[] alphaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (alpha * 100), ALPHA_BIN_LENGTH));
        boolean[] betaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (beta * 100), BETA_BIN_LENGTH));
        boolean[] lambdaBinArray = Utils.encodeGray(Utils.numberToBinArray((long) (lambda * 100), LAMBDA_BIN_LENGTH));

        genome.add(mBinArray);
        genome.add(qBinArray);
        genome.add(alphaBinArray);
        genome.add(betaBinArray);
        genome.add(lambdaBinArray);

        return genome;
    }

    @Override
    public Individual createIndividual(List<boolean[]> genome) {
        int m = (int) Utils.binArrayToNumber(Utils.decodeGray(genome.get(0)));
        double q = Utils.binArrayToNumber(Utils.decodeGray(genome.get(1)));
        double alpha = Utils.binArrayToNumber(Utils.decodeGray(genome.get(2))) / 100.0;
        double beta = Utils.binArrayToNumber(Utils.decodeGray(genome.get(3))) / 100.0;
        double lambda = Utils.binArrayToNumber(Utils.decodeGray(genome.get(4))) / 100.0;

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
