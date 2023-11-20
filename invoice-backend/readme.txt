https://app.diagrams.net/ used to be draw.io
	online tool to create diagrams

Flyway  
	helps maintain the database, keeps a database changes history that allows rollbacks, etc
	Provide the SQL script - create tables, add data in 
	V1_0__init.sql file inside the src/main/resources/db/migration directory
	Flyway naming convention is V<version>__<name>.sql ex V1_0__init.sql
	
Access the H2 database: http://localhost:8080/h2-console/


NamedParameterJdbcTemplate usage

	We are using NamedParameterJdbcTemplate instead of a simple JdbcTemplate
	to be able to provide named parameters instead of generic ? in SQL queries
	thus being less prone to errors
	
	So the :firstName used in the INSERT_USER_QUERY is the same name used 
	when building a MapSqlParameterSource with the addValue call 
	addValue("firstName", appUser.getFirstName() 
	
	private final NamedParameterJdbcTemplate jdbcTmpl;

	private static final String INSERT_USER_QUERY = """
			insert into app_user(first_name, last_name, email)
			values(:firstName, :lastName, :email, :password)
			""";
			
	private SqlParameterSource getSqlParameterSource(AppUser appUser) {
		return new MapSqlParameterSource()
				.addValue("firstName", appUser.getFirstName())
				.addValue("lastName", appUser.getLastName())
				.addValue("email", appUser.getEmail())
				.addValue("password", passEncoder.encode(appUser.getPassword()));
	}
	
	Usage:
		SqlParameterSource params = getSqlParameterSource(appUser);
		jdbcTmpl.update(INSERT_USER_QUERY, params, holder);		
		

DTO pattern ( Data Transfer Object )
	
	Junior here says that it refers to the cases when we do not want/need to pass
	all the members of our model/domain object ( the one which we persist into the 
	database ) to the front-end application.
	
	As the model/domain object might have all kind of info used to implement different
	business cases on the back-end but are not needed in the front-end.
	
	So for example for the AppUser object we might want to create a UserDTO object
	with less/different members to pass to the front-end.
	In our case we do not want to send the password field to front-end.
	
	To create a UserDTO from an AppUser - we need to keep the members we want
	with the same name and call BeanUtils.copyProperties(appUser, userDTO)
	
	!!! More important probably we would add the the UserDTO the information from
	the related tables like app_role.
	
	Q: what happens if we use the domain object which is modeled as a JPA entity
	which has lazy fetch with the related data. Will the default JSON serializer
	call the getters and fill all the possible dependent data ? 
	

How to completely disable Spring security
	
	1.  exclude the auto-configuration of security by Spring Boot by excluding
		it from the Spring Boot application:
		@SpringBootApplication( exclude = {SecurityAutoConfiguration.class} )
		
	2.	exclude it from application.properties
		spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;	
		

How to fix exception: InvalidDataAccessApiUsageException - The current key entry contains multiple keys 

	org.springframework.dao.InvalidDataAccessApiUsageException: The getKey method should only be used when a single key is returned. The current key entry contains multiple keys: [{user_id=3, created_date=2023-11-20}]
	
	It seems Spring cannot/does not infer the primary key from the table metadata of the query	
	
	So in code like this we need to specify the actual key
	
		KeyHolder holder = new GeneratedKeyHolder();
		SqlParameterSource params = getSqlParameterSource(appUser);
		jdbcTmpl.update(INSERT_USER_QUERY, params, holder);
		appUser.setUserId( holder.getKey().longValue() );	
	
	Either do
		jdbcTmpl.update(INSERT_USER_QUERY, params, holder, new String[] { "user_id" });	
	Or get the real key from whatever Spring populates:
		Long newId;
	    if (holder.getKeys().size() > 1) {
	        newId = (Long)holder.getKeys().get("your_id_column");
	    } else {
	        newId= holder.getKey().longValue();
	    }	
		
	
///// Post request

http://localhost:8080/api/v1/users/register

{
    "firstName": "Maria",
    "lastName": "Marinescu",
    "email": "mmarinesc@hotmail.com",
    "password": "sinaia2000"
}	

pass: 90e8d637-0b1a-4193-9787-dd1a64d0cc02	 		


	
	
				 