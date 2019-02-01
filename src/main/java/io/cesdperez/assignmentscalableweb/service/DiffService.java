package io.cesdperez.assignmentscalableweb.service;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.cesdperez.assignmentscalableweb.controller.ApiException;
import io.cesdperez.assignmentscalableweb.dto.Diff;
import io.cesdperez.assignmentscalableweb.persistence.DiffInputsEntity;
import io.cesdperez.assignmentscalableweb.persistence.DiffInputsRepository;

@Service
public class DiffService {

    private final DiffInputsRepository repository;

    @Autowired
    public DiffService(DiffInputsRepository repository) {
        this.repository = repository;
    }

    public Diff computeDiff(String id) throws ApiException {
        DiffInputsEntity entity = validateAndGet(id);
        String diff = computeDiff(entity.getLeft(), entity.getRight());
        return mapToDto(entity, diff);
    }

    public void putLeft(String id, byte[] content) {
        DiffInputsEntity entity = repository.findById(id).orElse(new DiffInputsEntity(id));
        entity.setLeft(content);
        repository.save(entity);
    }

    public void putRight(String id, byte[] content) {
        DiffInputsEntity entity = repository.findById(id).orElse(new DiffInputsEntity(id));
        entity.setRight(content);
        repository.save(entity);
    }

    private DiffInputsEntity validateAndGet(String id) throws ApiException {
        DiffInputsEntity entity = repository.findById(id)
                .orElseThrow(() -> new ApiException(PRECONDITION_FAILED, "The left and right parts are missing for computing diff {" + id + "}"));

        if (entity.getLeft() == null) {
            throw new ApiException(PRECONDITION_FAILED, "The left part is missing for computing diff {" + id + "}");
        }

        if (entity.getRight() == null) {
            throw new ApiException(PRECONDITION_FAILED, "The right part is missing for computing diff {" + id + "}");
        }

        return entity;
    }

    private String computeDiff(byte[] left, byte[] right) {
        int diffs = DiffUtils.countDifferentBytes(left, right);

        if (diffs == 0) {
            return "Left and Right are the same";
        }

        if (left.length > right.length) {
            return String.format("Left is longer than Right, and %s byte(s) are different", diffs);
        }

        if (right.length > left.length) {
            return String.format("Right is longer than Left, and %s bytes(s) are different", diffs);
        }

        return String.format("Left and Right have the same length, but %s byte(s) are different", diffs);
    }

    private Diff mapToDto(DiffInputsEntity entity, String diff) {
        return Diff.builder()
                .id(entity.getId())
                .left(entity.getLeft())
                .right(entity.getRight())
                .diff(diff)
                .build();
    }
}
