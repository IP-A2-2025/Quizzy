-- SQL migration script to fix learningstage column type and ensure constraints are satisfied
-- 1. First, alter the column type with explicit cast
ALTER TABLE flashcardprogress ALTER COLUMN learningstage TYPE integer USING learningstage::integer;

-- 2. Set default value for existing null entries (if any)
UPDATE flashcardprogress SET learningstage = 3 WHERE learningstage IS NULL;

-- 3. Add not null constraint if needed
ALTER TABLE flashcardprogress ALTER COLUMN learningstage SET NOT NULL;

-- 4. Verify the check constraint exists and matches our expectations
-- If the constraint is wrong, we'll need to drop and recreate it
-- Check if constraint exists and what its definition is:
-- SELECT conname, pg_get_constraintdef(oid) FROM pg_constraint WHERE conname = 'flashcardprogress_learningstage_check';

-- 5. If needed, recreate the constraint to accept value 3
-- ALTER TABLE flashcardprogress DROP CONSTRAINT IF EXISTS flashcardprogress_learningstage_check;
-- ALTER TABLE flashcardprogress ADD CONSTRAINT flashcardprogress_learningstage_check CHECK (learningstage >= 3);
