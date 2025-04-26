package SJ_Project.DrawD.DTO.dataDTO;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class saveRequestDTO {
    private final String title;
    private final String uuid;
    private final LocalDateTime createDate;
    private final String diagram;

    public saveRequestDTO(String title, String uuid, String createDate, String diagram) {
        this.title = title;
        this.uuid = uuid;
        this.createDate = LocalDateTime.parse(createDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.diagram = diagram;
    }
}
