package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.Owner;

public interface OwnerService extends CrudService<OwnerService, Long> {

    Owner findByLastName(String lastName);

}
