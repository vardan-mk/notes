package am.vardanmk.notes.service.impl;

import am.vardanmk.notes.domain.dto.NotesDto;
import am.vardanmk.notes.exception.BadRequestException;
import am.vardanmk.notes.exception.NotFoundException;
import am.vardanmk.notes.mapper.NotesMapper;
import am.vardanmk.notes.repository.NotesRepository;
import am.vardanmk.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;
    private final NotesMapper mapper;

    @Autowired
    public NotesServiceImpl(NotesRepository notesRepo, NotesMapper mapper) {

        this.notesRepo = notesRepo;
        this.mapper = mapper;
    }


    @Override
    public Mono<NotesDto> getUserNote(String userEmail, long noteId) {
        return notesRepo.findByNoteIdAndUserEmail(noteId, userEmail)
                        .map(mapper::mapToNotesDto)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Flux<NotesDto> getAllUserNotes(String userEmail) {
        return notesRepo.findNotesByUserEmail(userEmail)
                        .map(mapper::mapToNotesDto)
                        .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<NotesDto> saveUserNote(String userEmail, NotesDto note) {

        if (note.getTitle() == null || note.getTitle().length() > 50 || note.getNote().length() > 1000)
            return Mono.error(new BadRequestException("Title can't be null or greater then 50 chars, or your note is longer then 1000"));

        note.setUserEmail(userEmail);

        return notesRepo.save(mapper.mapToNotes(note))
                        .map(mapper::mapToNotesDto)
                        .switchIfEmpty(Mono.error(new BadRequestException()));

    }

    @Override
    public Mono<NotesDto> updateUserNote(String userEmail, long noteId, NotesDto noteDto) {

        return notesRepo.findByNoteIdAndUserEmail(noteId, userEmail)
                        .flatMap(n -> {n.setNote(noteDto.getNote() == null ? n.getNote() : noteDto.getNote());
                                       n.setTitle(noteDto.getTitle() == null ? n.getTitle() : noteDto.getTitle());
                                       n.setLastUpdateTime(LocalDateTime.now());
                                       n.setUserEmail(n.getUserEmail());
                                       return notesRepo.save(n).map(mapper::mapToNotesDto);})
                        .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<NotesDto> deleteUserNote(String userEmail, long noteId) {

        return  notesRepo.findByNoteIdAndUserEmail(noteId, userEmail)
                         .flatMap(note -> notesRepo.delete(note).thenReturn(note)
                         .map(mapper::mapToNotesDto)
                         .switchIfEmpty(Mono.error(new NotFoundException())));
    }
}