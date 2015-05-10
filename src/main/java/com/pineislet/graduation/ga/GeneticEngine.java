package com.pineislet.graduation.ga;

import com.pineislet.graduation.util.Utils;

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
     *  变异概率
     * */
    public static final double P_MUTATE = 0.1;

    private boolean useGrayCode = true;

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
        this.currentPopulation = originalPopulation;
        this .populationSize = originalPopulation.size();
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
            int index = Utils.roulette(fitnessArray);
            Individual individual = population.get(index);
            resultPopulation.add((T) individual.createIndividual(individual.getGenome()));
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

            // 获取两者基因组
            List<boolean[]> genome1 = individual1.getGenome();
            List<boolean[]> genome2 = individual2.getGenome();

            List<boolean[]> crossGenome1 = new ArrayList<>();
            List<boolean[]> crossGenome2 = new ArrayList<>();
            assert genome1.size() == genome2.size();
            // 基因交叉
            for (int j = 0; j < genome1.size(); j++) {
                boolean[] gene1 = genome1.get(j);
                boolean[] gene2 = genome2.get(j);
                // 随机选取交叉点
                assert gene1.length == gene2.length;
                int geneLength = gene1.length;
                int index = (int) (Math.random() * geneLength);
                // 基因交叉互换
                boolean[] crossGene1 = new boolean[geneLength];
                boolean[] crossGene2 = new boolean[geneLength];
                for (int k = 0; k < geneLength; k++) {
                    if (k < index) {
                        crossGene1[k] = gene1[k];
                        crossGene2[k] = gene2[k];
                    }
                    else {
                        crossGene1[k] = gene2[k];
                        crossGene2[k] = gene1[k];
                    }
                }
                crossGenome1.add(crossGene1);
                crossGenome2.add(crossGene2);
            }

            resultPopulation.add((T) individual1.createIndividual(crossGenome1));
            resultPopulation.add((T) individual2.createIndividual(crossGenome2));
        }
        return resultPopulation;
    }

    /**
     *  变异
     * */
    @SuppressWarnings("unchecked")
    private List<T> mutate(List<T> currentPopulation) {
        return currentPopulation.stream().map(individual ->
                (T) individual.createIndividual(individual.getGenome().stream().map(gene -> {
                    boolean[] mutateGene = new boolean[gene.length];
                    if (gene.length > 0) {
                        mutateGene[0] = gene[0];
                        for (int i = 1; i < gene.length; i++) {
                            mutateGene[i] = Math.random() < P_MUTATE ? (!gene[i]) : gene[i];
                        }
                    }
                    return mutateGene;
                }).collect(Collectors.toList()))).collect(Collectors.toList());

//        return currentPopulation.stream().map(individual -> {
//            List<boolean[]> mutateGenome = individual.getGenome().stream().map(gene -> {
//                boolean[] mutateGene = new boolean[gene.length];
//                if (gene.length > 0) {
//                    mutateGene[0] = gene[0];
//                    for (int i = 1; i < gene.length; i++) {
//                        gene[i] = Math.random() < P_MUTATE ? (!gene[i]) : gene[i];
//                    }
//                }
//                return mutateGene;
//            }).collect(Collectors.toList());
//            return (T) individual.createIndividual(mutateGenome);
//        }).collect(Collectors.toList());
    }


    /**
     *  getter & setter
     * */
    public List<List<T>> getPopulationHistory() {
        return populationHistory;
    }

    public void setPopulationHistory(List<List<T>> populationHistory) {
        this.populationHistory = populationHistory;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public List<T> getCurrentPopulation() {
        return currentPopulation;
    }

    public void setCurrentPopulation(List<T> currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    public List<T> getOriginalPopulation() {
        return originalPopulation;
    }

    public void setOriginalPopulation(List<T> originalPopulation) {
        this.originalPopulation = originalPopulation;
    }
}
