CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS public.users (
    id bigint PRIMARY KEY DEFAULT nextval('users_id_seq'),
    username varchar(32) NOT NULL UNIQUE,
    password varchar(128) NOT NULL,
    name varchar(32) NOT NULL
);

CREATE SEQUENCE user_authority_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS public.user_authority (
    id bigint PRIMARY KEY DEFAULT nextval('user_authority_id_seq'),
    user_id bigint NOT NULL REFERENCES users(id),
    role varchar(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.deactivated_token (
    id uuid PRIMARY KEY,
    keep_until timestamp with time zone NOT NULL
);

CREATE SEQUENCE project_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE project(
    id bigint PRIMARY KEY DEFAULT nextval('project_id_seq'),
    name varchar(255) NOT NULL UNIQUE
);

CREATE SEQUENCE test_case_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_case(
    id bigint PRIMARY KEY DEFAULT nextval('test_case_id_seq'),
    name varchar(255) NOT NULL,
    state varchar(20),
    precondition text,
    requirement_id bigint,
    project_id bigint
);

CREATE SEQUENCE test_case_step_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_case_step(
    id bigint PRIMARY KEY DEFAULT nextval('test_case_step_id_seq'),
    test_data text,
    description text,
    expected_result text,
    test_case_id bigint NOT NULL
);

CREATE SEQUENCE test_cycle_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_cycle(
    id bigint PRIMARY KEY DEFAULT nextval('test_cycle_id_seq'),
    name varchar(255) NOT NULL,
    description text,
    assignee_id bigint,
    project_id bigint,
    state varchar(20) NOT NULL
);

CREATE SEQUENCE test_case_x_cycle_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_case_x_cycle(
    id bigint PRIMARY KEY DEFAULT nextval('test_case_x_cycle_id_seq'),
    test_case_id bigint NOT NULL,
    test_cycle_id bigint NOT NULL
);

CREATE SEQUENCE test_case_execution_history_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_case_execution_history(
    id bigint PRIMARY KEY DEFAULT nextval('test_case_execution_history_id_seq'),
    user_id bigint,
    execution_date_time timestamp with time zone,
    result text,
    test_case_id bigint NOT NULL,
    state varchar(20) NOT NULL
);

CREATE SEQUENCE test_cycle_execution_history_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_cycle_execution_history(
    id bigint PRIMARY KEY DEFAULT nextval('test_cycle_execution_history_id_seq'),
    test_cycle_id bigint NOT NULL,
    user_id bigint,
    execution_date_time timestamp with time zone,
    tests_passed integer,
    tests_fail integer,
    tests_not_executed integer,
    tests_skipped integer,
    tests_blocked integer
);

CREATE SEQUENCE test_plan_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_plan(
    id bigint PRIMARY KEY DEFAULT nextval('test_plan_id_seq'),
    name varchar(255) NOT NULL,
    description text,
    assignee_id bigint NOT NULL,
    state varchar(20) NOT NULL,
    release_id bigint NOT NULL,
    specification_id bigint
);

CREATE SEQUENCE test_plan_x_cycle_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE test_plan_x_cycle(
    id bigint PRIMARY KEY DEFAULT nextval('test_plan_x_cycle_id_seq'),
    test_cycle_id bigint NOT NULL,
    test_plan_id bigint NOT NULL
);

CREATE SEQUENCE release_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE release(
    id bigint PRIMARY KEY DEFAULT nextval('release_id_seq'),
    name varchar(255) NOT NULL,
    description text,
    project_id bigint NOT NULL
);

ALTER TABLE
    test_case_x_cycle ADD CONSTRAINT test_case_x_cycle_test_case_id_foreign FOREIGN KEY(test_case_id) REFERENCES test_case(id);
ALTER TABLE
    test_case_step ADD CONSTRAINT test_case_step_test_case_id_foreign FOREIGN KEY(test_case_id) REFERENCES test_case(id);
ALTER TABLE
    test_case_execution_history ADD CONSTRAINT test_case_execution_history_user_id_foreign FOREIGN KEY(user_id) REFERENCES users(id);
ALTER TABLE
    test_cycle_execution_history ADD CONSTRAINT test_cycle_execution_history_test_cycle_id_foreign FOREIGN KEY(test_cycle_id) REFERENCES test_cycle(id);
ALTER TABLE
    test_case_execution_history ADD CONSTRAINT test_case_execution_history_test_case_id_foreign FOREIGN KEY(test_case_id) REFERENCES test_case(id);
ALTER TABLE
    test_case ADD CONSTRAINT test_case_project_id_foreign FOREIGN KEY(project_id) REFERENCES project(id);
ALTER TABLE
    test_plan ADD CONSTRAINT test_plan_release_id_foreign FOREIGN KEY(release_id) REFERENCES release(id);
ALTER TABLE
    test_case_x_cycle ADD CONSTRAINT test_case_x_cycle_test_cycle_id_foreign FOREIGN KEY(test_cycle_id) REFERENCES test_cycle(id);
ALTER TABLE
    test_plan_x_cycle ADD CONSTRAINT test_plan_x_cycle_test_plan_id_foreign FOREIGN KEY(test_plan_id) REFERENCES test_plan(id);
ALTER TABLE
    test_cycle ADD CONSTRAINT test_cycle_assignee_id_foreign FOREIGN KEY(assignee_id) REFERENCES users(id);
ALTER TABLE
    test_cycle ADD CONSTRAINT test_cycle_project_id_foreign FOREIGN KEY(project_id) REFERENCES project(id);
ALTER TABLE
    test_cycle ADD CONSTRAINT test_cycle_id_foreign FOREIGN KEY(id) REFERENCES test_plan_x_cycle(id);
ALTER TABLE
    release ADD CONSTRAINT release_project_id_foreign FOREIGN KEY(project_id) REFERENCES project(id);
ALTER TABLE
    test_cycle_execution_history ADD CONSTRAINT test_cycle_execution_history_user_id_foreign FOREIGN KEY(user_id) REFERENCES users(id);