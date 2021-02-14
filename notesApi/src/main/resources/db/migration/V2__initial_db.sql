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

INSERT INTO usr (email, role, password)
VALUES ('vardanmk@gmail.com', 'ROLE_USER', '12345678');
INSERT INTO usr (email, role, password)
VALUES ('lv-vt@mail.ru', 'ROLE_USER', '23456789');
INSERT INTO usr (email, role, password)
VALUES ('hello.world@yahoo.com', 'ROLE_USER', '34567890');
COMMIT;