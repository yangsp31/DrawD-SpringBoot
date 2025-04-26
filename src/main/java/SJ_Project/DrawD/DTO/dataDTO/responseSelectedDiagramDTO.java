package SJ_Project.DrawD.DTO.dataDTO;

import lombok.Getter;

@Getter
public class responseSelectedDiagramDTO {
    private final String title;
    private final String diagramData;

    public responseSelectedDiagramDTO(String title, String diagramData) {
        this.title = title;
        this.diagramData = diagramData;
    }
}
