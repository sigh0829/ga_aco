package com.pineislet.graduation;

import com.pineislet.graduation.aco.AntColony;
import com.pineislet.graduation.ga.GeneticEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2015/5/8.
 * @author Yasenia
 */

public class Demo {
    public static void main(String[] args) {
        int[] mArray = new int[] {20, 80};
        double[] qArray = new double[] {100, 800};
        double[] alphaArray = new double[] {1, 5};
        double[] betaArray = new double[] {1, 5};
        double[] lambdaArray = new double[] {0.1, 0.5};

        List<AntColony> antColonyPopulation = new ArrayList<>();
        for (int m : mArray) {
            for (double q : qArray) {
                for (double alpha : alphaArray) {
                    for (double beta : betaArray) {
                        for (double lambda : lambdaArray) {
                            antColonyPopulation.add(new AntColony(m, q, alpha, beta, lambda));
                        }
                    }
                }
            }
        }

        GeneticEngine<AntColony> geneticEngine = new GeneticEngine<>(antColonyPopulation);

        for (int i = 0; i < 100; i++) {
            geneticEngine.evolution();
            System.out.println("第" + i + "代种群：");
            showAcPopulation(geneticEngine.getCurrentPopulation());
            System.out.println();
        }
    }

    public static void showAcPopulation(List<AntColony> acPopulation) {
        int size = acPopulation.size();
        System.out.println("个体数量：" + size);
        double mSum = 0;
        double qSum = 0;
        double alphaSum = 0;
        double betaSum = 0;
        double lambdaSum = 0;
        for (int i = 0; i < size; i++) {
            AntColony antColony = acPopulation.get(i);
            System.out.println(i + ": \t" +
                            "m = " + antColony.getM() + "; " +
                            "q = " + antColony.getQ() + "; " +
                            "alpha = " + antColony.getAlpha() + "; " +
                            "beta = " + antColony.getBeta() + "; " +
                            "lambda = " + antColony.getLambda() + ";"
            );
            mSum += antColony.getM();
            qSum += antColony.getQ();
            alphaSum += antColony.getAlpha();
            betaSum += antColony.getBeta();
            lambdaSum += antColony.getLambda();
        }

        System.out.println("平均: \t" +
                        "m = " + mSum / size + "; " +
                        "q = " + qSum / size + "; " +
                        "alpha = " + alphaSum / size + "; " +
                        "beta = " + betaSum / size + "; " +
                        "lambda = " + lambdaSum / size + "."
        );
    }
}
