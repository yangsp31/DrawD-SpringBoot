package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.diagramDataEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class diagramDataRepository {
    private final diagramDataJpaRepository diagramDataJpaRepository;

    public diagramDataRepository(diagramDataJpaRepository diagramDataJpaRepository) {
        this.diagramDataJpaRepository = diagramDataJpaRepository;
    }

    @Transactional
    public void saveData(diagramDataEntity diagramData) {
        diagramDataJpaRepository.save(diagramData);
    }

    @Transactional
    public void updateDiagram(String diagramId, String data) {
        diagramDataJpaRepository.updateDataByDiagramId(diagramId, data);
    }

    @Transactional
    public void deleteDiagram(String diagramId) {
        diagramDataJpaRepository.deleteById(diagramId);
    }
}
