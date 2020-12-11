ALTER TABLE pe_student ADD FOREIGN KEY student_sso_user_fk(fk_sso_user_id) REFERENCES sso_user(id);
ALTER TABLE pe_teacher ADD FOREIGN KEY teacher_sso_user_fk(fk_sso_user_id) REFERENCES sso_user(id);