CREATE SCHEMA IF NOT EXISTS invoice;

USE invoice;

CREATE TABLE IF NOT EXISTS app_user (

	user_id       IDENTITY PRIMARY KEY,
	first_name    VARCHAR(50)  NOT NULL,
	last_name     VARCHAR(50)  NOT NULL,
	email         VARCHAR(100) NOT NULL,
	password      VARCHAR(255),
	address       VARCHAR(255),
	phone         VARCHAR(20),
	title         VARCHAR(50),
	description   VARCHAR(255),
	enabled       BOOLEAN DEFAULT FALSE,
	locked        BOOLEAN DEFAULT FALSE,
	use_mfa       BOOLEAN DEFAULT FALSE,
	created_date  DATE DEFAULT CURRENT_DATE,
	
	CONSTRAINT UQ_Email UNIQUE(email)  

);

CREATE TABLE IF NOT EXISTS app_role (
	
	role_id     IDENTITY PRIMARY KEY,
	role_name   VARCHAR(50) NOT NULL,
	permissions VARCHAR(255),
	
	CONSTRAINT UQ_Role_Name UNIQUE(role_name)
	
);

CREATE TABLE IF NOT EXISTS user_role (
	
	user_role_id  IDENTITY PRIMARY KEY,
	user_id       BIGINT NOT NULL,
	role_id       BIGINT NOT NULL,
	
	FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES app_role (role_id) ON DELETE CASCADE,
	
	CONSTRAINT UQ_UserRole_User_Id UNIQUE(user_id)

);

CREATE TABLE IF NOT EXISTS user_event (
	
	event_id         IDENTITY PRIMARY KEY,
	user_id          BIGINT NOT NULL,
	event_type       VARCHAR(100) NOT NULL,
	description      VARCHAR(500),
	created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS verify_acct (
	
	verify_id    IDENTITY PRIMARY KEY,
	user_id      BIGINT NOT NULL,
	verify_url   VARCHAR(255) NOT NULL,
	created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE,
	
	CONSTRAINT UQ_Verify_Acct_Url UNIQUE (verify_url),
	CONSTRAINT UQ_Verify_Acct_User_Id UNIQUE (user_id)

);

CREATE TABLE IF NOT EXISTS verify_reset_pass (
	
	reset_id         IDENTITY PRIMARY KEY,
	user_id          BIGINT NOT NULL,
	reset_url        VARCHAR(255) NOT NULL,
	expiration_date  TIMESTAMP NOT NULL,
	
	FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE,
	
	CONSTRAINT UQ_Rest_Pass_Url UNIQUE (reset_url),
	CONSTRAINT UQ_Reset_Pass_User_Id UNIQUE (user_id)

);

CREATE TABLE IF NOT EXISTS verify_mfa (
	
	mfa_id           IDENTITY PRIMARY KEY,
	user_id          BIGINT NOT NULL,
	mfa_code         VARCHAR(10) NOT NULL,
	expiration_date  TIMESTAMP NOT NULL,
	
	FOREIGN KEY (user_id) REFERENCES app_user (user_id) ON DELETE CASCADE,
	
	CONSTRAINT UQ_Mfa_Code UNIQUE (mfa_code),
	CONSTRAINT UQ_Verify_Mfa_User_Id UNIQUE (user_id)

);

insert into app_user(first_name, last_name, email, password, enabled) 
values ('pop', 'popescu', 'ppopescu@gmail.com', '$2a$10$nb9mQwVv8u41V61OIe0ovue3vtuP/nL6TWpMhmQTb.LzJamC8.VrO', true);

insert into app_user(first_name, last_name, email, password, enabled) 
values ('gigi', 'gigescu', 'ggicescu@outlook.com', '$2a$10$nb9mQwVv8u41V61OIe0ovue3vtuP/nL6TWpMhmQTb.LzJamC8.VrO', true);

insert into user_event(user_id, event_type, description)
values (1, 'LOGIN_SUCCESS', 'login from ip 10.0.0.10 using chrome');

insert into user_event(user_id, event_type, description)
values (1, 'LOGOUT', 'logout from ip 10.0.0.10 using chrome');

insert into app_role (role_name, permissions)
values ('ROLE_USER', 'READ:USER,READ:CUSTOMER');

insert into app_role (role_name, permissions)
values ('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER');

insert into app_role (role_name, permissions)
values ('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER');

insert into app_role (role_name, permissions)
values ('ROLE_SYSADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');

insert into user_role(user_id,role_id) values(1,1);
insert into user_role(user_id,role_id) values(2,3);