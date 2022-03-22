package site.halenspace.pocketcloud.threadpool.listnode.doublepointer;

import site.halenspace.pocketcloud.threadpool.listnode.ListNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Halen Leo · 2022/3/21
 */
public class DoublePointer {

    /**
     * 21. 合并两个有序链表
     * @param list1
     * @param list2
     * @return
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        ListNode r = new ListNode(-1), p = r;
        ListNode p1 = list1, p2 = list2;

        while (p1 != null && p2 != null) {
            if (p1.val >= p2.val) {
                p.next = p2;
                p2 = p2.next;
            } else {
                p.next = p1;
                p1 = p1.next;
            }
            p = p.next;
        }

        if (p1 != null) {
            p.next = p1;
        }
        if (p2 != null) {
            p.next = p2;
        }

        return r.next;
    }

    /**
     * 23. 合并K个升序链表
     * 解题思路:
     *  利用优先级队列的小顶堆每次循环求出最小头节点的那个链表，每次将最小头节点取出再剩余链表放入队列，直至队列为空
     * 时间复杂度:
     *  add优先级队列时间复杂度为O(Logk), k为链表的条数
     *  while循环的最坏时间复杂度为O(n), n为所有链表元素个数总和
     *  所以整个算法的最坏时间复杂度为O(nLogk)
     * @param lists n 个链表
     * @return n个链表合并后的结果
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        ListNode r = new ListNode(-1), p = r;
        // 优先级队列
        PriorityQueue<ListNode> listsHeadPriority = new PriorityQueue<>(lists.length, Comparator.comparingInt(a -> a.val));

        List<ListNode> filterLists = Arrays.stream(lists).filter(Objects::nonNull).collect(Collectors.toList());
        listsHeadPriority.addAll(filterLists);

        while (!listsHeadPriority.isEmpty()) {
            ListNode minListNode = listsHeadPriority.poll();
            p.next = minListNode;
            if (minListNode.next != null) {
                listsHeadPriority.add(minListNode.next);
            }
            p = p.next;
        }

        return r.next;
    }

    /**
     * 19. 删除链表的倒数第 N 个结点
     * 解题思路:
     *  利用双指针，第一个指针走k步后将第二个指针指向头节点，然后两个指针同时走，
     *  第一个指针走到末尾也就是走了 n-k 步，随之第二个指针也是走了n-k步，即指向了倒数第k个节点。
     * 时间复杂度: 最坏时间复杂度O(n), n为链表节点个数
     * @param head 链表头节点
     * @param k 倒数第k个节点
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int k) {
        if (head == null) return null;
        ListNode r = new ListNode(-1);
        r.next = head;
        // 查找倒数第k+1个元素
        ListNode nthFromEnd = findNthFromEnd(r, k + 1);

        // 删除了第k个节点
        nthFromEnd.next = nthFromEnd.next.next;

        return r.next;
    }

    private ListNode findNthFromEnd(ListNode head, int k) {
        ListNode p1 = head, p2 = head;
        // p1指针先走k步, 也就是寻找到倒数第k个节点
        for (int i = 1; i <= k; i++) {
            p1 = p1.next;
        }
        // p1,p2走n-k步后, p2指向倒数第k个节点
        while (p1 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p2;
    }

    /**
     * 876. 链表的中间结点
     * 解决思路：快慢指针，slow指针移动一个节点，fast指针就移动两个节点，直到fast走到末尾，此时slow指针才走了一半
     * 时间复杂度：最坏时间复杂度为O(N), N为链表的长度
     * @param head
     * @return
     */
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    /**
     * 141. 环形链表
     * 解决思路：快慢指针，slow指针移动一个节点，fast指针就移动两个节点，如果fast指针遇到null则无环，否则fast和slow指针相遇则有环
     * 时间复杂度：最坏时间复杂度为O(N), N为链表的长度
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            // 如果快慢指针相遇则代表有环
            if (slow == fast) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 142. 环形链表 II
     * 解题思路:
     * （1）快慢指针，slow指针移动一个节点，fast指针就移动两个节点，如果存在环，则slow和fast肯定会在环内相遇，
     *  那么fast 一定比 slow 多走了 k 步，这多走的 k 步其实就是 fast 指针在环里转圈圈，所以 k 的值就是环长度的「整数倍」。
     * （2）假设相遇点距环的起点的距离为 m，那么结合上图的 slow 指针，环的起点距头结点 head 的距离为 k - m，也就是说如果从 head 前进 k - m 步就能到达环起点。
     * （3）巧的是，如果从相遇点继续前进 k - m 步，也恰好到达环起点。因为结合上图的 fast 指针，从相遇点开始走k步可以转回到相遇点，那走 k - m 步肯定就走到环起点了
     * 时间复杂度: 最坏时间复杂度为O(N)
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        int meetCount = 0;
        while (fast != null && fast.next != null) {
            if (meetCount == 0) {
                // 第一次相遇前为快慢指针
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    // 第一次相遇将slow指向链表头部，并标记相遇第一次
                    slow = head;
                    meetCount++;
                }
            } else {
                if (slow == fast) {
                    // 第二次相遇点则为环入口节点
                    return slow;
                }
                // 第二次相遇前为同步指针
                slow = slow.next;
                fast = fast.next;
            }
        }

        return null;
    }

    ListNode detectCycleTemp(ListNode head) {
        ListNode fast, slow;
        fast = slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        // 上面的代码类似 hasCycle 函数
        if (fast == null || fast.next == null) {
            // fast 遇到空指针说明没有环
            return null;
        }

        // 重新指向头结点
        slow = head;
        // 快慢指针同步前进，相交点就是环起点
        while (slow != fast) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 160. 相交链表
     * 解题思路: 如果两个链表有相交，有个特点就是p1指针遍历链表1再遍历链表2，同时p2先遍历链表2再遍历链表1，那么两个指针总会在第二次遍历
     *  时在交点相遇。
     * 时间复杂度: 最坏时间复杂度O(m+n)，m,n为两条链表的长度
     * @param headA
     * @param headB
     * @return
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode p1 = headA, p2 = headB;
        int p1Count = 0, p2Count = 0;
        while (p1 != null && p2 != null) {
            if (p1 == p2) {
                return p1;
            }
            p1 = p1.next;
            p2 = p2.next;
            if (p1 == null && p1Count == 0) {
                p1 = headB;
                p1Count++;
            }
            if (p2 == null && p2Count == 0) {
                p2 = headA;
                p2Count++;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        // test mergeTwoLists
//        ListNode listNode1 = new ListNode(1);
//        listNode1.next = new ListNode(3);
//        listNode1.next.next = new ListNode(5);
//        listNode1.next.next.next = new ListNode(6);
//
//        ListNode listNode2 = new ListNode(2);
//        listNode2.next = new ListNode(4);
//        listNode2.next.next = new ListNode(7);
//        listNode2.next.next.next = new ListNode(8);

//        new DoublePointer().mergeTwoLists(listNode1, listNode2);

        // test removeNthFromEnd
//        ListNode listNode = new ListNode(1);
//        new DoublePointer().removeNthFromEnd(listNode, 1);

        // test detectCycle
        ListNode listNode = new ListNode(1);
        listNode.next = listNode;
        new DoublePointer().detectCycle(listNode);
    }

}
