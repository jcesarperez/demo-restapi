package com.jcesarperez.demo.restapi.domain;

import java.util.List;
import java.util.Optional;

public interface ResourceService {

    List<Resource> findAll();

    Optional<Resource> find(Long id);

    Resource create(Resource resource);

    Resource update(Resource resource);

    void delete(Long id);
}
