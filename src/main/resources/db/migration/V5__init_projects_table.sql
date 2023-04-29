create table projects(
                      id long primary key auto_increment,
                      description varchar(100) not null
);
create table projects_steps(
                         id long primary key auto_increment,
                         description varchar(100) not null,
                         days_to_deadline int not null,
                         project_id long not null,
                         foreign key (project_id) references projects (id)
);
alter table TASK_GROUPS add column project_id long null;
alter table TASK_GROUPS add foreign key (project_id) references projects (id);
