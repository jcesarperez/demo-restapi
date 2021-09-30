package com.jcesarperez.demo.restapi.application;

import com.jcesarperez.demo.restapi.domain.Resource;
import com.jcesarperez.demo.restapi.domain.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/resources")
public class ResourceRestController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping()
    public List<Resource> findAll() {
        return resourceService.findAll();
    }

    @GetMapping("/{id}")
    public Resource find(@PathVariable Long id) {
        Optional<Resource> resource = resourceService.find(id);

        if (resource.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return resource.get();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(@Valid @RequestBody Resource resource) {
        return resourceService.create(resource);
    }

    @PutMapping()
    public Resource update(@Valid @RequestBody Resource resource) {
        if (resource.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return resourceService.update(resource);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        resourceService.delete(id);

        return "deleted: " + id;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
