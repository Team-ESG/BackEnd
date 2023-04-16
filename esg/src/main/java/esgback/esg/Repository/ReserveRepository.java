package esgback.esg.Repository;

import esgback.esg.Domain.Reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}