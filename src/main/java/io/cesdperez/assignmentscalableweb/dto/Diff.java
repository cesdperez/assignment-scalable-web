package io.cesdperez.assignmentscalableweb.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Diff {
    private final String id;
    private final byte[] left;
    private final byte[] right;
    private final String diff;
}
