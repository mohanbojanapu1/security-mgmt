## Following steps to run the project

-mvn clean install

-mvn package spring-boot:repackage

-java -jar target/SecureId-Web-Java-0.0.1-SNAPSHOT.jar


## API documentation URI:
    http://{domainUrl}/secure-id-web-java/swagger-ui.html
    
## Package Structure
 - Package name for Application Configuration: com.grit.secureid
 - Package name for Other configurations: com.grit.secureid.config
 - Package name for REST controllers: com.grit.secureid.controller
 - Package name for Services: com.grit.secureid.service
 - Package name for Repositories: com.grit.secureid.repository
 - Package name for DAO classes: com.grit.secureid.dao
 - Package name for DTO classes: com.grit.secureid.dto
 - Package name for UTL classes: com.grit.secureid.util
 
 
## Captcha Integration for Web application:
-Create servlet class in project

-Configure Servlet name in Web.xml

```xml
<servlet>
        <servlet-name>Captcha</servlet-name>
        <servlet-class>com.grit.bfl.sales.captcha.GritCaptchaServlet</servlet-class>
        <init-param>
            <param-name>width</param-name>
            <param-value>250</param-value>
        </init-param>
        <init-param>
            <param-name>height</param-name>
            <param-value>75</param-value>
        </init-param>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>Captcha</servlet-name>
        <url-pattern>/captcha</url-pattern>
    </servlet-mapping>
```

-Include security-mgmt-0.0.1-SNAPSHOT.jar and simplecaptcha-1.2.1.jar file in WEB-INF/lib folder

-import com.grit.secureid.captcha/GritCaptcha class and call writeImage method in our servlet class it return image

    GritCaptcha captcha = new GritCaptcha();
    captcha.writeImage(req, resp, COLORS, FONTS, _width, _height);

-Display captcha image in html/jsp pages

```html

<div class="captcha-card">
			<div class="captcha-card-body">
				<img src="captcha" id="captcha" />
				<img src="images/refreshing.png" onclick="reloadCaptcha()" alt="Reload" id="refresh-button"/>
			</div>
			  	
		</div>
		<div id="secondblockgap"></div> 
		<div id="userdetails">
			<input type="text" name="answer" id="answer"
				placeholder="Image Text" autocomplete='off' class="rounded text-box"/>
		</div>
```

-Validate input text and image text by calling validateCaptcha method.

    String captchaName = request.getParameter("answer");
    GritCaptcha captcha = new GritCaptcha();
    boolean flag = captcha.validateCaptcha(request, captchaName);
    
## CSRF Token Implementation

-Create LoadToken and ValidateToken filters.

-Configure filters name in Web.xml.

```xml
	<filter>
		<filter-name>LoadToken</filter-name>
		<filter-class>com.XXX.LoadToken</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>LoadToken</filter-name>
		<url-pattern>*.do </url-pattern>
	</filter-mapping>
	
	<filter-mapping>
		<filter-name>LoadToken</filter-name>
		<url-pattern>/login.jsp</url-pattern>
	</filter-mapping>

	<filter>
		<filter-name>ValidateToken</filter-name>
		<filter-class>com.XXX.ValidateToken</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>ValidateToken</filter-name>
		<url-pattern>*.do</url-pattern>
	</filter-mapping>
```
-Include hidden field in jsp/html file 

```html
	<input type="hidden" name="csrfPreventionToken" id="aesphassphrase" value="${csrfPreventionToken}" />
```

-Include security-mgmt jar file in WEB-INF/lib folder

-Import com.grit.secureid.csrfToken.GritCsrfToken package exits in security-mgmt jar file. 

-To Generate CSRT token Create GritCsrfToken class and call generateCsrfToken method.

	LoadToken.java
	GritCsrfToken csrfObject = new GritCsrfToken();
	csrfObject.generateCsrfToken(httpReq, token);

-To Validate CSRF token Create GritCsrfToken class and call validateToken method.

	ValidateToken.java
	GritCsrfToken csrfObject = new GritCsrfToken();
	boolean flag = csrfObject.validateToken(httpReq, token);
	
	