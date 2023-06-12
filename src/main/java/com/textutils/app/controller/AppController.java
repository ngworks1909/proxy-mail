package com.textutils.app.controller;


import java.sql.ResultSet;
import java.sql.SQLException;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



import com.mashape.unirest.http.exceptions.UnirestException;
import com.textutils.app.entities.ChangeEmail;
import com.textutils.app.entities.Email;
import com.textutils.app.entities.Otp;
import com.textutils.app.entities.Password;
import com.textutils.app.entities.User;

@Controller
public class AppController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	private User newuser;

	public User getNewuser() {
		return newuser;
	}

	public void setNewuser(User newuser) {
		this.newuser = newuser;
	}

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private String validemail;
	

	public String getValidemail() {
		return validemail;
	}

	public void setValidemail(String validemail) {
		this.validemail = validemail;
	}

	@GetMapping("/")
	public ModelAndView homeGet(ModelAndView modelAndView)
	{
		modelAndView.setViewName("index");
		return modelAndView;
	}

    @GetMapping("/Login")
	public ModelAndView LogGet(ModelAndView modelAndView,User user)
	{
		System.out.println("Login called");
		// modelAndView.addObject("iserror",false);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping("/Register")
	public ModelAndView RegGet(ModelAndView modelAndView,User user)
	{
		// modelAndView.addObject("iserror",false);
		
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;
    }

    @PostMapping("/Login")
	public ModelAndView loginUser(ModelAndView modelAndView, User user){
		System.out.println("post login");
      String email=user.getEmailId();
	  String password=user.getPassword();

	  String sql="SELECT COUNT(*) FROM textusers WHERE emailId=?";
	  int count =  jdbcTemplate.queryForObject(sql, Integer.class, email);

	  
	  if(count>0){
		sql="SELECT * FROM textusers WHERE emailId='"+email+"'";
		

		jdbcTemplate.query(sql, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet resultSet, int i) throws SQLException {
				String passwordentered = resultSet.getString("password");
				
				if(passwordentered.equals(password)){

					
					modelAndView.setViewName("redirect:/Email");
				
				}
				else{
					modelAndView.addObject("iserror",true);
					setMsg("Invalid username or password!");
					modelAndView.addObject("msg", getMsg());
					modelAndView.setViewName("login");
				}

				return null;
			}
		});
		
		
		
	  }
	  else{
		modelAndView.addObject("iserror",true);
		setMsg("User doesn't exist!");
		modelAndView.addObject("msg", getMsg());
		modelAndView.setViewName("login");
	  }

	  
	  

		return modelAndView;
	}

	@PostMapping("/Register")
	public ModelAndView registerUser(ModelAndView modelAndView, User user) {
		String email=user.getEmailId();
		
		
		System.out.println(email);
		   String sql = "SELECT COUNT(*) FROM textusers WHERE emailId=?";
		   int count =  jdbcTemplate.queryForObject(sql, Integer.class, email);
		if(count>0) {
			
			modelAndView.addObject("iserror",true);
			setMsg("The email already exists!");
			modelAndView.addObject("msg",getMsg());
			modelAndView.setViewName("register");
		} 
		else{
			 
			
				// modelAndView.addObject("iserror",false);
				// setMsg("Account Registered Successfully");
			    this.setNewuser(user);
				setValidemail(email);
				modelAndView.addObject("msg",getMsg());
				modelAndView.setViewName("redirect:/Otp");
			
			
		}
		
		 
	   
		
		return modelAndView;		
	}

	@GetMapping("/Email")
	public ModelAndView loginHome(ModelAndView modelAndView,Email sendemail){
	

		// modelAndView.addObject("iserror",false);
		modelAndView.addObject("iserror",false);
		modelAndView.addObject("msg", "Login successful!");
		
		modelAndView.addObject("sendemail",sendemail);
		modelAndView.setViewName("email");
		return modelAndView;
	}

	Otp newotp=new Otp();
	@GetMapping("/Otp")
	public ModelAndView setOtp(ModelAndView modelAndView,Otp otp){
	
		Email sendemail=new Email();
		String sourceemail=RandomStringUtils.randomAlphanumeric(10)+"@gmail.com";
		String value=RandomStringUtils.randomNumeric(4);
		sendemail.setSource(sourceemail);
		sendemail.setDestination(getValidemail());
		sendemail.setText("The otp for pmail : "+value);
		sendemail.setSubject("Otp");
		newotp.setOtp(value);
		TextServiceImpl ts=new TextServiceImpl(); 
		ts.setApikey("f08f847f23mshebd48ab6414263bp1f804cjsn1f81c01384a8");
		ts.sendMail(sendemail);
		modelAndView.addObject(otp);
		modelAndView.setViewName("otp");
		return modelAndView;
	}

	@PostMapping("/Email")
	public ModelAndView convertText(ModelAndView modelAndView,Email sendemail) throws UnirestException{
		

		String sourceemail=RandomStringUtils.randomAlphanumeric(10)+"@gmail.com";
		sendemail.setSource(sourceemail);
		TextServiceImpl ts=new TextServiceImpl(); 
		ts.setApikey("5c192f32f1mshfb1d57b40e0417bp1a11b4jsn7dce7b721ab6");
		if(sendemail.getLanguage().equals("en")){
          ts.sendMail(sendemail);
		}
		else{
			sendemail.setText(ts.translate(sendemail.getText(),sendemail.getLanguage()));
			ts.sendMail(sendemail);
		}
		modelAndView.setViewName("success");
		return modelAndView;
	}

	

	@PostMapping("/Otp")
	public ModelAndView sendOtp(ModelAndView modelAndView,Otp otp) throws UnirestException{
		
		otp.setOtp(otp.getV1()+otp.getV2()+otp.getV3()+otp.getV4());

		System.out.println("the otp for register : "+otp.getOtp());

		if(otp.getOtp().equals(newotp.getOtp())){
			String sql="INSERT INTO textusers(name,emailId,password) VALUES(?,?,?)";
		    jdbcTemplate.update(sql,newuser.getName(),newuser.getEmailId(),newuser.getPassword());
			modelAndView.addObject("iserror",false);
		    modelAndView.addObject("msg", "Account Registered Successfully");
			modelAndView.setViewName("redirect:/Login");
		}
		else{

			modelAndView.addObject("iserror",true);
		    modelAndView.addObject("msg", "Otp Invalid");
			modelAndView.setViewName("otp");
		}
		
		return modelAndView;
	}

	@GetMapping("/Success")
	public ModelAndView success(ModelAndView modelAndView){
	
		modelAndView.setViewName("success");
		return modelAndView;
	}

	@GetMapping("/Forgot")
	public ModelAndView forgotpassword(ModelAndView modelAndView,ChangeEmail mail){
	
		modelAndView.addObject("mail", mail);
		modelAndView.setViewName("forgot");
		return modelAndView;
	}

	@PostMapping("/Forgot")
	public ModelAndView changepassword(ModelAndView modelAndView,ChangeEmail mail){

		String email=mail.getEmail();

        System.out.println("The email to change is : "+email);

		String sql = "SELECT COUNT(*) FROM textusers WHERE emailId=?";
		int count =  jdbcTemplate.queryForObject(sql, Integer.class, email);
		if(count>0){

		Email sendemail=new Email();
		String sourceemail=RandomStringUtils.randomAlphanumeric(10)+"@gmail.com";
		String value=RandomStringUtils.randomNumeric(4);
		
		sendemail.setSource(sourceemail);
		sendemail.setDestination(email);
		// -----------------------------------
		sendemail.setText("The otp for email verification is : "+value);
		sendemail.setSubject("Otp");
		this.setValidemail(email);

		// ----------------------------------------------------------------
		System.out.println("The value set to : "+this.getValidemail());
		newotp.setOtp(value);

		TextServiceImpl ts=new TextServiceImpl();
		ts.setApikey("f08f847f23mshebd48ab6414263bp1f804cjsn1f81c01384a8");
		ts.sendMail(sendemail);

		System.out.println("Email sent with otp : "+newotp.getOtp());
		
		modelAndView.setViewName("redirect:/VerifyOtp");
		
		}
		else{
		modelAndView.addObject("iserror",true);
		modelAndView.addObject("msg", "User not found!");
		modelAndView.setViewName("redirect:/Register");
		}
		
		return modelAndView;
	}


	@GetMapping("/VerifyOtp")
	public ModelAndView verifyotp(ModelAndView modelAndView,Otp otp){
	
		modelAndView.addObject("otp", otp);
		modelAndView.setViewName("emailotp");
		return modelAndView;
	}

	@PostMapping("/VerifyOtp")
	public ModelAndView verifyemailotp(ModelAndView modelAndView,Otp Otp){

	  Otp.setOtp(Otp.getV1()+Otp.getV2()+Otp.getV3()+Otp.getV4());	;
		
	
		if(newotp.getOtp().equals(Otp.getOtp())){
		
			
			modelAndView.setViewName("redirect:/ChangePassword");
		}
		else{
			System.out.print("im executing");
			modelAndView.addObject("otp", Otp);
			modelAndView.addObject("iserror",true);
		    modelAndView.addObject("msg", "Otp Invalid");
			modelAndView.setViewName("emailotp");

		}
		return modelAndView;
	}

	@GetMapping("/ChangePassword")
	public ModelAndView changepassword(ModelAndView modelAndView,Password pass){
	
		modelAndView.addObject("pass", pass);
		modelAndView.setViewName("changepassword");
		return modelAndView;
	}

	@PostMapping("/ChangePassword")
	public ModelAndView changedatabasepassword(ModelAndView modelAndView,Password pass){
	
		String sql="UPDATE textusers SET password=? WHERE emailId=?";
		int result=jdbcTemplate.update(sql,pass.getPassword(),this.getValidemail());

		if(result>0){
			modelAndView.addObject("iserror",false);
		    modelAndView.addObject("msg", "Password Changed Successfully");
			modelAndView.setViewName("redirect:/Login");
		}
		else{
			modelAndView.addObject("pass", pass);
			modelAndView.addObject("iserror",true);
		    modelAndView.addObject("msg", "Some error occured!!Try again");
			modelAndView.setViewName("changepassword");
		}
		return modelAndView;
	}





}
