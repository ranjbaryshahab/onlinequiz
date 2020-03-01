package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.PersonCompletionDto;
import ir.maktab.onlinequiz.exceptions.PreviouslyRecordedInformation;
import ir.maktab.onlinequiz.outcome.PersonCompletionOutcome;
import ir.maktab.onlinequiz.services.PersonService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class PersonController {
    final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/personCompletion")
    private PersonCompletionOutcome completion(@RequestBody PersonCompletionDto personCompletionDto) throws PreviouslyRecordedInformation {
        return personService.completion(personCompletionDto);
    }
}