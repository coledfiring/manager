ALTER TABLE talk_group MODIFY COLUMN id varchar(40);
ALTER TABLE talk_group_bulletin MODIFY COLUMN fk_group_id varchar(40);
ALTER TABLE talk_member MODIFY COLUMN fk_group_id varchar(40);