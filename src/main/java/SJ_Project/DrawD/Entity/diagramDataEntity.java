package SJ_Project.DrawD.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagramData")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class diagramDataEntity {
    @Id
    @Column(name = "diagramId")
    private String diagramId;

    @Column(name = "data")
    private String data;

    @Builder(toBuilder = true)
    public diagramDataEntity(String diagramId, String data) {
        this.diagramId = diagramId;
        this.data = data;
    }
}
