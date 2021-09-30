package com.jcesarperez.demo.restapi.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcesarperez.demo.restapi.domain.Resource;
import com.jcesarperez.demo.restapi.domain.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc()
public class ResourceRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResourceRepository repository;

    private ObjectMapper jsonMapper;

    public ResourceRestControllerTests() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void givenExistingResources_whenGet_thenReturnTheResources() throws Exception {
        int expectedNumberOfResources = repository.findAll().size();

        mvc.perform(get("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(expectedNumberOfResources))
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[*].name").isNotEmpty())
                .andExpect(jsonPath("$[*].creationDate").isNotEmpty())
                .andExpect(jsonPath("$[*].active").isNotEmpty());
    }

    @Test
    public void givenExistingResourceWithId_whenGetIt_thenReturnTheResource() throws Exception {
        Resource resource = repository.create(new Resource(null, "resource 1", LocalDate.parse("2021-09-16"), Boolean.TRUE));

        mvc.perform(get("/api/resources/{id}", resource.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(resource.getId().intValue())))
                .andExpect(jsonPath("$.name").value(resource.getName()))
                .andExpect(jsonPath("$.creationDate").value(resource.getCreationDate().toString()))
                .andExpect(jsonPath("$.active").value(resource.getActive()));
    }

    @Test
    public void givenResourceNotExist_whenGetIt_thenReturnNotFound() throws Exception {
        int id = -1;

        mvc.perform(get("/api/resources/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewResource_whenPostIt_thenIsCreated() throws Exception {
        Resource newResource = new Resource(null, "new resource", LocalDate.parse("2021-09-16"), Boolean.TRUE);

        mvc.perform(post("/api/resources")
                        .content(jsonMapper.writeValueAsString(newResource))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(newResource.getName()))
                .andExpect(jsonPath("$.creationDate").value(newResource.getCreationDate().toString()))
                .andExpect(jsonPath("$.active").value(newResource.getActive()));
    }

    @Test
    public void givenNewInvalidResource_whenPostIt_thenReturnsError() throws Exception {
        Resource newResource = new Resource(null, "new invalid resource", null, null);

        mvc.perform(post("/api/resources")
                        .content(jsonMapper.writeValueAsString(newResource))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenOneUpdatedResource_whenPutIt_thenIsUpdated() throws Exception {
        Resource resource = repository.create(new Resource(null, "resource", LocalDate.parse("2021-09-16"), Boolean.TRUE));
        resource.setName("updated resource");

        mvc.perform(put("/api/resources")
                            .content(jsonMapper.writeValueAsString(resource))
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resource.getId()))
                .andExpect(jsonPath("$.name").value(resource.getName()))
                .andExpect(jsonPath("$.creationDate").value(resource.getCreationDate().toString()))
                .andExpect(jsonPath("$.active").value(resource.getActive()));
    }

    @Test
    public void givenResourceWithoutId_whenPutIt_thenReturnsError() throws Exception {
        Resource newResource = new Resource(null, "invalid resource", LocalDate.parse("2021-01-01"), Boolean.FALSE);

        mvc.perform(put("/api/resources")
                            .content(jsonMapper.writeValueAsString(newResource))
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenExistingResourceWithId_whenDeleteIt_thenReturnsOk() throws Exception {
        Long id = 1L;
        repository.create(new Resource(id, "resource 1", LocalDate.parse("2021-09-16"), Boolean.TRUE));

        mvc.perform(delete("/api/resources/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Resource> resource = repository.find(id);
        assertTrue(resource.isEmpty());
    }
}
