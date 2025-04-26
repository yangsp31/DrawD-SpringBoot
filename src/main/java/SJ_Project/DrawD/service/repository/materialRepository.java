package SJ_Project.DrawD.service.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class materialRepository {
    private final materialJpaRepository materialJpaRepository;

    public materialRepository(materialJpaRepository materialJpaRepository) {
        this.materialJpaRepository = materialJpaRepository;
    }

    @Transactional
    public List<materialData> getMaterialData(String partial) {
        return materialJpaRepository.findByPurposeStartingWithIgnoreCase(partial);
    }
}
