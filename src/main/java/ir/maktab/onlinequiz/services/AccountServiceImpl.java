package ir.maktab.onlinequiz.services;

import ir.maktab.onlinequiz.dao.*;
import ir.maktab.onlinequiz.dto.AccountSearchDTO;
import ir.maktab.onlinequiz.dto.AccountStatusDTO;
import ir.maktab.onlinequiz.dto.LoginAccountDTO;
import ir.maktab.onlinequiz.dto.RegisterAccountDTO;
import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.enums.RoleTypes;
import ir.maktab.onlinequiz.exceptions.AccountNotFoundException;
import ir.maktab.onlinequiz.exceptions.UsernameExistInSystemException;
import ir.maktab.onlinequiz.mappers.RoleMapper;
import ir.maktab.onlinequiz.models.Account;
import ir.maktab.onlinequiz.models.Person;
import ir.maktab.onlinequiz.models.Student;
import ir.maktab.onlinequiz.models.Teacher;
import ir.maktab.onlinequiz.outcome.LoginToAccountOutcome;
import ir.maktab.onlinequiz.outcome.RegisterAccountOutcome;
import ir.maktab.onlinequiz.specification.AccountSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    final AccountDAO accountDao;
    final RoleDAO roleDao;
    final PersonDAO personDao;
    final AccountSpecification accountSpecification;

    public AccountServiceImpl(AccountDAO accountDao, RoleDAO roleDao, PersonDAO personDao, StudentDAO studentDao, TeacherDAO teacherDao, AccountSpecification accountSpecification) {
        this.accountDao = accountDao;
        this.roleDao = roleDao;
        this.personDao = personDao;
        this.accountSpecification = accountSpecification;
    }

    @Override
    public RegisterAccountOutcome register(RegisterAccountDTO registerAccountDto) throws UsernameExistInSystemException {
        if (accountDao.findByUsername(registerAccountDto.getUsername()).isPresent())
            throw new UsernameExistInSystemException("کاربری با این نام کاربری در سیستم وجود دارد!");
        Person person = new Person();
        String code = "" + LocalDate.now().getYear() + LocalDate.now().getMonth().getValue() +
                LocalDate.now().getDayOfMonth() + LocalTime.now().getHour() +
                LocalTime.now().getMinute() + LocalTime.now().getSecond();

        if (registerAccountDto.getRole().equals("ROLE_STUDENT")) {
            Student student = new Student();
            student.setStudentCode("S_" + code);
            person = student;
        } else if (registerAccountDto.getRole().equals("ROLE_TEACHER")) {
            Teacher teacher = new Teacher();
            teacher.setTeacherCode("T_" + code);
            person = teacher;
        }
        Account account = accountDao.save(
                new Account(
                        null,
                        registerAccountDto.getUsername(),
                        new BCryptPasswordEncoder().encode(registerAccountDto.getPassword()),
                        AccountStatus.DEACTIVATE,
                        new Date(),
                        null,
                        person,
                        true,
                        Set.of(roleDao.findRoleJpaEntityByRoleType(RoleTypes.valueOf(registerAccountDto.getRole())),
                                roleDao.findRoleJpaEntityByRoleType(RoleTypes.ROLE_GUEST))
                ));

        return new RegisterAccountOutcome(account.getUsername());
    }

    @Override
    public LoginToAccountOutcome login(LoginAccountDTO loginAccountDto) throws AccountNotFoundException {
        Optional<Account> account = accountDao.findByUsername(loginAccountDto.getUsername());
        if (account.isEmpty())
            throw new AccountNotFoundException("اطلاعات وارد شده صحبح نمی باشد");

        account.get().setLastLoginDate(new Date());
        accountDao.save(account.get());
        return new LoginToAccountOutcome(account.get().getUsername(),
                account
                        .stream()
                        .flatMap(accountJpaEntity -> accountJpaEntity.getRoles().stream())
                        .map(RoleMapper::map)
                        .collect(Collectors.toSet()));
    }

    @Override
    public Page<Account> paginatedAwaitingApprovalAccounts(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return accountDao.findAllByAccountStatus(AccountStatus.AWAITING_APPROVAL, paging);
    }

    @Override
    public void acceptAllSelected(List<Long> idList) {
        accountDao.findAllByIdIn(idList)
                .stream()
                .map(account -> new Account(
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        AccountStatus.ACTIVATE,
                        account.getCreateAccountDate(),
                        account.getLastLoginDate(),
                        account.getPerson(),
                        account.isActive(),
                        account.getRoles()
                ))
                .collect(Collectors.toList())
                .forEach(accountDao::save);
    }

    @Override
    public void dismissAllSelected(List<Long> idList) {
        accountDao.findAllByIdIn(idList)
                .stream()
                .map(account -> new Account(
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        AccountStatus.REJECTED,
                        account.getCreateAccountDate(),
                        account.getLastLoginDate(),
                        account.getPerson(),
                        account.isActive(),
                        account.getRoles()
                ))
                .collect(Collectors.toList())
                .forEach(accountDao::save);
    }

    @Override
    public void acceptAll() {
        accountDao.findAllByAccountStatus(AccountStatus.AWAITING_APPROVAL)
                .stream()
                .map(account -> new Account(
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        AccountStatus.ACTIVATE,
                        account.getCreateAccountDate(),
                        account.getLastLoginDate(),
                        account.getPerson(),
                        account.isActive(),
                        account.getRoles()
                ))
                .collect(Collectors.toList())
                .forEach(accountDao::save);
    }

    @Override
    public void dismissAll() {
        accountDao.findAllByAccountStatus(AccountStatus.AWAITING_APPROVAL)
                .stream()
                .map(account -> new Account(
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        AccountStatus.REJECTED,
                        account.getCreateAccountDate(),
                        account.getLastLoginDate(),
                        account.getPerson(),
                        account.isActive(),
                        account.getRoles()
                ))
                .collect(Collectors.toList())
                .forEach(accountDao::save);
    }

    @Override
    public Page<Account> accountSearch(AccountSearchDTO accountSearchDTO, Pageable pageable) {
        accountSpecification.setAccountSearchDTO(accountSearchDTO);
        return accountDao.findAll(accountSpecification, pageable);
    }

    @Override
    public Page<Account> paginatedAccounts(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return accountDao.findAll(paging);
    }

    @Override
    public List<Account> accounts() {
        return accountDao.findAll();
    }

    @Override
    public Page<Account> findByStatusEqualsNot(AccountStatus accountStatus, Pageable pageable) {
        return accountDao.findAllByAccountStatusNot(accountStatus, pageable);
    }


}
