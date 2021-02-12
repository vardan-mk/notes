package am.vardanmk.notes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(value = "notes")
public class Notes {
    @Id
    private long noteId;

    private String userEmail;

    // max 50 characters
    private String title;

    // max 1000 characters
    private String note;

    private LocalDate createTime;
    private LocalDateTime lastUpdateTime;
}
