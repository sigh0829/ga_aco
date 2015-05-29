package com.pineislet.graduation.bag;

import com.pineislet.graduation.util.Utils;

import java.util.Arrays;

/**
 * Created on 2015/5/27.
 * @author Yasenia
 */
public class AntColony {

    public static final int MAX_LOOP_COUNT = 100;

    private int m;
    private double Q;
    private double alpha;
    private double beta;
    private double rho;

    public AntColony(int m, double Q, double alpha, double beta, double rho) {
        this.m = m;
        this.Q = Q;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
    }

/**
 *  求解 0-1背包 问题
 * */
public boolean[] solveBagProblem(BagProblem bagProblem) {
    // 记录信息素数组
    double[] pheromoneArray = new double[bagProblem.n];

    for (int i  = 0; i < pheromoneArray.length; i++) {
        pheromoneArray[i] = 1;
    }
    // 记录最优解
    double maxValue = 0;
    // 记录最优路径
    boolean[] maxSolution = new boolean[bagProblem.n];
    // 迭代
    for (int i = 0; i < MAX_LOOP_COUNT; i++) {
        // 记录本次迭代的最优解
        double currentValue = 0;
        double currentWeigth = 0;
        // 记录本次迭代的最优路径
        boolean[] solution = new boolean[bagProblem.n];
        // 对所有蚂蚁进行操作
        for (int j = 0; j < m; j++) {
            // 计算轮盘赌权重
            double[] weightArray = new double[bagProblem.n];
            for (int k = 0; k < bagProblem.n; k++) {
                weightArray[k] = Math.pow(pheromoneArray[k], alpha) * Math.pow(bagProblem.vArray[k] / bagProblem.wArray[k], beta);
            }
            boolean[] banArray = new boolean[bagProblem.n];
            boolean[] currentSolution = new boolean[bagProblem.n];

            double wSum = 0;
            double vSum = 0;
            boolean flag = true;
            while (flag) {
                // 轮盘赌选择下一件物品
                int index = Utils.roulette(weightArray, banArray);
                wSum += bagProblem.wArray[index];
                vSum += bagProblem.vArray[index];
                banArray[index] = true;
                currentSolution[index] = true;
                // 无法被选择的物品加入禁忌表
                flag = false;
                for (int k = 0; k < bagProblem.n; k++) {
                    if (!banArray[k]) {
                        if (bagProblem.wArray[k] + wSum > bagProblem.capacity) {
                            banArray[k] = true;
                        }
                        else {
                            // 若尚有能选择的物品，则继续循环
                            flag = true;
                        }
                    }
                }
            }
            if (vSum > currentValue) {
                currentValue = vSum;
                currentWeigth = wSum;
                System.arraycopy(currentSolution, 0, solution, 0, bagProblem.n);
            }
        }
        // 打印出本次循环的最优解
        System.out.println(i + "\t" + currentValue + "\t" + currentWeigth);

        if (maxValue < currentValue) {
            maxValue = currentValue;
            System.arraycopy(solution, 0, maxSolution, 0, bagProblem.n);
        }
        // 更新信息素
        for (int j = 0; j < bagProblem.n; j++) {
            pheromoneArray[j] *= rho;
            if (solution[j]) {
                pheromoneArray[j] += Q * bagProblem.vArray[j] / currentValue;
            }
        }
    }
    return maxSolution;
}

    public static void main(String[] args) {

        double[] wArray = new double[]
                {
                78.49060493, 35.01845871, 44.8817435, 86.55968788, 42.54245717, 11.94508957, 41.7637125, 22.1937714, 94.40883967, 16.61729196,
                10.54493422, 76.77022886, 86.16235947, 65.20129067, 35.9159732, 13.84682241, 28.66950597, 7.318852825, 45.59709683, 14.87483999,
                80.69367718, 1.516582512, 81.76855581, 53.45075461, 86.34416338, 86.05384212, 41.35466185, 63.5192863, 8.136690192, 48.52487279,
                44.92722521, 17.54351607, 27.93117676, 22.83079141, 18.99401055, 55.65766031, 13.0123001, 67.01705822, 1.437833887, 31.81812885,
                69.57850716, 0.492039497, 4.871903318, 67.9685174, 30.90511842, 4.142321334, 97.67706445, 32.23412734, 52.44710438, 11.86034449,
                87.48654176, 82.16164628, 11.14719275, 47.0000514, 16.88381391, 39.01964258, 31.21493924, 82.27610857, 6.079258168, 84.17544865,
                83.64234007, 26.62237984, 2.331177272, 93.56410329, 31.72166732, 67.52523836, 20.87682096, 51.26495232, 19.8311121, 22.3473621,
                87.13907426, 8.201550284, 27.7538877, 77.15763138, 14.97436586, 76.98045589, 37.79638713, 16.58275613, 80.72749928, 78.88459885,
                86.97106006, 73.19626687, 77.85896097, 84.03678466, 1.509589392, 49.96045929, 61.91039219, 10.74900847, 35.97203314, 94.23696133,
                20.57379317, 75.07317942, 64.21752461, 26.88153133, 28.6301337, 6.568881239, 94.50218681, 54.93870021, 72.52296754, 71.30381145
        };
//        {54 , 29 , 78 , 77 , 71 , 42 , 80 , 97 , 6 , 37 , 77 , 60 , 30 , 65 , 28 , 83 , 99 , 23 , 99 , 54 , 100 , 2 , 11 , 80 , 5 , 39 , 95 , 41 , 17 , 65 , 42 , 33 , 21 , 59 , 46 , 27 , 38 , 92 , 63 , 10 , 70 , 84 , 55, 44 , 51 , 47 , 41 , 6 , 98 , 40 , 49 , 48 , 63 , 16 , 66 , 40 , 79 , 76 , 84 , 22, 11 , 13 , 54 , 55 , 9 , 68 , 36 , 71 , 54 , 76 , 47 , 21 , 10 , 17 , 10 , 28 , 76 , 68 , 9 , 33 , 30 , 49 , 35 , 49 , 87 , 76 , 34 , 9 , 42 , 12 , 63 , 15 , 22 , 14 , 35 , 93 , 41 , 83 , 73 , 34};
        double[] vArray = new double[]
                {
                68.11731264, 49.12720958, 0.526982525, 9.895890292, 3.616221519, 47.9653447, 41.26957537, 90.66520882, 5.772814762, 62.14493739,
                9.32333027, 30.4315707, 25.18585746, 47.84774823, 11.84618478, 88.50393705, 7.519433722, 79.64527645, 39.49834093, 69.82485612,
                53.0809873, 76.19160386, 28.19619207, 5.622423898, 18.34881992, 49.99460432, 16.13605442, 14.58590707, 22.33968076, 50.40616821,
                71.47826757, 26.20568923, 45.7070671, 93.97200505, 91.08235501, 72.91813731, 35.46085845, 1.281735764, 95.68379333, 82.69603181,
                53.19893028, 84.45981745, 71.90309837, 28.7251736, 29.74132415, 54.43798764, 36.47778645, 17.80779966, 14.05767337, 42.1498135,
                23.10786929, 24.13343462, 29.3784213, 52.7879902, 17.30549056, 0.057787414, 74.3805443, 97.26500473, 89.63935583, 20.53034461,
                32.33832759, 70.01780256, 2.764544, 42.81125537, 76.12987677, 67.29706077, 92.62835851, 88.11816188, 81.98996041, 65.08171916,
                49.24281465, 76.84357563, 89.89981055, 0.759366356, 47.41441902, 73.98343896, 73.50451593, 27.71313758, 2.821833785, 26.41749844,
                7.448634056, 36.95570244, 53.83207354, 98.68210349, 45.02062793, 64.95103902, 94.4458624, 38.87605746, 43.99677364, 31.76453591,
                8.762738931, 31.34016802, 3.575154323, 78.68211348, 82.35659371, 17.75108043, 0.290373951, 99.4389558, 90.95220718, 93.27006478
        };
//        {71, 58, 31, 2, 82 , 5, 87 , 38 , 88 , 95 , 53 , 6 , 47 , 63 , 27 , 83 , 59 , 92 , 70 , 25 , 11 , 68 , 58 , 11 , 29 , 30 , 31 , 98 , 28 , 17 , 42 , 72 , 64 , 19 , 9 , 91 , 79 , 29 , 64 , 43 , 57 , 92 , 3, 92 , 68 , 52 , 36 , 27 , 25 , 7 , 37 , 16 , 26 , 55 , 94 , 51 , 11 , 46 , 60 , 2 , 8 , 34, 1 , 66 , 83 , 20 , 46 , 15 , 93 , 9 , 41 , 50 , 33 , 59 , 93 , 45 , 88 , 28 , 26 , 4 , 80 , 24 , 26 , 5 , 21 , 59 , 93 , 55 , 64 , 97 , 93 , 35 , 48 , 100 , 3 , 55 , 54 , 85 , 68 , 100};
        BagProblem problem = new BagProblem();
        problem.n = 100;
        problem.wArray = wArray;
        problem.vArray = vArray;

        for (int i = 0; i < 100; i++) {
            problem.capacity += wArray[i] * 0.8;
        }

        AntColony antColony = new AntColony(100, 1, 1, 1, 0.7);
        boolean[] s = antColony.solveBagProblem(problem);


        double max = 0;
        for (int i = 0; i < 100; i++) {
            max += s[i] ? vArray[i] : 0;
        }

        System.out.println(max);
    }
}
