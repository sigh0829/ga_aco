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
            System.out.print(i + "\t");
            showAcPopulation(geneticEngine.getCurrentPopulation());
            geneticEngine.evolution();
        }
    }

    public static void showAcPopulation(List<AntColony> acPopulation) {
        int size = acPopulation.size();
        double mSum = 0;
        double qSum = 0;
        double alphaSum = 0;
        double betaSum = 0;
        double lambdaSum = 0;
        for (AntColony antColony : acPopulation) {
            mSum += antColony.getM();
            qSum += antColony.getQ();
            alphaSum += antColony.getAlpha();
            betaSum += antColony.getBeta();
            lambdaSum += antColony.getLambda();
        }

        double mAvg = mSum / size;
        double qAvg = qSum / size;
        double alphaAvg = alphaSum / size;
        double betaAvg = betaSum / size;
        double lambdaAvg = lambdaSum / size;

        double mSqSum = 0;
        double qSqSum = 0;
        double alphaSqSum = 0;
        double betaSqSum = 0;
        double lambdaSqSum = 0;
        for (AntColony antColony : acPopulation) {
            mSqSum += (mAvg - antColony.getM()) * (mAvg - antColony.getM());
            qSqSum += (qAvg - antColony.getQ()) * (qAvg - antColony.getQ());
            alphaSqSum += (alphaAvg - antColony.getAlpha()) * (alphaAvg - antColony.getAlpha());
            betaSqSum += (betaAvg - antColony.getBeta()) * (betaAvg - antColony.getBeta());
            lambdaSqSum += (lambdaAvg - antColony.getLambda()) * (lambdaAvg - antColony.getLambda());
        }

        double mSd = Math.sqrt(mSqSum / size);
        double qSd = Math.sqrt(qSqSum / size);
        double alphaSd = Math.sqrt(alphaSqSum / size);
        double betaSd = Math.sqrt(betaSqSum / size);
        double lambdaSd = Math.sqrt(lambdaSqSum / size);

//        System.out.println("平均: \t" +
//                        "m = " + mSum / size + "; " +
//                        "q = " + qSum / size + "; " +
//                        "alpha = " + alphaSum / size + "; " +
//                        "beta = " + betaSum / size + "; " +
//                        "lambda = " + lambdaSum / size + "."
//        );

        System.out.println(mAvg + "\t" + mSd + "\t" +
                        qAvg + "\t" + qSd + "\t" +
                        alphaAvg + "\t" + alphaSd + "\t" +
                        betaAvg + "\t" + betaSd + "\t" +
                        lambdaAvg + "\t" + lambdaSd + "\t"
        );
    }
}
