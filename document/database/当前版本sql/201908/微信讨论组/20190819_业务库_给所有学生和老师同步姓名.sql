update sso_user ss inner join pe_student stu on stu.fk_sso_user_id = ss.id set ss.true_name = stu.true_name;
update sso_user ss inner join pe_teacher stu on stu.fk_sso_user_id = ss.id set ss.true_name = stu.true_name;
