-- Create a table to bind together URI parts
CREATE TABLE short_url (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	scheme_id BIGINT REFERENCES scheme(id) NOT NULL,
	authority_id BIGINT REFERENCES authority(id) NOT NULL,
	relative_path_id BIGINT REFERENCES relative_path(id) NOT NULL
);