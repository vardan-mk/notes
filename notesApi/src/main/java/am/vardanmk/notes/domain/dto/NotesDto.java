package am.vardanmk.notes.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotesDto {

    private long noteId;

    private String userEmail;

    private String title;

    private String note;

    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
}
