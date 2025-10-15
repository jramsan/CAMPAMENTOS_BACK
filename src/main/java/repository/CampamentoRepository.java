package repository;

import entity.Campamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampamentoRepository extends JpaRepository<Campamento, Integer> {
}

