package io.cesdperez.assignmentscalableweb.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.cesdperez.assignmentscalableweb.controller.ApiException;
import io.cesdperez.assignmentscalableweb.dto.Diff;
import io.cesdperez.assignmentscalableweb.persistence.DiffInputsEntity;
import io.cesdperez.assignmentscalableweb.persistence.DiffInputsRepository;

@RunWith(JUnit4.class)
public class DiffServiceTest {

    private DiffInputsRepository mockRepository;
    private DiffService diffService;

    @Before
    public void setup() {
        mockRepository = mock(DiffInputsRepository.class);
        diffService = new DiffService(mockRepository);
    }

    @Test
    public void it_puts_left() {
        // given
        String id = "someID";
        byte[] content = {1, 2};

        // when
        diffService.putLeft(id, content);

        // then
        ArgumentCaptor<DiffInputsEntity> captor = ArgumentCaptor.forClass(DiffInputsEntity.class);
        verify(mockRepository).save(captor.capture());

        // and
        DiffInputsEntity entity = captor.getValue();
        assertThat(entity.getId(), is(id));
        assertThat(entity.getLeft(), is(content));
    }

    @Test
    public void it_puts_right() {
        // given
        String id = "someID";
        byte[] content = {1, 2};

        // when
        diffService.putRight(id, content);

        // then
        ArgumentCaptor<DiffInputsEntity> captor = ArgumentCaptor.forClass(DiffInputsEntity.class);
        verify(mockRepository).save(captor.capture());

        // and
        DiffInputsEntity entity = captor.getValue();
        assertThat(entity.getId(), is(id));
        assertThat(entity.getRight(), is(content));
    }

    @Test
    public void it_computes_diff_with_left_and_right_equals() throws ApiException {
        // given
        String id = "someID";
        DiffInputsEntity entity = new DiffInputsEntity(id);
        entity.setLeft(new byte[]{0, 1});
        entity.setRight(new byte[]{0, 1});

        // and
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        // when
        Diff result = diffService.computeDiff(id);

        // then
        verify(mockRepository).findById(id);

        // and
        assertThat(result.getId(), is(id));
        assertThat(result.getDiff(), is("Left and Right are the same"));
    }

    @Test
    public void it_computes_diff_with_left_and_right_different() throws ApiException {
        // given
        String id = "someID";
        DiffInputsEntity entity = new DiffInputsEntity(id);
        entity.setLeft(new byte[]{0, 1, 0});
        entity.setRight(new byte[]{0, 1});

        // and
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        // when
        Diff result = diffService.computeDiff(id);

        // then
        verify(mockRepository).findById(id);

        // and
        assertThat(result.getId(), is(id));
        assertThat(result.getDiff(), is("Left is longer than Right, and 1 byte(s) are different"));
    }

    @Test
    public void it_fails_to_compute_diff_where_left_part_is_missing() throws ApiException {
        // given
        String id = "someID";
        DiffInputsEntity entity = new DiffInputsEntity(id);
        entity.setRight(new byte[]{0, 1});

        // and
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(entity));

        // when
        try {
            diffService.computeDiff(id);
            assert false;
        } catch (ApiException e) {
            // then
            verify(mockRepository).findById(id);
            // and
            assertThat(e.getStatus(), is(PRECONDITION_FAILED));
            assertThat(e.getMessage(), is("The left part is missing for computing diff {someID}"));
        }
    }
}
