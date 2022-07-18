package site.halenspace.pocketcloud.threadpool.binarytree.build;

import site.halenspace.pocketcloud.threadpool.structure.TreeNode;

/**
 * @author Halen Leo · 2022/6/13
 */
public class BinaryTreeBuild889 {
    public static void main(String[] args) {
        int[] preOrder = new int[]{1, 2, 4, 5, 3, 6, 7};
        int[] inOrder = new int[]{4, 5, 2, 6, 7, 3, 1};
        new BinaryTreeBuild889().constructFromPrePost(preOrder, inOrder);
    }

    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        return build(preorder, 0, preorder.length - 1, postorder, 0, postorder.length - 1);
    }

    public TreeNode build(int[] preOrder, int preLeft, int preRight, int[] postOrder, int postLeft, int postRight) {
        if (preLeft > preRight) return null;

        // 前序遍历数组第一个节点或后序遍历数组最后一个节点是roote节点
        int rootVal = preOrder[preLeft];

        if (preLeft == preRight) {
            return new TreeNode(rootVal);
        }

        // 前序遍历数组第二个节点为左子树根节点
        int preLeftChildTreeRootVal = preOrder[preLeft + 1];

        // 寻找左子树根节点在后续遍历数组中的位置
        int postLeftChildTreeRootIdx = 0;
        for (int i = postLeft; i <= postRight; i++) {
            if (postOrder[i] == preLeftChildTreeRootVal) {
                postLeftChildTreeRootIdx = i;
            }
        }

        // 通过后续遍历数组中左子树位置计算左子树节点个数
        int leftTreeNodeSum = postLeftChildTreeRootIdx - postLeft + 1;

        // 构建完整树结构
        TreeNode root = new TreeNode(rootVal);
        root.left = build(preOrder, preLeft + 1, preLeft + leftTreeNodeSum, postOrder, postLeft, postLeftChildTreeRootIdx);
        root.right = build(preOrder, preLeft + leftTreeNodeSum + 1, preRight, postOrder, postLeftChildTreeRootIdx + 1, postRight);

        return root;
    }
}
