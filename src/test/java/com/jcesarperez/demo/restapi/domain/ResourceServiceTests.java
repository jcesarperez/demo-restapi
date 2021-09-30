package com.jcesarperez.demo.restapi.domain;


import com.jcesarperez.demo.restapi.infraestructure.ResourceFakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceServiceTests {

    private ResourceService resourceService;

    @BeforeEach
    public void init() {
        ResourceFakeRepository resourceRepository = new ResourceFakeRepository();
        resourceRepository.init();
        resourceService = new ResourceServiceImpl(resourceRepository);
    }

    @Test
    public void givenSomeResources_whenFindAll_thenReturnTheResources() {
        List<Resource> resources;

        resources = resourceService.findAll();

        assertEquals(2, resources.size());
        for (Resource resource: resources) {
            assertResourceNotNull(resource);
        }
    }

    @Test
    public void givenIdOfExistingResource_whenFindIt_thenReturnTheResource() {
        Long id = 1L;

        Optional<Resource> resource = resourceService.find(id);

        assertTrue(resource.isPresent());
        assertResourceNotNull(resource.get());
        assertEquals(id, resource.get().getId());
    }

    @Test
    public void givenIdOfNonExistingResource_whenFindIt_thenReturnNull() {
        Long id = -1L;

        Optional<Resource> resource = resourceService.find(id);

        assertTrue(resource.isEmpty());
    }

    @Test
    public void givenNewResource_whenCreateIt_thenReturnsTheAddedResource() {
        Resource resource = new Resource(null, "string", LocalDate.parse("2021-08-31"), Boolean.TRUE);

        Resource savedResource = resourceService.create(resource);

        assertNotNull(savedResource.getId());
    }

    @Test
    public void givenNewResourceWithNullString_whenCreateIt_thenThrowsException() {
        Resource resource = new Resource(1L, null, LocalDate.parse("2021-08-31"), Boolean.TRUE);

        assertThrows(IllegalArgumentException.class, () ->
                resourceService.create(resource));
    }

    @Test
    public void givenOneResource_whenUpdateIt_thenItIsUpdated() {
        Resource resource = resourceService.find(1L).get();
        resource.setName("string updated");

        Resource updatedResource = resourceService.update(resource);

        assertEquals(resource, updatedResource);
    }

    @Test
    public void givenOneResourceWithNullId_whenUpdateIt_thenThrowsException() {
        Resource resource = new Resource(null, "string updated", LocalDate.parse("2021-08-31"), Boolean.TRUE);

        assertThrows(IllegalArgumentException.class, () ->
                resourceService.update(resource));
    }

    @Test
    public void givenOneResource_whenDeleteIt_thenNotExistsInRepository() {
        Optional<Resource> resource = resourceService.find(1L);
        assertTrue(resource.isPresent());

        resourceService.delete(1L);

        Optional<Resource> deletedResource = resourceService.find(1L);
        assertTrue(deletedResource.isEmpty());
    }

    private void assertResourceNotNull(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getActive());
        assertNotNull(resource.getCreationDate());
        assertNotNull(resource.getName());
    }
}
