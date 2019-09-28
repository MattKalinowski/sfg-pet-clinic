package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/owners")
@Controller
public class OwnerController {

    private static final String CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String FIND_OWNERS = "owners/findOwners";
    private static final String REDIRECT_OWNERS = "redirect:/owners/";
    private static final String OWNERS_LIST = "owners/ownersList";
    private static final String OWNER_DETAILS = "owners/ownerDetails";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping("/find")
    public String findOwners(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return FIND_OWNERS;
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        List<Owner> results = ownerService.findAllByLastName(owner.getLastName());

        if (results.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return OwnerController.FIND_OWNERS;
        } else if (results.size() == 1) {
            owner = results.iterator().next();
            return REDIRECT_OWNERS + owner.getId();
        } else {
            model.addAttribute("selections", results);
            return OWNERS_LIST;
        }
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable Long ownerId) {
        ModelAndView mav = new ModelAndView(OWNER_DETAILS);
        mav.addObject("owner", ownerService.findById(ownerId));
        return mav;
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return CREATE_OR_UPDATE_OWNER_FORM;
    }

    @PostMapping("/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_OWNER_FORM;
        } else {
            Owner savedOwner = ownerService.save(owner);
            return REDIRECT_OWNERS + savedOwner.getId();
        }
    }

    @GetMapping("/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable Long ownerId, Model model) {
        model.addAttribute("owner", ownerService.findById(ownerId));
        return CREATE_OR_UPDATE_OWNER_FORM;
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateOwnerForm(@PathVariable Long ownerId,
                                         @Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_OWNER_FORM;
        } else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return REDIRECT_OWNERS + savedOwner.getId();
        }
    }
}
