package site.halenspace.pocketcloud.threadpool.dp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author Zg.Li · 2022/3/8
 */
public class DynamicProgramingBase {

    public static int fibonacci(int[] arr, int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        if (arr[n] != 0) {
            return arr[n];
        }
        int sum = fibonacci(arr, n - 1) + fibonacci(arr, n - 2);
        arr[n] = sum;
        return sum;
    }

    public static int fibonacciSolutionDP(int n) {

        // base case dp[0] = 0, dp[1] = 1
        int[] dp = new int[n + 1];
        dp[1] = 1;

        // 状态转移 f(n) = f(n - 1) + f(n - 2)
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    public static int coinChangeOld(int[] coins, int amount) {

        int n = coins.length + 1;
        int w = amount + 1;
        // base case dp[0][0...n] = false, dp[0...n][0] = true
        boolean[][] dp = new boolean[n][w];

        for (int i = 0; i < coins.length; i++) {
            dp[i][0] = true;
        }

        // 状态转移 dp[i - 1] || dp[amount - coins[i]]
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= w; j++) {
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - coins[i - 1]];
                    if (dp[i][j]) {
                        // 最少所需硬币数量
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public static int coinChange(int[] coins, int amount) {

        int w = amount + 1;
        // dp数组的含义为 金额为i时, 凑够金额i使用的硬币最少个数
        int[] dp = new int[w];
//        boolean[][] dp = new boolean[n][w];

        // 初始化最大值为amount + 1，因为使用最小面值1元的硬币，最多使用amount个可以凑够给定的金额，所以amount + 1可以代表最大值
        for (int i = 0; i < w; i++) {
            dp[i] = w;
        }

        // base case 金额数为0最少使用0个硬币可以凑出
        dp[0] = 0;

        for (int i = 0; i < w; i++) {
            for (int coin : coins) {
                if (i - coin < 0) {
                    // 放不下
//                    dp[i] = dp[i - 1];
                    continue;
                }
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }

        return dp[amount] < w ? dp[amount] : -1;
    }

    public static int coinChangeTemplate(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        // 数组大小为 amount + 1，初始值也为 amount + 1
        Arrays.fill(dp, amount + 1);

        // base case
        dp[0] = 0;
        // 外层 for 循环在遍历所有状态的所有取值
        for (int i = 0; i < dp.length; i++) {
            // 内层 for 循环在求所有选择的最小值
            for (int coin : coins) {
                // 子问题无解，跳过
                if (i - coin < 0) {
                    continue;
                }
                dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
            }
        }
        return (dp[amount] == amount + 1) ? -1 : dp[amount];
    }

    public int lengthOfLIS(int[] nums) {
//        使用数组 cell 保存每步子问题的最优解。
//        cell[i] 代表含第 i 个元素的最长上升子序列的长度。
//        求解 cell[i] 时，向前遍历找出比 i 元素小的元素 j，令 cell[i] 为 max（cell[i],cell[j]+1)。
        int[] dp = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            // 循环寻找nums[0] ~ nums[i - 1]间小于nums[i]的元素所对应的子序列长度，选最大的长度
            for (int j = 0; j < i; j++) {
                if (nums[j] >= nums[i]) {
                    continue;
                }
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }

        int maxItem = 1;
        // 循环获取最大的上升子序列长度
        for (int i = 0; i < dp.length; i++) {
            maxItem = Math.max(maxItem, dp[i]);
        }


        return maxItem;
    }

    public static void main(String[] args) {
        int n = 6;
        int[] arr = new int[n + 1];
//        System.out.println(fibonacci(arr, n));
//        System.out.println(fibonacciSolutionDP(n));

        int[] coins = new int[]{1, 2, 5};
        System.out.println(coinChange(coins, 11));
//        System.out.println(coinChangeTemplate(coins, 7));
    }


}
