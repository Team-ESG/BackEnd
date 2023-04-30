package esgback.esg.Repository;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.ShoppingCart.ShoppingCartReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShoppingCartReserveRepository extends JpaRepository<ShoppingCartReserve, Long> {
    List<ShoppingCartReserve> findByReservedStateAndReserveDateBefore(State state, LocalDateTime time);
}