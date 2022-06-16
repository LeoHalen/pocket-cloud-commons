package site.halenspace.pocketcloud.threadpool.array.slidingWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Halen Leo · 2022/5/15
 */
public class MinWindow {

    /**
     *
     * @param s
     * @param t
     * @return
     */
    public String minWindow(String s, String t) {
        char[] arr = s.toCharArray();
        char[] tArr = t.toCharArray();
        HashMap<Character, Integer> window = new HashMap<>(), need = new HashMap<>(t.length());
        // 初始化need哈希表
        for (int i = 0, len = t.length(); i < len; i++) {
            char tC = tArr[i];
            if (need.containsKey(tC)) {
                need.put(tC, need.get(tC) + 1);
            } else {
                need.put(tC, 1);
            }
        }

        int left = 0, right = 0;
        int valid = 0;
        // 记录最小覆盖子串的起始索引及长度
        int start = 0, len = Integer.MAX_VALUE;
        while (right < s.length()) {
            char c = arr[right++];
            // 进行窗口内数据的一系列更新
            if (need.containsKey(c)) {
                // 装入窗口中
                Integer wv = window.putIfAbsent(c, 1);
                if (wv != null) {
                    window.put(c, wv + 1);
                }

                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }

            // 判断左侧窗口是否要收缩
            while (valid == need.size()) {
                if (right - left < len) {
                    start = left;
                    len = right - left;
                }
                // d是将要移出窗口中的字符
                char d = arr[left++];
                // 进行窗口内数据的一系列更新
                if (need.containsKey(d)) {
                    if (window.get(c).equals(need.get(c))) {
                        valid--;
                    }

                    // 移出窗口中
                    Integer wv = window.get(d);
                    if (wv > 1) {
                        window.put(d, window.get(d) - 1);
                    } else {
                        window.remove(d);
                    }
                }
            }
        }

        return len == Integer.MAX_VALUE ? "" : s.substring(start, len);
    }

    public static void main(String[] args) {
//        new MinWindow().minWindowNew("ADOBECODEBANC", "ABC");
        new MinWindow().lengthOfLongestSubstring("pwwkew");
    }

    public String minWindowNew(String s, String t) {
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Map<Character, Integer> window = new HashMap<>(), need = new HashMap<>();
        // 初始化 need
        for (Character tChar : tArr) {
            if (need.containsKey(tChar)) {
                need.put(tChar, need.get(tChar) + 1);
            } else {
                need.put(tChar, 1);
            }
        }

        int validCount = 0;
        // 最小覆盖子串，初始化为int最大值
        int minSubStrLen = Integer.MAX_VALUE;
        int minSubStrStart = 0;
        // 初始的窗口[0, 0)
        int left = 0, right = 0;
        while (right < s.length()) {
            // 装入窗口中
            Character rightChar = sArr[right++];
            if (need.containsKey(rightChar)) {
                Integer oldValue = window.putIfAbsent(rightChar, 1);
                if (oldValue != null) {
                    window.put(rightChar, oldValue + 1);
                }
                if (window.get(rightChar).equals(need.get(rightChar))) {
                    // 有效标记
                    validCount++;
                }
            }

            // 缩小窗口
            while (validCount == need.size()) {
                // 更新最小覆盖子串位置记录
                if (right - left < minSubStrLen) {
                    minSubStrStart = left;
                    minSubStrLen = right - left;
                }

                Character leftChar = sArr[left++];
                if (need.containsKey(leftChar)) {
                    if (window.get(leftChar).equals(need.get(leftChar))) {
                        // 清除有效标记
                        validCount--;
                    }

                    // 缩小左边窗口
                    Integer windowValue = window.get(leftChar);
                    if (windowValue > 1) {
                        window.put(leftChar, window.get(leftChar) - 1);
                    } else {
                        window.remove(leftChar);
                    }
                }
            }
        }

        return minSubStrLen == Integer.MAX_VALUE ? "" : s.substring(minSubStrStart, minSubStrStart + minSubStrLen);
    }

    public Boolean checkInclusion(String s, String t) {
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Map<Character, Integer> window = new HashMap<>(), need = new HashMap<>();
        // 初始化 need
        for (Character tChar : tArr) {
            if (need.containsKey(tChar)) {
                need.put(tChar, need.get(tChar) + 1);
            } else {
                need.put(tChar, 1);
            }
        }

        int validCount = 0;
        // 初始的窗口[0, 0)
        int left = 0, right = 0;
        while (right < s.length()) {
            // 装入窗口中
            Character rightChar = sArr[right++];
            if (need.containsKey(rightChar)) {
                Integer oldValue = window.putIfAbsent(rightChar, 1);
                if (oldValue != null) {
                    window.put(rightChar, oldValue + 1);
                }
                if (window.get(rightChar).equals(need.get(rightChar))) {
                    // 有效标记
                    validCount++;
                }
            }

            // 缩小窗口
            while (right - left >= t.length()) {
                if (validCount == need.size()) {
                    return true;
                }

                Character leftChar = sArr[left++];
                if (need.containsKey(leftChar)) {
                    if (window.get(leftChar).equals(need.get(leftChar))) {
                        // 清除有效标记
                        validCount--;
                    }

                    // 缩小左边窗口
                    Integer windowValue = window.get(leftChar);
                    if (windowValue > 1) {
                        window.put(leftChar, window.get(leftChar) - 1);
                    } else {
                        window.remove(leftChar);
                    }
                }
            }
        }

        return false;
    }

    public List<Integer> findAnagrams(String s, String t) {
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Map<Character, Integer> window = new HashMap<>(), need = new HashMap<>();
        // 初始化 need
        for (Character tChar : tArr) {
            if (need.containsKey(tChar)) {
                need.put(tChar, need.get(tChar) + 1);
            } else {
                need.put(tChar, 1);
            }
        }

        List<Integer> result = new ArrayList<>();
        int validCount = 0;
        // 初始的窗口[0, 0)
        int left = 0, right = 0;
        while (right < s.length()) {
            // 装入窗口中
            Character rightChar = sArr[right++];
            if (need.containsKey(rightChar)) {
                Integer oldValue = window.putIfAbsent(rightChar, 1);
                if (oldValue != null) {
                    window.put(rightChar, oldValue + 1);
                }
                if (window.get(rightChar).equals(need.get(rightChar))) {
                    // 有效标记
                    validCount++;
                }
            }

            // 缩小窗口
            while (right - left >= t.length()) {
                if (validCount == need.size()) {
                    result.add(left);
                }

                Character leftChar = sArr[left++];
                if (need.containsKey(leftChar)) {
                    if (window.get(leftChar).equals(need.get(leftChar))) {
                        // 清除有效标记
                        validCount--;
                    }

                    // 缩小左边窗口
                    Integer windowValue = window.get(leftChar);
                    if (windowValue > 1) {
                        window.put(leftChar, window.get(leftChar) - 1);
                    } else {
                        window.remove(leftChar);
                    }
                }
            }
        }

        return result;
    }

    public Integer lengthOfLongestSubstring(String s) {
        Map<Character, Integer> window = new HashMap<>();
        char[] sArr = s.toCharArray();

        int maxSubStrLen = 0;
        int left = 0, right = 0;
        while (right < s.length()) {
            char rightChar = sArr[right++];
            Integer oldValue = window.putIfAbsent(rightChar, 1);
            if (oldValue != null) {
                window.put(rightChar, window.get(rightChar) + 1);
            }

            while (window.getOrDefault(rightChar, 0) > 1) {
                // 缩小窗口
                char leftChar = sArr[left++];
                Integer windowValue = window.get(leftChar);
                if (windowValue > 1) {
                    window.put(leftChar, windowValue - 1);
                } else {
                    window.remove(leftChar);
                }
            }
            // 更新最长子串长度
            maxSubStrLen = Math.max(maxSubStrLen, right - left);
        }

        return maxSubStrLen;
    }
}
