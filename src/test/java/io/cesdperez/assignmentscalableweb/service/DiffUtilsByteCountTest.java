package io.cesdperez.assignmentscalableweb.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DiffUtilsByteCountTest {

    @Test
    public void it_compares_equal_arrays() {
        // given
        byte[] left = new byte[]{0, 1};
        byte[] right = new byte[]{0, 1};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(0));
    }

    @Test
    public void it_compares_arrays_of_same_size_but_different() {
        // given
        byte[] left = new byte[]{0, 1, 0, 1, 1, 1};
        byte[] right = new byte[]{0, 1, 0, 0, 1, 0};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(2));
    }

    @Test
    public void it_compares_arrays_where_left_is_longer() {
        // given
        byte[] left = new byte[]{0, 1, 0, 1, 1};
        byte[] right = new byte[]{0, 1};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(3));
    }

    @Test
    public void it_compares_arrays_where_right_is_longer() {
        // given
        byte[] left = new byte[]{0, 1, 0};
        byte[] right = new byte[]{0, 1, 1, 1};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(2));
    }

    @Test
    public void it_compares_arrays_where_one_is_empty() {
        // given
        byte[] left = new byte[]{};
        byte[] right = new byte[]{0, 1, 1, 1};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(4));
    }

    @Test
    public void it_compares_two_empty_arrays() {
        // given
        byte[] left = new byte[]{};
        byte[] right = new byte[]{};

        // when
        int diff = DiffUtils.countDifferentBytes(left, right);

        // then
        assertThat(diff, is(0));
    }
}
