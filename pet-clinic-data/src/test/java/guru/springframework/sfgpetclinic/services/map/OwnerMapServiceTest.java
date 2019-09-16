package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {

    OwnerMapService service;

    Long ownerId = 1L;
    String lastName = "Smith";

    @BeforeEach
    void setUp() {
        service = new OwnerMapService(new PetTypeMapService(), new PetMapService());

        Owner owner = Owner.builder().id(ownerId).lastName(lastName).build();
        service.save(owner);
    }

    @Test
    void findAll() {
        Set<Owner> owners = service.findAll();

        assertEquals(1, owners.size());
    }

    @Test
    void findById() {
        Owner owner = service.findById(ownerId);

        assertEquals(ownerId, owner.getId());

    }

    @Test
    void findByLastName() {
        Owner owner = service.findByLastName(lastName);

        assertNotNull(owner);
        assertEquals(ownerId, owner.getId());
    }

    @Test
    void findByLastNameNotFound() {
        Owner owner = service.findByLastName("foo");

        assertNull(owner);
    }

    @Test
    void saveExistingId() {
        Long id = 2L;

        Owner owner2 = new Owner();
        Owner savedOwner = service.save(owner2);

        assertEquals(id, savedOwner.getId());
    }

    @Test
    void saveNoId() {
        Owner savedOwner = service.save(new Owner());

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void delete() {
        service.delete(service.findById(ownerId));

        assertEquals(0, service.findAll().size());
    }

    @Test
    void deleteById() {
        service.deleteById(ownerId);

        assertEquals(0, service.findAll().size());
    }
}