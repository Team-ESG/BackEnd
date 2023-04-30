package esgback.esg.Repository;

import esgback.esg.Domain.ShoppingCart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByMemberId(Long memberId);
}