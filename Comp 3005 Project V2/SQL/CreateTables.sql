create table users(
        user_email      varchar(255) not null unique,
        first_name      varchar(255) not null,
        last_name       varchar(255) not null,
        account_type    varchar(255) not null,
        sex             varchar(1) not null,
        password        varchar(255) not null,
        signup_date     date default now(),
	 primary key (user_email)
	);

create table fitness_goals(
        goal_id         serial,
        user_email      varchar(255) not null,
        goal            text,
        start_date      date default now(),
        completion_date date,
	 primary key (goal_id),
         foreign key (user_email) references users
	);
create table exercise_routines(
        routine_id         serial,
        user_email      varchar(255) not null,
        routine            text,
        start_date      date default now(),
	primary key (routine_id),
         foreign key (user_email) references users
	);

create table health_metrics(
        metric_id		serial,
        user_email      varchar(255) not null,
        weight          decimal,
        height          decimal,
        heart_rate      int,
        bmi             decimal,
        input_date      date default now(),
	 primary key (metric_id),
	 foreign key (user_email) references users
	);

create table room(
        room_num        integer not null unique,
        name      varchar(255) not null,
         primary key (room_num)
        );

create table session(
        session_id		serial,
        user_email      varchar(255) not null,
        max_size        int,
        date            date,
        start_time      time,
        end_time        time,
        room_num        int not null,
        cost            decimal,
	 primary key (session_id),
	 foreign key (user_email) references users,
         foreign key (room_num) references room
	);

create table session_members(
        session_members_id      serial,
        session_id		int,
        user_email              varchar(255) not null,
        paid                    boolean default false,
         primary key (session_members_id),
         foreign key (session_id) references session,
	 foreign key (user_email) references users
	);

create table equipment(
        equipment_id		serial,
        name    varchar(255) not null,
        open                  boolean default false,
        room_num                    int not null,
         primary key (equipment_id),
         foreign key (room_num) references room
	);

create table equipment_maintenance(
        equipment_maintenance_id   serial,
        equipment_id		integer,
        comments                  text,
        date     date default now(),
        user_email      varchar(255) not null,
         primary key (equipment_maintenance_id),
         foreign key (equipment_id) references equipment,
         foreign key (user_email) references users
        );
