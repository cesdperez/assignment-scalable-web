package io.cesdperez.assignmentscalableweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.cesdperez.assignmentscalableweb.dto.BinaryContent;
import io.cesdperez.assignmentscalableweb.dto.Diff;
import io.cesdperez.assignmentscalableweb.service.DiffService;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private static final Logger log = LoggerFactory.getLogger(DiffController.class);

    private final DiffService diffService;

    @Autowired
    public DiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @PutMapping(value = "/{id}/left", produces = "application/json", consumes = "application/json")
    public void putLeft(@PathVariable("id") String id, @RequestBody(required = true) BinaryContent content) {
        log.debug("Calling putLeft with id {} and content {}", id, content);
        diffService.putLeft(id, content.getContent());
        log.info("Left input for diff with id {} successfully saved/updated", id);
    }

    @PutMapping(value = "/{id}/right", produces = "application/json", consumes = "application/json")
    public void putRight(@PathVariable("id") String id, @RequestBody(required = true) BinaryContent content) {
        log.debug("Calling putRight with id {} and content {}", id, content);
        diffService.putRight(id, content.getContent());
        log.info("Right input for diff with id {} successfully saved/updated", id);
    }

    @GetMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public Diff getDiff(@PathVariable("id") String id) throws ApiException {
        log.debug("Calling getDiff with id {}", id);
        Diff diff = diffService.computeDiff(id);
        log.info("Diff with id {} successfully computed", id);
        return diff;
    }
}
