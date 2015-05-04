package com.pineislet.graduation.ga;

/**
 * Created on 15/5/4
 * @author Yasenia
 *
 * 个体
 */

public interface Individual {

    /**
     *  计算适应度
     * */
    double calcFitness();

    /**
     *  获取基因
     * */
    boolean[] getGene();

    /**
     *  通过基因创建个体
     * */
    Individual createIndividual(boolean[] gene);
}
