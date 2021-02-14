package am.vardanmk.etl.batch;

import am.vardanmk.etl.model.Notes;
import org.springframework.batch.item.ItemProcessor;

public class NoteItemProcessor implements ItemProcessor<Notes, Notes> {

    @Override
    public Notes process(Notes note) throws Exception {
        return note;
    }
}