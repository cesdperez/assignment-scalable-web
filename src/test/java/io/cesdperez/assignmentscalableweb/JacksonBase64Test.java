package io.cesdperez.assignmentscalableweb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.cesdperez.assignmentscalableweb.dto.BinaryContent;
import io.cesdperez.assignmentscalableweb.dto.Diff;

@RunWith(JUnit4.class)
public class JacksonBase64Test {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void it_decodes_from_base64() throws IOException {
        String encoded = "{\"content\":\"AQ==\"}";

        BinaryContent decoded = objectMapper.readValue(encoded, BinaryContent.class);

        assertThat(decoded.getContent(), is(new byte[]{1}));
    }

    @Test
    public void it_fails_to_decode_non_encoded_input() throws IOException {
        String badEncoded = "{\"content\":\"bad\"}";

        try {
            objectMapper.readValue(badEncoded, BinaryContent.class);
            assert false;
        } catch (InvalidFormatException ex) {
            assert true;
        }
    }

    @Test
    public void it_encodes_to_base64() throws IOException {
        Diff decoded = Diff.builder()
                .id("1")
                .left(new byte[]{1, 0})
                .right(new byte[]{0, 1})
                .diff("diff result")
                .build();

        String encoded = objectMapper.writeValueAsString(decoded);

        assertThat(encoded, is("{\"id\":\"1\",\"left\":\"AQA=\",\"right\":\"AAE=\",\"diff\":\"diff result\"}"));
    }
}
