package am.vardanmk.notes.mapper;

import am.vardanmk.notes.domain.Notes;
import am.vardanmk.notes.domain.dto.NotesDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotesMapper {

    NotesDto mapToNotesDto(Notes note);
    Notes mapToNotes(NotesDto noteDto);
}