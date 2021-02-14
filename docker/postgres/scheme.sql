CREATE TABLE usr(
    user_id serial PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
	password varchar(255) NOT NULL,
	role varchar(255) NOT NULL,
    create_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	last_update_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notes(
    note_id serial PRIMARY KEY,
	user_email varchar(255) NOT NULL,
    title varchar(50) NOT NULL,
	note varchar(1000),
    create_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	last_update_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMIT;

