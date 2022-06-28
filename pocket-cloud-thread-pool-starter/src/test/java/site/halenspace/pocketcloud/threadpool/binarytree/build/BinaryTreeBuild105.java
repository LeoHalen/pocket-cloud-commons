package site.halenspace.pocketcloud.threadpool.binarytree.build;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * @author Halen Leo · 2022/6/13
 */
public class BinaryTreeBuild105 {

    public static void main(String[] args) {

        int[] preOrderNums = new int[]{3, 9, 20, 15, 7};
        int[] inOrderNums = new int[]{9, 3, 15, 20, 7};
        new BinaryTreeBuild105().buildTree(preOrderNums, inOrderNums);
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 根据函数定义，用 preorder 和 inorder 构造二叉树
        return build(preorder, 0, preorder.length - 1,
                inorder, 0, inorder.length - 1);
    }


    public TreeNode build(int[] preOrderNums, int preLow, int preHigh, int[] inOrderNums, int inLow, int inHigh) {
        if (preLow > preHigh) return null;
        // 前序遍历数组的第一个元素就是root节点值
        int rootVal = preOrderNums[preLow];
        // 寻找root节点在中序遍历数组中的位置
        int inRootIdx = 0;
        for (int i = inLow; i <= inHigh; i++) {
            if (inOrderNums[i] == rootVal) {
                inRootIdx = i;
            }
        }

        // 通过中序遍历数组根节点位置计算出左子树的节点个数
        int leftTreeNodeSum = inRootIdx - inLow;

        // 构造根节点
        TreeNode root = new TreeNode(rootVal);
        root.left = build(preOrderNums, preLow + 1, preLow + leftTreeNodeSum, inOrderNums, inLow, inRootIdx - 1);
        root.right = build(preOrderNums, preLow + leftTreeNodeSum + 1, preHigh, inOrderNums, inRootIdx + 1, inHigh);

        return root;
    }
}
