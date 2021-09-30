package com.jcesarperez.demo.restapi.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class Resource {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate creationDate;
    @NotNull
    private Boolean active;

    public Resource(Long id, String name, LocalDate creationDate, Boolean active) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.active = active;
    }

    public Resource() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id)
                && Objects.equals(name, resource.name)
                && Objects.equals(creationDate, resource.creationDate)
                && Objects.equals(active, resource.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creationDate, active);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", active=" + active +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
