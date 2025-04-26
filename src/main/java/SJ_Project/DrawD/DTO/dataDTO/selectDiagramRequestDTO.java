package SJ_Project.DrawD.DTO.dataDTO;

import lombok.Getter;

@Getter
public class selectDiagramRequestDTO {
    private final String uuid;

    public selectDiagramRequestDTO(String uuid) {
        this.uuid = uuid;
    }
}
