package site.halenspace.pocketcloud.threadpool.bfs;

import java.util.*;

/**
 * @Author Zg.Li · 2022/3/15
 */
public class BreadthFirstSearch {

    /**
     * Leetcode 111. 二叉树最小深度
     * @param root
     * @return
     */
    public int minDepth(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        int depth = 1;
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = q.poll();
                if (treeNode.left == null && treeNode.right == null) {
                    return depth;
                }
                if (treeNode.left != null) {
                    q.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    q.offer(treeNode.right);
                }
            }
            depth++;
        }

        return depth;
    }

    /**
     * Leetcode 752. 打开转盘锁
     * @param deadendArr
     * @param target
     * @return
     */
    public int openLock(String[] deadendArr, String target) {
        Set<String> visitedSet = new HashSet<>(Arrays.asList(deadendArr));
        Queue<String> q = new LinkedList<>();
        q.offer("0000");
        int depth = 0;

        while (!q.isEmpty()) {
            int i = 0, size = q.size();
            while (i++ < size) {
                String pwdStr = q.poll();

                // 跳过deadends 和拨动过的
                if (visitedSet.contains(pwdStr)) {
                    continue;
                }

                assert pwdStr != null;
                // 找到返回depth
                if (pwdStr.equals(target)) {
                    return depth;
                }
                // 查找过的存起来
                visitedSet.add(pwdStr);

                for (int j = 0; j < 4; j++) {
                    // 向上拨动一位
                    String plusPwdStr = plusOne(pwdStr, j);
                    q.offer(plusPwdStr);

                    // 向下拨动一位
                    String minusPwdStr = minusOne(pwdStr, j);
                    q.offer(minusPwdStr);
                }
            }
            depth++;
        }

        return -1;
    }

    private String plusOne(String pwdStr, int j) {
        char[] pwdArr = pwdStr.toCharArray();
        if (pwdArr[j] == '9') {
            pwdArr[j] = '0';
        } else {
            pwdArr[j]++;
        }
        return String.copyValueOf(pwdArr);
    }

    private String minusOne(String pwdStr, int j) {
        char[] pwdArr = pwdStr.toCharArray();
        if (pwdArr[j] == '0') {
            pwdArr[j] = '9';
        } else {
            pwdArr[j]--;
        }
        return String.copyValueOf(pwdArr);
    }


    public static void main(String[] args) {
//        String[] deadends = {"0201","0101","0102","1212","2002"};
        String[] deadends = {"8888"};
        System.out.println("resut: " + new BreadthFirstSearch().openLock(deadends, "0009"));
    }


    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
