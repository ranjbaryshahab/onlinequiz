package ir.maktab.onlinequiz.controllers;

import ir.maktab.onlinequiz.dto.AccountSearchDTO;
import ir.maktab.onlinequiz.dto.LoginAccountDTO;
import ir.maktab.onlinequiz.dto.NewUsersIdsListDTO;
import ir.maktab.onlinequiz.dto.RegisterAccountDTO;
import ir.maktab.onlinequiz.exceptions.AccountNotFoundException;
import ir.maktab.onlinequiz.exceptions.UsernameExistInSystemException;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.outcome.LoginToAccountOutcome;
import ir.maktab.onlinequiz.outcome.RegisterAccountOutcome;
import ir.maktab.onlinequiz.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping
public class AccountController {

    final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/user/register")
    private RegisterAccountOutcome register(@RequestBody RegisterAccountDTO registerAccountDto) throws UsernameExistInSystemException {
        return accountService.register(registerAccountDto);
    }

    @PostMapping("/user/login")
    private LoginToAccountOutcome login(@RequestBody LoginAccountDTO loginAccountDto) throws AccountNotFoundException {
        return accountService.login(loginAccountDto);
    }

    @PostMapping("/manager/accounts/new-users-list/{pageNo}/{pageSize}")
    private Page<Account> getPaginatedAwaitingApprovalAccounts(@PathVariable int pageNo, @PathVariable int pageSize) {
        return accountService.paginatedAwaitingApprovalAccounts(pageNo, pageSize);
    }

    @PostMapping("/manager/new-user-list/accept-all-selected")
    private void acceptAllSelected(@RequestBody NewUsersIdsListDTO newUsersIdsListDTO) {
        accountService.acceptAllSelected(newUsersIdsListDTO.getListId()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    @PostMapping("/manager/new-user-list/accept-all")
    private void acceptAll() {
        accountService.acceptAll();
    }

    @PostMapping("/manager/new-user-list/dismiss-all-selected")
    private void dismissAllSelected(@RequestBody NewUsersIdsListDTO newUsersIdsListDTO) {
        accountService.dismissAllSelected(newUsersIdsListDTO.getListId()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    @PostMapping("/manager/new-user-list/dismiss-all")
    private void dismissAll() {
        accountService.dismissAll();
    }

    @PostMapping("/manager/users-list/search/accounts/{pageNo}/{pageSize}")
    private Page<Account> search(@RequestBody AccountSearchDTO accountSearchDTO, @PathVariable int pageNo, @PathVariable int pageSize) {
        return accountService.accountSearch(accountSearchDTO, PageRequest.of(pageNo, pageSize));
    }

    @PostMapping("/manager/accounts/all/{pageNo}/{pageSize}")
    private Page<Account> getPaginatedAccounts(@PathVariable int pageNo, @PathVariable int pageSize) {
        return accountService.paginatedAccounts(pageNo, pageSize);
    }

    @PostMapping("/manager/accounts/findAll")
    private List<Account> accounts() {
        return accountService.accounts();
    }
}
