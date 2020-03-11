package com.przemek.choir.controllersIntegrationTest;

import com.przemek.choir.exceptions.particularExceptions.ChoristerNotFound;
import com.przemek.choir.models.Chorister;
import com.przemek.choir.services.ChoirService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/choir")
public class ChoirController {

    private final ChoirService choirService;

    public ChoirController(ChoirService choirService) {
        this.choirService = choirService;
    }

    @GetMapping("/choristers")
    public List<Chorister> getSortedChoristers() {
        return choirService.getAllChoristerSortedByName();
    }

    @PostMapping("/chorister")
    public Integer addChorister(@Validated @RequestBody Chorister chorister) {
        return choirService.addChorister(chorister);
    }

    @DeleteMapping("/chorister/{id}")
    public ResponseEntity deleteChoristerById(@PathVariable Integer id) {
        choirService.deleteChorister(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/chorister")
    public Chorister updateChorister(@Validated @RequestBody Chorister chorister) throws ChoristerNotFound {
        return choirService.updateChorister(chorister);
    }
}
