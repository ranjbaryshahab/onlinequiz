package ir.maktab.onlinequiz.dao;

import ir.maktab.onlinequiz.enums.AccountStatus;
import ir.maktab.onlinequiz.models.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountDAO extends PagingAndSortingRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUsername(String username);

    Page<Account> findAllByAccountStatus(AccountStatus accountStatus, Pageable pageable);

    List<Account> findAllByAccountStatus(AccountStatus accountStatus);

    List<Account> findAllByIdIn(List<Long> idList);

    List<Account> findAll();
}
