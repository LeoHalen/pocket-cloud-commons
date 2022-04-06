package site.halenspace.pocketcloud.threadpool.array.prefixsum;

import java.util.HashMap;

/**
 * @author Halen Leo · 2022/3/31
 */
public class NumMatrix {

    // preNum[i][j] 记录着matrix子矩阵 [0, 0, i - 1, j - 1]的元素和
    private int[][] preSum;

    public NumMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        if (n == 0) return;
        // 初始化
        preSum = new int[m + 1][n + 1];
        // 计算前缀和矩阵
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= m; j++) {
                preSum[i][j] = preSum[i - 1][j] + preSum[i][j - 1] + matrix[i - 1][j - 1] - preSum[i - 1][j - 1];
            }
        }
    }

    /**
     * 304. 二维区域和检索 - 矩阵不可变
     * 解题思路：在一维数组前缀和的思路上稍作扩展
     * 时间复杂度：O(1), 空间换时间
     * @param x1 矩阵左上角元素行位置
     * @param y1 矩阵左上角元素列位置
     * @param x2 矩阵右上角元素行位置
     * @param y2 矩阵右上角元素列位置
     * @return
     */
    public int sumRegion(int x1, int y1, int x2, int y2) {
        // 目标矩阵由四个相邻的矩阵运算得出
        return preSum[x2 + 1][y2 + 1] - preSum[x1][y2 + 1] - preSum[x2 + 1][y1] + preSum[x1][y1];
    }


    int subarraySumTemplate(int[] nums, int k) {
        int n = nums.length;
        // 构造前缀和
        int[] preSum = new int[n + 1];
        preSum[0] = 0;
        for (int i = 0; i < n; i++)
            preSum[i + 1] = preSum[i] + nums[i];

        int res = 0;
        // 穷举所有子数组
        for (int i = 1; i <= n; i++)
            for (int j = 0; j < i; j++)
                // 子数组 nums[j..i-1] 的元素和
                if (preSum[i] - preSum[j] == k)
                    res++;

        return res;
    }

    int subarraySumImprove(int[] nums, int k) {
        int n = nums.length;
        // map：前缀和 -> 该前缀和出现的次数
        HashMap<Integer, Integer>
                preSum = new HashMap<>();
        // base case
        preSum.put(0, 1);

        int res = 0, sum0_i = 0;
        for (int i = 0; i < n; i++) {
            sum0_i += nums[i];
            // 这是我们想找的前缀和 nums[0..j]
            int sum0_j = sum0_i - k;
            // 如果前面有这个前缀和，则直接更新答案
            if (preSum.containsKey(sum0_j))
                res += preSum.get(sum0_j);
            // 把前缀和 nums[0..i] 加入并记录出现次数
            preSum.put(sum0_i,
                    preSum.getOrDefault(sum0_i, 0) + 1);
        }
        return res;
    }

    public int subarraySum(int[] nums, int k) {

        return 1;
    }
}
