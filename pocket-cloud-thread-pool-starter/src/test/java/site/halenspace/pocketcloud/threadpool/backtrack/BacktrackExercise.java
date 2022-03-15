package site.halenspace.pocketcloud.threadpool.backtrack;

import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author Zg.Li · 2022/3/11
 */
public class BacktrackExercise {

    private static final List<List<Integer>> res = new LinkedList<>();

    /* 主函数，输入一组不重复的数字，返回它们的全排列 */
    public static List<List<Integer>> permute(int[] nums) {
        // 记录「路径」
        LinkedList<Integer> track = new LinkedList<>();
        // 「路径」中的元素会被标记为 true，避免重复使用
        boolean[] used = new boolean[nums.length];

        backtrack(nums, track, used);
        return res;
    }

    // 路径：记录在 track 中
    // 选择列表：nums 中不存在于 track 的那些元素（used[i] 为 false）
    // 结束条件：nums 中的元素全都在 track 中出现
    private static void backtrack(int[] nums, LinkedList<Integer> track, boolean[] used) {
        // 触发结束条件
        if (track.size() == nums.length) {
            res.add(new LinkedList<>(track));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            // 排除不合法的选择
            if (used[i]) {
                // nums[i] 已经在 track 中，跳过
                continue;
            }
            // 做选择
            track.add(nums[i]);
            used[i] = true;
            // 进入下一层决策树
            backtrack(nums, track, used);
            // 取消选择
            track.removeLast();
            used[i] = false;
        }
    }

    /*public vector<vector<string>> solveNQueens(int n) {
        // '.' 表示空，'Q' 表示皇后，初始化空棋盘。
        vector<string> board(n, string(n, '.'));
        backtrack(board, 0);
        return res;
    }

    void backtrack(vector<string>& board, int row) {
        // 触发结束条件
        if (row == board.size()) {
            res.push_back(board);
            return;
        }

        int n = board[row].size();
        for (int col = 0; col < n; col++) {
            // 排除不合法选择
            if (!isValid(board, row, col)) {
                continue;
            }
            // 做选择
            board[row][col] = 'Q';
            // 进入下一行决策
            backtrack(board, row + 1);
            // 撤销选择
            board[row][col] = '.';
        }
    }*/

    List<List<String>> result = Lists.newArrayList();
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        // 初始化棋盘数组p
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }

        backtrack(board, 0);
        return result;
    }

    public void backtrack(char[][] board, int row) {
        // 递归结束条件
        if (board.length == row) {
            // 拷贝所有行到结果集中
            for (int i = 0; i < board.length; i++) {
                result.add(Collections.singletonList(String.copyValueOf(board[i])));
            }
        }

        for (int i = 0; i < board.length; i++) {
            // 判断是否合法
            if (!isValid(board, row, i)) {
                continue;
            }
            board[row][i] = 'Q';
            // 进入下一层决策树
            backtrack(board, row + 1);
            // back
            board[row][i] = '.';
        }
    }

    private boolean isValid(char[][] board, int row, int col) {
        // board[row][col] 正上方、左上方和右上方如果为皇后则不合法
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') {
                return false;
            }
        }
        for (int i = row, j = col; i > 0 && j > 0;) {
            if (board[--i][--j] == 'Q') {
                return false;
            }
        }
        for (int i = row, j = col; i > 0 && j < board.length - 1;) {
            if (board[--i][++j] == 'Q') {
                return false;
            }
        }

        return true;
    }



    public static void main(String[] args) {
//        int[] nums = new int[]{1, 2, 3};
//        List<List<Integer>> resultList = permute(nums);

        List<List<String>> lists = new BacktrackExercise().solveNQueens(4);
        System.out.println();
    }


    class Solution1 {
        int len;
        char[][] p;
        // 下面的数组含义分别为：列、对角线、反对角线
        boolean col[], dg[], bdg[];

        List<List<String>> ans = new ArrayList<>();

        public List<List<String>> solveNQueens(int n) {
            len = n;
            // 开2n倍空间防止数组越界
            p = new char[n][n];
            col = new boolean[2 * n];
            dg = new boolean[2 * n];
            bdg = new boolean[2 * n];
            // 初始化棋盘数组p
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    p[i][j] = '.';

            dfs(0);

            return ans;

        }

        public void dfs(int u) {
            // 递归出口条件
            if (u == len) {
                List<String> tmp = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    tmp.add(String.copyValueOf(p[i]));
                }
                ans.add(tmp);
                return;

            }

            // 递归体
            // 参考全排列
            // 判断当前行(u)中每一列是否满足条件
            for (int i = 0; i < len; i++) {
                if (!col[i] && !dg[len - u + i] && !bdg[u + i]) {
                    p[u][i] = 'Q';
                    col[i] = dg[len - u + i] = bdg[u + i] = true;
                    dfs(u + 1);
                    col[i] = dg[len - u + i] = bdg[u + i] = false;
                    p[u][i] = '.';
                }
            }
        }
    }
}
