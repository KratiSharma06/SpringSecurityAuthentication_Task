package SpringSecurityAuthentication_usingInmemory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

@WicketSignInPage
public class LoginPage extends WebPage {

	public LoginPage(PageParameters parameters) {
		super(parameters);

		if (((AbstractAuthenticatedWebSession) getSession()).isSignedIn()) {
			continueToOriginalDestination();
		}
		add(new LoginForm("loginForm"));
	}

	private class LoginForm extends Form<LoginForm> {

		private String username;
		
		private String password;

		public LoginForm(String id) {
			super(id);
			setModel(new CompoundPropertyModel<>(this));
			add(new FeedbackPanel("feedback"));
			add(new RequiredTextField<String>("username"));
			add(new PasswordTextField("password"));
		}

		@Override
		protected void onSubmit() {
			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(username, password)) {
//--------------------------------------------------------------------------------------------------------------------
				try
				   {   
					String url = "jdbc:mysql://localhost:3306/registration?autoReconnect=true&useSSL=false"; 
				    String uname = "root"; // MySQL credentials
				    String pword = "Root";

				    Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
				    Connection con = DriverManager.getConnection(url, uname, pword);
				    System.out.println("Connected with database successfully");
				    Statement st = (Statement) con.createStatement();
				    
				    session.signIn(username, password);
				    String a = username; 

				    String query = "select * from user where username = '" + a + "'";
				    ResultSet rs= st.executeQuery(query); 
				    rs.next();
				    
				    String name= rs.getString("username");
				    String password= rs.getString("password");
				    String authorities= rs.getString("authorities");
				    System.out.println("Entered Login Username : " +name+" "+"Password : "+password+" "+"Authorities : "+authorities);
		
				    con.close(); 
				    System.out.println("Details completed successfully....");
				    
					}
				
						catch(Exception e)
				
					    {
					        e.printStackTrace();
					    }
//--------------------------------------------------------------------------------------------------------------------------
				setResponsePage(HomePage.class);
			} else {
				error("Login failed");
			}
		}
	}
}
