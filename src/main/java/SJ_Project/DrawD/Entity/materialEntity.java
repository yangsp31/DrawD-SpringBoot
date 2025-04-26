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
@Table(name = "material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class materialEntity {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "highvalue")
    private String highValue;

    @Column(name = "lowvalue")
    private String lowValue;

    @Builder(toBuilder = true)
    public materialEntity(String name, String purpose, String highValue, String lowValue) {
        this.name = name;
        this.purpose = purpose;
        this.highValue = highValue;
        this.lowValue = lowValue;
    }
}
