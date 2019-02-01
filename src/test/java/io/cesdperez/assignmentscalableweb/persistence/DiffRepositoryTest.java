package io.cesdperez.assignmentscalableweb.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.cesdperez.assignmentscalableweb.persistence.DiffInputsEntity;
import io.cesdperez.assignmentscalableweb.persistence.DiffInputsRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiffRepositoryTest {

    @Autowired
    private DiffInputsRepository repository;

    @Test
    public void it_saves_and_retrieves_an_entity() {
        // given
        DiffInputsEntity expected = new DiffInputsEntity("id");
        expected.setLeft(new byte[]{0, 1, 1});
        expected.setRight(new byte[]{0, 1, 1});

        // when
        repository.save(expected);

        // and
        DiffInputsEntity result = repository.findById("id").orElse(null);

        // then
        assertThat(result, is(expected));
    }
}
