package com.pineislet.graduation.ga;

import java.util.List;

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
     *  获取基因组
     * */
    List<boolean[]> getGenome();

    /**
     *  通过基因组创建个体
     * */
    Individual createIndividual(List<boolean[]> genome);
}
