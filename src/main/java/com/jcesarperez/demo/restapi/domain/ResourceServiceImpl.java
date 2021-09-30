package com.jcesarperez.demo.restapi.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository repository;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.repository = resourceRepository;
    }

    @Override
    public List<Resource> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Resource> find(Long id) {
        Assert.notNull(id, "id must not be null");

        return repository.find(id);
    }

    @Override
    public Resource create(Resource resource) {
        Assert.isNull(resource.getId(), "id must be null");
        Assert.notNull(resource.getActive(), "active must not be null");
        Assert.notNull(resource.getCreationDate(), "creationDate must not be null");
        Assert.notNull(resource.getName(), "name must not be null");

        return repository.create(resource);
    }

    @Override
    public Resource update(Resource resource) {
        Assert.notNull(resource.getId(), "id must not be null");

        return repository.update(resource);
    }

    @Override
    public void delete(Long id) {
        Assert.notNull(id, "id must not be null");

        repository.delete(id);
    }
}
