package am.vardanmk.notes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(value = "notes")
public class Notes {
    @Id
    private long noteId;

    private String userEmail;

    private String title;

    private String note;

    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
