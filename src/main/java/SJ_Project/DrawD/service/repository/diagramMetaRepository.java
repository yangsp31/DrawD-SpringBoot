package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.DTO.dataDTO.responseSelectedDiagramDTO;
import SJ_Project.DrawD.Entity.diagramMetaEntity;
import SJ_Project.DrawD.Entity.diagramMetaId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class diagramMetaRepository {
    private final diagramMetaJpaRepository diagramMetaJpaRepository;

    public diagramMetaRepository(diagramMetaJpaRepository diagramMetaJpaRepository) {
        this.diagramMetaJpaRepository = diagramMetaJpaRepository;
    }

    @Transactional
    public void saveMeta(diagramMetaEntity diagramMeta) {
        diagramMetaJpaRepository.save(diagramMeta);
    }

    @Transactional
    public List<diagramMetaData> getDiagram(String email, LocalDateTime startDate, LocalDateTime endDate) {
        return diagramMetaJpaRepository.findByDiagramMetaId_EmailAndCreateDateBetween(email, startDate, endDate);
    }

    @Transactional
    public boolean existsData(String email, String diagramId) {
        diagramMetaId diagramMetaId = new diagramMetaId(email, diagramId);

        return diagramMetaJpaRepository.existsByDiagramMetaId(diagramMetaId);
    }

    @Transactional
    public responseSelectedDiagramDTO getTitleAndData(String email, String diagramId) {
        diagramMetaId diagramMetaId = new diagramMetaId(email, diagramId);

        Optional<diagramData> data = diagramMetaJpaRepository.findDiagramMetaAndDataById(diagramMetaId);

        if(data.isPresent()) {
            return new responseSelectedDiagramDTO(data.get().getTitle(), data.get().getData());
        }

        throw new RuntimeException("fail");
    }

    @Transactional
    public void updateMetaData(String email, String diagramId, String title, LocalDateTime createDate) {
        diagramMetaId diagramMetaId = new diagramMetaId(email, diagramId);

        diagramMetaJpaRepository.updateTitleAndCreateDateByDiagramMetaId(diagramMetaId, title, createDate);
    }
}
