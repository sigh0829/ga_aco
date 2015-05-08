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

            // 获取两者基因
//            boolean[] gene1 = useGrayCode ? encodeGray(individual1.getGene()) : individual1.getGene();
//            boolean[] gene2 = useGrayCode ? encodeGray(individual2.getGene()) : individual2.getGene();
            boolean[] gene1 = individual1.getGene();
            boolean[] gene2 = individual2.getGene();

            // 随机选取交叉点
            assert gene1.length == gene2.length;
            int geneLength = gene1.length;
            int index = (int) (Math.random() * geneLength);

            // 基因交叉互换
            boolean[] crossGene1 = new boolean[geneLength];
            boolean[] crossGene2 = new boolean[geneLength];
            for (int j = 0; j < geneLength; j++) {
                if (j < index) {
                    crossGene1[j] = gene1[j];
                    crossGene2[j] = gene2[j];
                }
                else {
                    crossGene1[j] = gene2[j];
                    crossGene2[j] = gene1[j];
                }
            }

//            resultPopulation.add((T) individual1.createIndividual(useGrayCode ? decodeGray(crossGene1) : crossGene1));
//            resultPopulation.add((T) individual2.createIndividual(useGrayCode ? decodeGray(crossGene2) : crossGene2));
            resultPopulation.add((T) individual1.createIndividual(crossGene1));
            resultPopulation.add((T) individual2.createIndividual(crossGene2));
        }
        return resultPopulation;
    }

    /**
     *  变异
     * */
    @SuppressWarnings("unchecked")
    private List<T> mutate(List<T> currentPopulation) {
        return currentPopulation.stream().map(individual -> {
            // 获取基因
//            boolean[] gene = useGrayCode ? encodeGray(individual.getGene()) : individual.getGene();
            boolean[] gene = individual.getGene();
            boolean[] mutateGene = new boolean[gene.length];
            // 基因突变
            for (int i = 0; i < gene.length; i++) {
                mutateGene[i] = Math.random() < P_MUTATE ? (!gene[i]) : gene[i];
            }
//            return (T) individual.createIndividual(useGrayCode ? decodeGray(mutateGene) : mutateGene);
            return (T) individual.createIndividual(mutateGene);
        }).collect(Collectors.toList());
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
