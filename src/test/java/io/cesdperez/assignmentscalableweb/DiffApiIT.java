package io.cesdperez.assignmentscalableweb;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DiffApiIT {

    private static final String DIFF_ROOT_PATH = "/v1/diff";

    @Autowired
    private MockMvc mvc;

    @Test
    public void it_boots() {
    }

    @Test
    public void it_creates_and_updates_the_diff_left_part() throws Exception {
        mvc.perform(put(DIFF_ROOT_PATH + "/1/left").contentType(APPLICATION_JSON).content("{\"content\":\"AQ==\"}"))
                .andExpect(status().isOk());

        mvc.perform(put(DIFF_ROOT_PATH + "/1/left").contentType(APPLICATION_JSON).content("{\"content\":\"BQ==\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void it_creates_and_updates_the_diff_right_part() throws Exception {
        mvc.perform(put(DIFF_ROOT_PATH + "/1/right").contentType(APPLICATION_JSON).content("{\"content\":\"AQ==\"}"))
                .andExpect(status().isOk());

        mvc.perform(put(DIFF_ROOT_PATH + "/1/right").contentType(APPLICATION_JSON).content("{\"content\":\"BQ==\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void it_computes_a_diff() throws Exception {
        String body1 = "{\"content\":\"AQ==\"}";
        String body2 = "{\"content\":\"BQ==\"}";

        mvc.perform(put(DIFF_ROOT_PATH + "/1/left").contentType(APPLICATION_JSON).content(body1))
                .andExpect(status().isOk());

        mvc.perform(put(DIFF_ROOT_PATH + "/1/right").contentType(APPLICATION_JSON).content(body2))
                .andExpect(status().isOk());

        mvc.perform(get(DIFF_ROOT_PATH + "/1").contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.left").value("AQ=="))
                .andExpect(jsonPath("$.right").value("BQ=="))
                .andExpect(jsonPath("$.diff").value("Left and Right have the same length, but 1 byte(s) are different"));
    }

    @Test
    public void it_fails_to_read_non_encoded_input() throws Exception {
        mvc.perform(put(DIFF_ROOT_PATH + "/1/left").contentType(APPLICATION_JSON).content("{\"content\":\"bad\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

    @Test
    public void it_fails_when_left_or_right_parts_are_missing() throws Exception {
        mvc.perform(get(DIFF_ROOT_PATH + "/1").contentType(APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty());
    }

}

