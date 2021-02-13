package am.vardanmk.etl.batch;


import am.vardanmk.etl.model.Notes;
import am.vardanmk.etl.repo.NotesRepo;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NotesItemReader implements ItemReader<Notes> {

    private NotesRepo notesRepo;
    private int nextNoteIndex;
    private List<Notes> notesList;

    @Autowired
    public void setNotesRepository(NotesRepo notesRepo) {
        this.notesRepo = notesRepo;
    }

    @Override
    public Notes read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Notes nextNote = null;

        this.notesList = notesRepo.findAll().collectList().block();
        if (nextNoteIndex < notesList.size()) {
            nextNote = notesList.get(nextNoteIndex);
            nextNoteIndex++;
        }

        return nextNote;
    }

    @BeforeStep
    public void getNotesList() {
        this.notesList = notesRepo.findAll().collectList().block();
    }
}
