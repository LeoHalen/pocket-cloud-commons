package site.halenspace.pocketcloud.threadpool.binarytree.lca;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * Leetcode.1644 二叉树的最近公共祖先 II
 * 思路: 递归后序位置判断是否找到并标记两个节点是否全部找到
 *
 * @author Zg.Li · 2022/6/17
 */
public class LCA1644 {

    /* 记录p节点是否找到 */
    private boolean foundP = false;
    /* 记录q节点是否找到 */
    private boolean foundQ = false;

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode res = find(root, p.val, q.val);
        if (foundP && foundQ) {
            // p 和 q 都存在二叉树中，才有公共祖先
            return res;
        }
        return null;
    }

    public TreeNode find(TreeNode root, int pVal, int qVal) {
        if (root == null) return null;

        TreeNode left = find(root.left, pVal, qVal);
        TreeNode right = find(root.right, pVal, qVal);

        // 后序位置判断是为了检索全部的节点
        if (left != null && right != null) {
            // 此时找到最近公共节点
            return root;
        }

        if (root.val == pVal || root.val == qVal) {
            if (root.val == pVal) foundP = true;
            if (root.val == qVal) foundQ = true;
            // 找到某个节点
            return root;
        }

        return left != null ? left : right;
    }
}
