package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.materialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface materialJpaRepository extends JpaRepository<materialEntity, String> {
    List<materialData> findByPurposeStartingWithIgnoreCase (String partial);
}
