package site.halenspace.pocketcloud.threadpool.listnode.reverse;

import site.halenspace.pocketcloud.threadpool.listnode.ListNode;

/**
 * 链表翻转
 * @author Halen Leo · 2022/3/22
 */
public class ListNodeReverse {

    private static ListNode successor;

    /**
     * 反转前n个节点
     * @param head
     * @param n
     * @return
     */
    public ListNode reverseN(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        if (n == 1) {
            successor = head.next;
            return head;
        }

        ListNode lastNode = reverseN(head.next, n - 1);

        // 反转指针
        head.next.next = head;
        // 将反转的节点与后边节点连接起来
        head.next = successor;
        return lastNode;
    }

    /**
     *
     * @param head
     * @param left
     * @param right
     * @return
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {

        return null;
    }

    /**
     * 迭代反转整个链表
     * @param head
     * @return
     */
    public ListNode iteReverseAll(ListNode head) {
        ListNode pre, cur, nxt;
        pre = null; cur = head;
        while (cur != null) {
            nxt = cur.next;
            // 逐个结点反转
            cur.next = pre;
            // 更新指针位置
            pre = cur;
            cur = nxt;
        }
        // 返回反转后的头结点
        return pre;
    }

    /**
     * 迭代反转区间[nodeBegin, nodeEnd)之间的元素
     * @param nodeBegin
     * @param nodeEnd
     * @return
     */
    public ListNode iteReverseInterval(ListNode nodeBegin, ListNode nodeEnd) {
        ListNode pre, cur, nxt;
        pre = null; cur = nodeBegin;
        while (cur != nodeEnd) {
            nxt = cur.next;
            // 逐个结点反转
            cur.next = pre;
            // 更新指针位置
            pre = cur;
            cur = nxt;
        }
        // 返回反转后的头结点
        return pre;
    }

    /**
     * 25. K 个一组翻转链表
     * 解题思路：拆解问题，k个元素迭代翻转一次，然后每 k 个元素递归一次，直到链表剩余元素不足k个则直接返回这些元素
     * 时间复杂度：O(n)，其中 n 为链表的长度
     * @param head 链表头节点
     * @param k 每 k 个节点一组进行翻转
     * @return
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) return null;
        // 区间[a, b)包含k个待反转元素
        ListNode a = head, b = head;
        for (int i = 1; i <= k; i++) {
            if (b == null) return head;
            b = b.next;
        }
        // 反转前k个元素
        ListNode newHead = iteReverseInterval(a, b);
        a.next = reverseKGroup(b, k);
        return newHead;
    }

    /**
     * 234. 回文链表
     * 解法1思路：链表后序遍历（也就是递归到底），借助另一个头节点指针，回溯过程推动头节点指针向后移动，以此比较回溯过程中节点和头节点指针元素是否相等
     * 时间复杂度：O(n)，其中 n 指的是链表的元素个数。
     *  第一步： 遍历链表并将值复制到数组中，O(n)。
     *  第二步：双指针判断是否为回文，执行了 O(n/2) 次的判断，即 O(n)。
     *  总的时间复杂度：O(2n) = O(n)。
     * 空间复杂度：O(n), 其中 n 指的是链表的元素个数，使用了额外的公共变量引用链表
     * @param head
     * @return
     */
    ListNode left;
    public boolean isPalindrome(ListNode head) {
        left = head;
        return traverse(head);
    }

    private boolean traverse(ListNode right) {
        if (right == null) return true;
        boolean result = traverse(right.next);
        // 后序遍历
        result = result && (right.val == left.val);
        left = left.next;
        return result;
    }
}
