package com.portfolio.apple.domain.orders;

import com.portfolio.apple.domain.account.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("select o from Orders o join fetch o.delivery where o.id = :ordersId and o.userAccount.id = :#{#userAccount.id}")
    Optional<Orders> findOrdersWithDeliveryByIdAndAccount(@Param("ordersId") Long ordersId, @Param("userAccount") UserAccount userAccount);

    List<Orders> findOrdersWithDeliveryByUserAccount(UserAccount userAccount);
}
