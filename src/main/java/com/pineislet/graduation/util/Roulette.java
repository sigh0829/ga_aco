package com.pineislet.graduation.util;

import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 */

public class Roulette {
    public static int roulette(double[] weightArray) {
        return 0;
    }


    public static int roulette(double[] weightArray, boolean banArray[]) {
        assert weightArray.length == banArray.length;

        int index = 0;

        // 计算权和
        double weightSum = 0;
        for (int i = 0; i < weightArray.length; i++) {
            if (!banArray[i] && weightArray[i] > 0) {
                weightSum += weightArray[i];
            }
        }

        // 权和大于0，轮盘赌选择
        if (weightSum > 0) {
            // 获取一个0-1之间的额随机数
            double p = Math.random();
            double q = 0;
            for (int i = 0; i < weightArray.length; i++) {
                if (!banArray[i] && weightArray[i] > 0) {
                    p += weightArray[i] / weightSum;
                    if (p >= q) {
                        index = i;
                        break;
                    }
                }
            }
        }
        // 权和为0，等概率选取
        else {
            // 计算可选总数
            int count = weightArray.length;
            for (boolean isBan : banArray) {
                if (isBan) {
                    count--;
                }
            }
            // 等概率挑选
            int t = (int) (Math.random() * count);
            for (int i = 0; i < banArray.length; i++) {
                if (!banArray[i]) {
                    if (t > 0) {
                        t--;
                    }
                    else {
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }
}
