package io.cesdperez.assignmentscalableweb.service;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

public class DiffUtils {

    /**
     * Counts how many bytes are different given 2 byte arrays.
     * If the array lengths are not the same, all remaining bytes are considered different.
     * (eg comparing {0,1} and {0,1,0} will return 1)
     *
     * Preconditions: left!=null&right!=null
     */
    public static int countDifferentBytes(@NotNull byte[] left, @NotNull byte[] right) {
        if (Arrays.equals(left, right)) return 0;

        int shorter = left.length < right.length ? left.length : right.length;
        int longer = left.length > right.length ? left.length : right.length;

        int diffs = 0;
        for (int i = 0; i < shorter; i++) {
            if (left[i] != right[i]) {
                diffs++;
            }
        }
        return diffs + (longer - shorter);
    }
}
