package site.halenspace.pocketcloud.threadpool.binarytree.lca;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * Leecode.236 二叉树的最近公共祖先
 *
 * @author Zg.Li · 2022/6/17
 */
public class LCA236 {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        return find(root, p.val, q.val);
    }

    public TreeNode find(TreeNode root, int pVal, int qVal) {
        if (root == null) return null;

        if (root.val == pVal || root.val == qVal) {
            return root;
        }

        // 查找左子树
        TreeNode left = find(root.left, pVal, qVal);
        // 查找右子树
        TreeNode right = find(root.right, pVal, qVal);

        // 左右子树均找到证明当前root节点就是LCA节点
        if (left != null && right != null) {
            return root;
        }

        // 只找到一个的情况证明被找到的节点就是LCA节点
        return (left != null) ? left : right;
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        TreeNode p = new TreeNode(5);
        TreeNode q = new TreeNode(4);
        new LCA236().lowestCommonAncestor(root, p, q);
    }
}
