https://app.diagrams.net/ used to be draw.io
	online tool to create diagrams

Flyway  
	helps maintain the database, keeps a database changes history that allows rollbacks, etc
	Provide the SQL script - create tables, add data in 
	V1_0__init.sql file inside the src/main/resources/db/migration directory
	Flyway naming convention is V<version>__<name>.sql ex V1_0__init.sql
	
Access the H2 database: http://localhost:8080/h2-console/

insert into app_user(first_name, last_name, email ) 
values( 'pop', 'popescu', 'ppopescu@gmail.com');

insert into app_user(first_name, last_name, email ) 
values( 'gigi', 'gigescu', 'ggicescu@outlook.com');


	
	
				 