package com.pineislet.graduation.aco;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/5/4
 * @author Yasenia
 */

public class TSP {

    public static final TSP DEFAULT_TSP;

    static {
        TSP temp = null;
        try {
            temp = loadTSPByInputStream(TSP.class.getClassLoader().getResourceAsStream("ALL-tsp/eil51.tsp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DEFAULT_TSP = temp;
    }

    /**
     *  TSP规格，城市数量
     * */
    public int n;

    /**
     *  城市坐标
     * */
    public double[] xArray;
    public double[] yArray;

    /**
     *  城市距离矩阵
     * */
    public double[][] distanceMatrix;

    /**
     *  平均距离
     * */
    public double averageDistance;

    /**
     *  构造方法
     * */
    public TSP(double[] xArray, double[] yArray) {
        this.xArray = xArray;
        this.yArray = yArray;
        this.n = xArray.length;
        this.distanceMatrix = new double[n][n];
        double distanceSum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = Math.sqrt((xArray[i] - xArray[j]) * (xArray[i] - xArray[j]) +
                            (yArray[i] - yArray[j]) * (yArray[i] - yArray[j]));
                    distanceSum += distanceMatrix[i][j];
                }
            }
        }
        this.averageDistance = distanceSum / (n * (n - 1));
    }


    /**
     *  计算指定路径的行程
     * */
    public double calcDistance(int[] path) {
        assert path.length == n;
        double distance = 0;
        for (int i = 0; i < n; i++) {
            distance += distanceMatrix[path[i]][path[(i + 1) % n]];
        }
        return distance;
    }

    /**
     *  加载文件数据创建 TSP 对象
     * */
    public static TSP loadTSPByInputStream(InputStream inputStream) throws IOException {
        // 创建IO流
        BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));

        List<double[]> pointList = new ArrayList<double[]>();
        boolean flag = false;
        String s;
        // 读取一行文本
        while (null != (s = bfr.readLine())) {
            // 遇见文末标识，文本结束，跳出循环
            if (s.trim().toUpperCase().equals("EOF")) {
                break;
            }
            // 记录数据
            if (flag) {
                String[] pointData = s.split("[ ]+");
                try {
                    double[] point = new double[2];
                    point[0] = Double.parseDouble(pointData[1]);
                    point[1] = Double.parseDouble(pointData[2]);
                    pointList.add(point);
                }
                catch (NumberFormatException e) {
                    throw new NumberFormatException("解析错误，请检查文本格式");
                }
            }
            // 遇见数据标志，开始读取数据
            if (s.trim().toUpperCase().equals("NODE_COORD_SECTION") || s.trim().toUpperCase().equals("DISPLAY_DATA_SECTION")) {
                flag = true;
            }
        }

        int dimension = pointList.size();
        double[] xArray = new double[dimension];
        double[] yArray = new double[dimension];
        for (int i = 0; i < pointList.size(); i++) {
            xArray[i] = pointList.get(i)[0];
            yArray[i] = pointList.get(i)[1];
        }

        return new TSP(xArray, yArray);
    }
}
