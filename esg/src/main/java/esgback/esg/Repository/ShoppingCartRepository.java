package esgback.esg.Repository;

import esgback.esg.Domain.ShoppingCart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByMemberId(Long MemberId);
}