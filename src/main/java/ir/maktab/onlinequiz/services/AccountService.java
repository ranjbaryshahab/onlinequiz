package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dto.AccountSearchDTO;
import ir.maktab.onlinequiz.dto.LoginAccountDTO;
import ir.maktab.onlinequiz.dto.RegisterAccountDTO;
import ir.maktab.onlinequiz.exceptions.AccountNotFoundException;
import ir.maktab.onlinequiz.exceptions.UsernameExistInSystemException;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.outcome.LoginToAccountOutcome;
import ir.maktab.onlinequiz.outcome.RegisterAccountOutcome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface AccountService {
    @Secured("ROLE_GUEST")
    RegisterAccountOutcome register(RegisterAccountDTO registerAccountDto) throws UsernameExistInSystemException;

    @Secured("ROLE_USER")
    LoginToAccountOutcome login(LoginAccountDTO loginAccountDto) throws AccountNotFoundException;

    @Secured("ROLE_MANAGER")
    Page<Account> paginatedAwaitingApprovalAccounts(int pageNo, int pageSize);

    @Secured("ROLE_MANAGER")
    void acceptAllSelected(List<Long> idList);

    @Secured("ROLE_MANAGER")
    void dismissAllSelected(List<Long> idList);

    @Secured("ROLE_MANAGER")
    void acceptAll();

    @Secured("ROLE_MANAGER")
    void dismissAll();

    @Secured("ROLE_MANAGER")
    Page<Account> accountSearch(AccountSearchDTO accountSearchDTO, Pageable pageable);

    @Secured("ROLE_MANAGER")
    Page<Account> paginatedAccounts(int pageNo, int pageSize);

    @Secured("ROLE_MANAGER")
    List<Account> accounts();
}
