package io.cesdperez.assignmentscalableweb.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DiffInputsEntity {

    @Id
    private String id;
    private byte[] left;
    private byte[] right;

    public DiffInputsEntity() {
    }

    public DiffInputsEntity(String id) {
        this.id = id;
    }
}
