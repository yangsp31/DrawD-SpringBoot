package SJ_Project.DrawD.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagramMeta")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class diagramMetaEntity {
    @EmbeddedId
    private diagramMetaId diagramMetaId;

    @Column(name = "createDate")
    private LocalDateTime createDate;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private userEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("diagramId")
    @JoinColumn(name = "diagramId", referencedColumnName = "diagramId", insertable = false, updatable = false)
    private diagramDataEntity diagramDataEntity;

    public diagramMetaEntity(diagramMetaId diagramMetaId, LocalDateTime createDate, String title, userEntity userEntity, diagramDataEntity diagramDataEntity) {
        this.diagramMetaId = diagramMetaId;
        this.createDate = createDate;
        this.title = title;
        this.userEntity = userEntity;
        this.diagramDataEntity = diagramDataEntity;
    }
}
