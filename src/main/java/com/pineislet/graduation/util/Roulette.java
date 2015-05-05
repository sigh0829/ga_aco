package com.pineislet.graduation.util;

import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 */

public class Roulette {
    /**
     *  轮盘赌选择
     *  @param weightArray 权重数组，若有负权或权和为0，则等概率随选择
     *  @param banArray 禁忌表，指定不参与轮盘选择的数据
     *
     *  @return 轮盘选择结果
     * */
    public static int roulette(double[] weightArray, boolean[] banArray) {
        int n = Math.min(weightArray.length, banArray.length);
        int index = 0;

        // 计算有效权和，并判断是否有负权
        double sum = 0;
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            if (!banArray[i]) {
                if (weightArray[i] < 0) {
                    flag = false; break;
                }
                sum += weightArray[i];
            }
        }

        // 若无负权且权和不为0，按权重轮盘选择
        if (flag && sum != 0) {
            double p = Math.random();
            double q = 0;
            for (int i = 0; i < n; i++) {
                if (!banArray[i]) {
                    q += weightArray[i] / sum;
                    if (q >= p) {
                        index = i; break;
                    }
                }
            }
        }
        // 若有负权或权和未0，等概率选择
        else {
            int count = 0;
            for (boolean isBan : banArray) {
                if (isBan) {
                    count++;
                }
            }
            int t = (int) (Math.random() * count);
            for (int i = 0; i < n; i++) {
                if (!banArray[i]) {
                    t--;
                    if (t == 0) {
                        index = i; break;
                    }
                }
            }
        }
        return index;
    }



    /**
     *  轮盘赌选择
     *  @param weightArray 权重数组，若有负权或权和为0，则等概率随选择
     *
     *  @return 轮盘选择结果
     * */
    public static int roulette(double[] weightArray) {
        boolean[] banArray = new boolean[weightArray.length];
        for (int i = 0; i < banArray.length; i++) {
            banArray[i] = false;
        }
        return roulette(weightArray, banArray);
    }
}
