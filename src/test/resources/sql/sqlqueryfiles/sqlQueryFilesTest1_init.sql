insert into department(id, name) values (7, 'dep7');
insert into occupation(id, name) values (1, 'occ1');
-- test query with comment and multiline query
insert into employee(id, department_id, occupation_id, first_name, last_name)
values (1, 7, 1, 'Ivan', 'Ivanov');