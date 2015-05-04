package com.pineislet.graduation.ga;

import com.pineislet.graduation.util.Roulette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 15/5/4
 * @author Yasenia
 *
 * 遗传引擎
 */

public class GeneticEngine<T extends Individual> {

    /**
     *  原始种群
     * */
    private List<T> originalPopulation;

    /**
     *  当前种群
     * */
    private List<T> currentPopulation;

     /**
     *  种群进化历史
     * */
    private List<List<T>> populationHistory;

    /**
     *  种群规模
     * */
    private int populationSize;

    /**
     *  构造方法
     * */
    public GeneticEngine(List<T> originalPopulation) {
        this.originalPopulation = originalPopulation;
        populationHistory = new ArrayList<>();
        populationHistory.add(originalPopulation);
    }

    /**
     *  进化
     * */
    public void evolution() {
        /**
         *  选择
         * */
        List<T> selectedPopulation = select(currentPopulation);

        /**
         *  交叉
         * */
        List<T> crossoverPopulation = crossover(selectedPopulation);

        /**
         *  变异，记录当前种群
         * */
        currentPopulation = mutate(crossoverPopulation);

        /**
         *  记录种群历史
         * */
        populationHistory.add(currentPopulation);
    }

    /**
     *  选择
     * */
    @SuppressWarnings("unchecked")
    private List<T> select(List<T> population) {
        List<T> resultPopulation = new ArrayList<>();
        // 计算适应度
        double[] fitnessArray = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitnessArray[i] = population.get(i).calcFitness();
        }
        // 选择个体
        for (int i = 0; i < populationSize; i++) {
            int index = Roulette.roulette(fitnessArray);
            Individual individual = population.get(index);
            resultPopulation.add((T) individual.createIndividual(individual.getGene()));
        }
        return resultPopulation;
    }

    /**
     *  交叉
     * */
    @SuppressWarnings("unchecked")
    private List<T> crossover(List<T> population) {

        List<T> resultPopulation = new ArrayList<>();
        // 随机打乱
        Collections.shuffle(population);
        // 两两配对
        for (int i = 0; i < populationSize / 2; i++) {
            Individual individual1 = population.get(i);
            Individual individual2 = population.get(populationSize - 1 - i);

            resultPopulation.add((T) individual1);
            resultPopulation.add((T) individual2);
        }
        return resultPopulation;
    }

    /**
     *  变异
     * */
    private List<T> mutate(List<T> currentPopulation) {

        return currentPopulation;
    }

}
