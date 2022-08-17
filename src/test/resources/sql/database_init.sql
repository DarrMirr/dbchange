create table department (
    id bigint primary key,
    name varchar
);

create table occupation (
    id bigint primary key,
    name varchar
);

create table employee (
    id bigint primary key,
    department_id bigint not null constraint fk_emp2dep references department(id),
    occupation_id bigint not null constraint fk_emp2oc references occupation(id),
    first_name varchar,
    last_name varchar
);