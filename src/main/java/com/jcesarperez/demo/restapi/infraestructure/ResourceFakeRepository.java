package com.jcesarperez.demo.restapi.infraestructure;

import com.jcesarperez.demo.restapi.domain.Resource;
import com.jcesarperez.demo.restapi.domain.ResourceRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

@Repository
public class ResourceFakeRepository implements ResourceRepository {

    private Map<Long, Resource> resourceMap = new HashMap<>();

    @PostConstruct
    public void init() {
        resourceMap.put(1L, new Resource(1L, "resource1", LocalDate.parse("2021-01-31"), Boolean.TRUE));
        resourceMap.put(2L, new Resource(2L, "resource2", LocalDate.parse("2021-01-31"), Boolean.TRUE));
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<>(resourceMap.values());
    }

    @Override
    public Optional<Resource> find(Long id) {
        return Optional.ofNullable(resourceMap.values().stream()
                .filter(r -> id.equals(r.getId()))
                .findFirst()
                .orElse(null));
    }

    @Override
    public Resource create(Resource resource) {
        Long nextId = resourceMap.values().stream()
                .mapToLong(Resource::getId)
                .max()
                .getAsLong()
                +1;

        resource.setId(nextId);
        resourceMap.put(nextId, resource);
        return resource;
    }

    @Override
    public Resource update(Resource resource) {
        resourceMap.put(resource.getId(), resource);
        return resource;
    }

    @Override
    public void delete(Long id) {
        resourceMap.remove(id);
    }
}
