package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.diagramDataEntity;
import SJ_Project.DrawD.Entity.diagramMetaEntity;
import SJ_Project.DrawD.Entity.diagramMetaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface diagramMetaJpaRepository extends JpaRepository<diagramMetaEntity, diagramMetaId> {
    List<diagramMetaData> findByDiagramMetaId_EmailAndCreateDateBetween(String diagramMetaId_email, LocalDateTime createDate, LocalDateTime createDate2);

    boolean existsByDiagramMetaId(diagramMetaId diagramMetaId);

    @Modifying
    @Transactional
    @Query("UPDATE diagramMetaEntity d SET d.title = CASE WHEN d.title <> :title THEN :title ELSE d.title END, d.createDate = :createDate WHERE d.diagramMetaId = :diagramMetaId")
    void updateTitleAndCreateDateByDiagramMetaId(@Param("diagramMetaId") diagramMetaId diagramMetaId, @Param("title") String title, @Param("createDate") LocalDateTime createDate);

    @Transactional
    @Query("SELECT d.title AS title, dd.data AS data FROM diagramMetaEntity d JOIN d.diagramDataEntity dd WHERE d.diagramMetaId = :diagramMetaId")
    Optional<diagramData> findDiagramMetaAndDataById(@Param("diagramMetaId") diagramMetaId diagramMetaId);
}
