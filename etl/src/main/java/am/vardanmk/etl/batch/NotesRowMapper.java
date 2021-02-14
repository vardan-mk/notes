package am.vardanmk.etl.batch;

import am.vardanmk.etl.model.Notes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesRowMapper implements RowMapper<Notes> {
        @Override
        public Notes mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notes note = new Notes();
            note.setNoteId(rs.getInt("note_id"));
            note.setUserEmail(rs.getString("user_email"));
            note.setTitle(rs.getString("title"));
            note.setNote(rs.getString("note"));
            note.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            note.setLastUpdateTime(rs.getTimestamp("last_update_time").toLocalDateTime());
            return note;
        }
}
