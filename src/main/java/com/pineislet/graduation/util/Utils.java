package com.pineislet.graduation.util;

/**
 * Created on 15/5/4
 * @author Yasenia
 */

public class Utils {
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

    /**
     *  整数转二进制数组
     *  @param n 输入的十进制整数
     *  @param length 返回二进制数组的位数（不足位补0，超出截取低位）
     *
     *  @return 返回二进制数组
     * */
    public static boolean[] numberToBinArray(long n, int length) {
        boolean[] binArray = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (n % 2 != 0) {
                binArray[length - 1 - i] = true;
            }
            n >>= 1;
        }
        return binArray;
    }

    /**
     *  二进制数组转整数
     *  @param binArray 输入的二进制数组
     *
     *  @return 返回十进制整数
     * */
    public static long binArrayToNumber(boolean[] binArray) {
        long result = 0;
        for (boolean b : binArray) {
            result <<= 1;
            result += b ? 1 : 0;
        }
        return result;
    }

    /**
     *  连接数组
     * */
    public static boolean[] concat(boolean[]... arrays) {
        int length = 0;
        for (boolean[] array : arrays) {
            length += array.length;
        }
        boolean[] result = new boolean[length];
        int startIndex = 0;
        for (boolean[] array : arrays) {
            System.arraycopy(array, 0, result, startIndex, array.length);
            startIndex += array.length;
        }
        return result;
    }

    /**
     *  格雷码编码
     * */
    public static boolean[] encodeGray(boolean[] gene) {
        boolean[] encodeGene = new boolean[gene.length];
        if (gene.length > 0) {
            encodeGene[0] = gene[0];
            for (int i = 1; i < gene.length; i++) {
                encodeGene[i] = gene[i - 1] ^ gene[i];
            }
        }
        return encodeGene;
    }

    /**
     *  格雷码解码
     * */
    public static boolean[] decodeGray(boolean[] gene) {
        boolean[] decodeGene = new boolean[gene.length];
        if (gene.length > 0) {
            decodeGene[0] = gene[0];
            for (int i = 1; i < gene.length; i++) {
                decodeGene[i] = decodeGene[i - 1] ^ gene[i];
            }
        }
        return decodeGene;
    }

    /**
     *  对给定数字进行二进制编码
     * */
    public static boolean[] encodeBin(double number, double min, double max, int length) {
        boolean[] binArray = new boolean[length];
        int n = (int) ((number - min) / ((max - min) / ((int) Math.pow(2, length))));
        for (int i = 0; i < length; i++) {
            if (n % 2 != 0) {
                binArray[length - 1 - i] = true;
            }
            n >>= 1;
        }
        return binArray;
    }

    /**
     *  对给定二进制编码进行解码
     * */
    public static double decodeBin(boolean[] binArray, double min, double max, int length) {
        int n = 0;
        for (boolean b : binArray) {
            n <<= 1;
            n += b ? 1 : 0;
        }
        return min + ((max - min) / ((int) Math.pow(2, length))) * n;
    }
}
