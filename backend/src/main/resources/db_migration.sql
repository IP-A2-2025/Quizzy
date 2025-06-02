-- SQL migration script to fix learningstage column type
ALTER TABLE flashcardprogress ALTER COLUMN learningstage TYPE integer USING learningstage::integer;
