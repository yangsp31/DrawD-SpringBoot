package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.diagramDataEntity;
import SJ_Project.DrawD.Entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface diagramDataJpaRepository extends JpaRepository<diagramDataEntity, String> {
    @Modifying
    @Transactional
    @Query("UPDATE diagramDataEntity d SET d.data = :data WHERE d.diagramId = :diagramId")
    void updateDataByDiagramId(@Param("diagramId") String diagramId, @Param("data") String data);
}
