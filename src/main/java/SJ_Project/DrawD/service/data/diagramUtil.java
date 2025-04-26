package SJ_Project.DrawD.service.data;

import SJ_Project.DrawD.DTO.dataDTO.responseSelectedDiagramDTO;
import SJ_Project.DrawD.DTO.dataDTO.saveRequestDTO;
import SJ_Project.DrawD.Entity.diagramDataEntity;
import SJ_Project.DrawD.Entity.diagramMetaEntity;
import SJ_Project.DrawD.Entity.diagramMetaId;
import SJ_Project.DrawD.Entity.userEntity;
import SJ_Project.DrawD.service.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class diagramUtil {
    private final userRepository userRepository;
    private final diagramDataRepository diagramDataRepository;
    private final diagramMetaRepository diagramMetaRepository;
    private final materialRepository materialRepository;
    private final JsonSchema jsonSchema;

    public boolean checkValidDiagram(String diagram) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode diagramNode = objectMapper.readTree(diagram);

            Set<ValidationMessage> errors = jsonSchema.validate(diagramNode);

            if(errors.isEmpty()) {
                return true;
            }
            else {
                System.out.println(errors);
                return false;
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void saveDiagramData(String email, saveRequestDTO saveRequest) {
        try {
            diagramDataEntity diagramData = new diagramDataEntity(saveRequest.getUuid(), saveRequest.getDiagram());
            diagramDataRepository.saveData(diagramData);

            userEntity user = userRepository.getUser(email);
            diagramMetaId metaId = new diagramMetaId(saveRequest.getUuid(), email);
            diagramMetaEntity diagramMeta = new diagramMetaEntity(metaId, saveRequest.getCreateDate(), saveRequest.getTitle(), user, diagramData);

            diagramMetaRepository.saveMeta(diagramMeta);
        }
        catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error" + e);
        }
    }

    public void updateDiagramData(String email, saveRequestDTO saveRequest) {
        try {
            diagramDataRepository.updateDiagram(saveRequest.getUuid(), saveRequest.getDiagram());
            diagramMetaRepository.updateMetaData(email, saveRequest.getUuid(), saveRequest.getTitle(), saveRequest.getCreateDate());
        }
        catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error" + e);
        }
    }

    public List<materialData> getSearchMaterial(String partial) {
        partial = partial.replace(" ", "");

        return materialRepository.getMaterialData(partial);
    }

    public List<diagramMetaData> getPastDiagram(String email, int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        return diagramMetaRepository.getDiagram(email, startDate, endDate);
    }

    public responseSelectedDiagramDTO getSelectedDiagram(String email, String diagramId) {
        try {
            if(diagramMetaRepository.existsData(email, diagramId)) {
                return diagramMetaRepository.getTitleAndData(email, diagramId);
            }

            throw new RuntimeException("fail");
        }
        catch (Exception e) {
            System.out.println(e);

            throw new RuntimeException("fail " + e);
        }
    }

    public void deleteDiagram(String diagramId, String email) {
        try {
            diagramDataRepository.deleteDiagram(diagramId);
        }
        catch(Exception e) {
            System.out.println(e);

            throw new RuntimeException("fail" + e);
        }
    }
}
