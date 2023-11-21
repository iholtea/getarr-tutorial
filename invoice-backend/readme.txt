https://app.diagrams.net/ used to be draw.io
	online tool to create diagrams

Flyway  
	helps maintain the database, keeps a database changes history that allows rollbacks, etc
	Provide the SQL script - create tables, add data in 
	V1_0__init.sql file inside the src/main/resources/db/migration directory
	Flyway naming convention is V<version>__<name>.sql ex V1_0__init.sql
	
Access the H2 database: http://localhost:8080/h2-console/

After authentication to h2-console it sends a session_id
http://localhost:8080/h2-console/login.do?jsessionid=88aa7391f6eaf7ddb76fcd78e0062576
(this might or might not have stuff to do with spring security and allowing
access to h2-console by allowing/excepting it from security rules of the application
as the h2-console is a sub-domain of our application) 


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


Validation :
	
	in our domain/model classes we have:
	@NotEmpty(message = "First name cannot be empty")
	private String firstName;
	
	The in our API resource if we use @Valid on the AppUser object
	@PostMapping("/register")
	public ResponseEntity<AppUser> saveUser(@RequestBody @Valid AppUser appUser) {
	
	If the firstName of the the AppUser created by Spring from the POST data
	would be empty, some exception would be thrown.		


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
	    
	    
Spring Security:
	
	Requests -> Security filters -> MVC/REST Controllers ( via DispatcherServlet )
	
	Adding the spring-security dependencies to the project classpath will trigger 
	SprinBoot to auto-configure security filters to intercept requests.
	
	UsernamePasswordAuthenticationFileter ->
		Authentication Manager( ProviderManager ) ->
			Authentication Provider ( DaoAuthenticationProvider ) ->
				User Details Service ( InMemoryUserDetailService )
				
To authorize a request we may use either a Token or a SesssionCookie and add it to each request.

Spring Security Configuration - create a class

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

CSRF: Cross-Site Request Forgery
	it seems to mean that an attacker sends you a link to access a site you are already
	logged in. As such, the browser will attach the authentication cookie/token and the 
	request will be successful. That request contains data ( HTTP GET since there's 
	just a link ) to perform some action on the server that the attacker will benefit.
	
	// disables CSRF for all requests
	http.csrf().disable()  
	
	// probably disables CSRF just for the /h2-console path
	csrf.ignoringRequestMatchers(PathRequest.toH2Console()).disable()
	
CORS: Cross-origin resource sharing
	is a mechanism for integrating applications. 
	CORS defines a way for client web applications that are loaded in one domain 
	to interact with resources in a different domain.
	
	// probably disables CORS protection, allowing access from all domains
	http.cors().disable()				
			
Some stuff relate to h2-console application
	
	http.authorizeHttpRequests( auth -> 
		//auth.requestMatchers(ALLOWED_URLS).permitAll()
		auth.requestMatchers(PathRequest.toH2Console()).permitAll()
	);
	
	Method requestMatcher() may get an array of String or RequestMatcher objects
	requestMatchers(RequestMatcher... requestMatchers)
	requestMatchers(String... patterns)		
	
	If we use the String[] method in conjunction with having h2-console I get an exception
	More than one mappable servlet in your servlet context: 
	{org.h2.server.web.JakartaWebServlet=[/h2-console/*], 
	org.springframework.web.servlet.DispatcherServlet=[/]}.
	
	It suggests to use the RequestMatcher[] method.
	
	When doing so, after logging into the h2-console the content of the application
	is not displayed - browser says localhost refused to connect
	( what is in fact the h2-console app as it looks like a java applet ? )
	
	Adding this to SecurityConfiguration. TODO - what is this
	http.headers( headers -> headers.frameOptions(FrameOptionsConfig::disable) );
	
Note on error on cycles in dependency injection
	Relying upon circular references is discouraged and they are prohibited by default. 
	Update your application to remove the dependency cycle between beans. 
	As a last resort, it may be possible to break the cycle automatically 
	by setting spring.main.allow-circular-references to true.
		
		
	
///// Post request

add user:

	http://localhost:8080/api/v1/users/register

	{
	    "firstName": "Maria",
	    "lastName": "Marinescu",
	    "email": "mmarinescu@hotmail.com",
	    "password": "123456"
	}

login:

	http://localhost:8080/api/v1/users/login
	
	{
    	"email": "ppopescu@gmail.com",
    	"password": "123456"
	}

		


	
	
				 