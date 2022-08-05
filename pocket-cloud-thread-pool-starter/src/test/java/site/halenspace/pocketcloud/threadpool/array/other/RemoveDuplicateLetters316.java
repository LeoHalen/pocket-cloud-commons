package site.halenspace.pocketcloud.threadpool.array.other;

import java.util.Stack;

/**
 * Leetcode.316 去除重复字符
 * @author Zg.Li · 2022/6/17
 */
public class RemoveDuplicateLetters316 {

    public String removeDuplicateLetters(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }

        char[] arr = s.toCharArray();
        // 记录字符剩余出现的次数
        int[] count = new int[256];
        for (char c : arr) {
            count[c]++;
        }
        // 判断是否重复字符
        boolean[] inStack = new boolean[256];
        // 利用栈特性先进后出弹出重复字符
        Stack<Character> stack = new Stack<>();

        for (char c : arr) {
            // 遍历到一个字符就扣除对应剩余字符数
            count[c]--;

            if (inStack[c]) {
                continue;
            }

            // 弹出字典序比当前字符大的元素
            while (!stack.empty() && stack.peek() > c) {
                // 判断栈顶字符后续是否还会出现, 不存在则停止pop
                if (count[stack.peek()] == 0) {
                    break;
                }
                // 否则弹出栈顶字符，并将inStack置为false
                inStack[stack.pop()] = false;
            }

            // 不存在栈中则放入
            stack.push(c);
            // 并记录在数组中
            inStack[c] = true;
        }

        // 反转栈字符串
        StringBuilder sb = new StringBuilder();
        while (!stack.empty()) {
            sb.append(stack.pop());
        }

        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        new RemoveDuplicateLetters316().removeDuplicateLetters("bcabc");
    }
}
