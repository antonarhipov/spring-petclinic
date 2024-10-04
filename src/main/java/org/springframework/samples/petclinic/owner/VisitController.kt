/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
internal class VisitController(private val owners: OwnerRepository) {
    @InitBinder
    fun setAllowedFields(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    /**
     * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
     * we always have fresh data - Since we do not use the session scope, make sure that
     * Pet object always has an id (Even though id is not part of the form fields)
     * @param petId
     * @return Pet
     */
    @ModelAttribute("visit")
    fun loadPetWithVisit(
        @PathVariable("ownerId") ownerId: Int, @PathVariable("petId") petId: Int,
        model: MutableMap<String, Any>
    ): Visit {
        val owner = this.owners.findById(ownerId)

        val pet = owner.getPet(petId)
        model.put("pet", pet)
        model.put("owner", owner)

        val visit = Visit()
        pet.addVisit(visit)
        return visit
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
    // called
    @GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    fun initNewVisitForm(): String {
        return "pets/createOrUpdateVisitForm"
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
    // called
    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    fun processNewVisitForm(
        @ModelAttribute owner: Owner, @PathVariable petId: Int, visit: @Valid Visit,
        result: BindingResult, redirectAttributes: RedirectAttributes
    ): String {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm"
        }

        owner.addVisit(petId, visit)
        this.owners.save(owner)
        redirectAttributes.addFlashAttribute("message", "Your vist has been boked")
        return "redirect:/owners/{ownerId}"
    }
}
