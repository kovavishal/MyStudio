/**
 * 
 */
package com.studio.controller;

import java.sql.Date;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.studio.constants.Login;
import com.studio.dao.MetaDAO;
import com.studio.dao.UserDAO;
import com.studio.domain.Employee;
import com.studio.controller.PrintController;
/*import com.studio.service.EmailService;*/

/**
 * @author ezhilraja_k
 *
 */

@Controller
@PropertySource("classpath:studio.properties")
public class LoginController implements Login  {
	
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private MetaDAO metaDAO;
	Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	private Environment env;
	
	@Autowired
	private PrintController cookie;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView defaultLogin(ModelAndView  modelAndView){
		Date today = new Date(0); // Fri Jun 17 14:54:28 PDT 2016 
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH); // 5
		int year= cal.get(Calendar.YEAR);
		/* System.out.println("MONTH "+month);  
		 System.out.println("YEAR "+year); 
		 System.out.println("env MONTH "+Integer.parseInt(env.getProperty("due.date")));  
	    if (month >=Integer.parseInt(env.getProperty("due.date")) && year > 2021)
		    modelAndView.addObject(MESSAGE, "Your SOFTWARE AMC IS DUE,Kindly do renewal and GET UNINTERRUPTED SERVICE");*/
		  
		modelAndView.setViewName(PAGE_LOGIN);
		
		
		return modelAndView;
	}
		
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView  modelAndView){
		modelAndView.setViewName(PAGE_LOGIN);
		return modelAndView;
	}
	 
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView validateUserCredential(@RequestParam(defaultValue = USERNAME)String userName,
			@RequestParam(defaultValue = PASSWORD)String password,  ModelAndView  modelAndView,HttpServletResponse response, HttpServletRequest HttpServletRequest){

		if(!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)){
			Employee employee = userDAO.getEmployee(userName);
			if(employee !=null && employee.getPassword()!=null){
				if(employee.getPassword().equals(password)){
					Map<Integer,String> roleMap = metaDAO.getRoleMap();
					if(employee.getRoleId()!=null &&  roleMap.get(employee.getRoleId()).equals(env.getProperty("rol.agent"))){
						modelAndView = new ModelAndView("redirect:/report/agent-remoteAccess");
					}else if(employee.getRoleId()!=null &&  roleMap.get(employee.getRoleId()).equals(env.getProperty("rol.employee"))){
					modelAndView = new ModelAndView("redirect:/order/allocation/" + employee.getEmpId());
// TO DISPLAY PURCHASE SCREEN IMMEDIALY AFTER LOGIN AS PURCAHASE EMPLOYEE
				}else if(employee.getRoleId()!=null &&  roleMap.get(employee.getRoleId()).equals(env.getProperty("rol.purchase"))){
				/*	modelAndView = new ModelAndView("redirect:/payment/purchase/" + employee.getEmpId());*/
					modelAndView = new ModelAndView("redirect:/payment/purchase/");
					}else{
						modelAndView.setViewName(PAGE_WORKORDER);
						modelAndView = new ModelAndView("redirect:/home");
					}
					Cookie cookie1 = new Cookie("empid", String.valueOf(employee.getEmpId()));
					response.addCookie(cookie1);
					Cookie roleCookie = new Cookie("role", roleMap.get(employee.getRoleId()));
					response.addCookie(roleCookie);
					Cookie nameCookie = new Cookie("name", String.valueOf(employee.getName()));
					response.addCookie(nameCookie);
					
					String uname = cookie.getCookie(HttpServletRequest , "empid");
					employee.setName(uname);
					//System.out.println("USER NAME :"+uname);
			// System Date verification starts
					/*ControlTable  lastlogin=userDAO.getLastLoginDate();
					Date date=lastlogin.getLastLoginDate();
					SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy"); 
					String date1 = dateformat.format(date);
					Date todaydate = new Date();
					String today=dateformat.format(todaydate);*/
	/*				ControlTable  lastlogin=userDAO.getLastLoginDate();
					Date ctrldate=lastlogin.getLastLoginDate();
					
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd"); 
					String date1 = dateformat.format(ctrldate);
				
					//System.out.println("Date control:"+date1);
					Date todaydate = new Date();
					String today=dateformat.format(todaydate);
					//System.out.println("Date control:"+today);
					if(today.compareTo(date1)<0){
						modelAndView.addObject(MESSAGE, MESSAGE_INVALID_DATE+" "+today+"  "+date1);
						modelAndView.setViewName(PAGE_LOGIN);
			
					}else{
						
						lastlogin.setLastLoginDate(todaydate);
						userDAO.save(lastlogin);
					}*/
			//System Date verification Ends
				}else{
					modelAndView.addObject(MESSAGE, MESSAGE_INVALID_USERNAME_PASSWORD);
					modelAndView.setViewName(PAGE_LOGIN);
				}
			}else{
				modelAndView.addObject(MESSAGE, MESSAGE_INVALID_USERNAME_PASSWORD);
				modelAndView.setViewName(PAGE_LOGIN);
			}
		}else{
			modelAndView.addObject(MESSAGE, MESSAGE_INVALID_USERNAME_PASSWORD);
			modelAndView.setViewName(PAGE_LOGIN);
		}
		 
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logOut(ModelAndView  modelAndView,HttpServletResponse response){
		Cookie cookie=new Cookie("empid","");  
		cookie.setMaxAge(0);  
		response.addCookie(cookie); 
        modelAndView.setViewName(PAGE_LOGIN);
		return modelAndView;
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ModelAndView validateUserName(@RequestParam(defaultValue = USERNAME)String userName,
			HttpServletRequest request,ModelAndView  modelAndView){
		
		request.getSession().removeAttribute(EMPLOYEEID);
		Employee employee = null;
		if(!StringUtils.isEmpty(userName))
			employee = userDAO.getEmployee(userName.trim());
		
		if(employee !=null){
			request.getSession().setAttribute(EMPLOYEEID, employee.getEmpId());
			modelAndView.setViewName(PAGE_UPDATEPASSWORD);
		}else{
			modelAndView.addObject(MESSAGE, MESSAGE_INVALID_USERNAME);
			modelAndView.setViewName(PAGE_FORGOTPASSWORD);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public ModelAndView forgotPassword(ModelAndView  modelAndView){
		modelAndView.setViewName(PAGE_FORGOTPASSWORD);
		return modelAndView;
	}
	
	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public ModelAndView updatePassword(@RequestParam(defaultValue = PASSWORD)String password,
			HttpServletRequest request, ModelAndView  modelAndView){
	
		try{
			if(!StringUtils.isEmpty(password)){
				String customerId = request.getSession().getAttribute(EMPLOYEEID).toString().trim();
				Employee employee = userDAO.getEmployeeById(Long.valueOf(customerId));
				employee.setPassword(password);
				userDAO.save(employee);
				request.getSession().removeAttribute(EMPLOYEEID);
				modelAndView.setViewName(PAGE_LOGIN);
			}else{
				modelAndView.addObject(MESSAGE, MESSAGE_INVALID_PASSWORD);
				modelAndView.setViewName(PAGE_UPDATEPASSWORD);
			}
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		return modelAndView;
	}
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(ModelAndView  modelAndView){
		modelAndView.setViewName("home");
		return modelAndView;
	}
  /*  public static boolean downloadFile(File file, File tofile) {
    	
		String home = System.getProperty("user.home");
		String exl1 = home + "\\Downloads\\MARVEL.pdf"; 
	            String exl2 = "NEWMARVEL.PDF";
	downloadFile(new File(exl1), new File(exl2));

    	
    	
        System.out.println("1. File exists : " + file.exists());
        boolean success = false;
        String saveAsFileName = tofile.toString();
        System.out.println("saveAsFileName : " + saveAsFileName);
      
        System.out.println("file.toPath() : " + file.toPath());

        if (file.exists()) {
            String fileName = file.getName();
            System.out.println("**********************");
            System.out.println("FILE FOUND " + fileName);
            System.out.println("**********************");
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            String contentType = ec.getMimeType(fileName); // JSF 1.x: ((ServletContext) ec.getContext()).getMimeType(fileName);
            int contentLength = (int) file.length();

            ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
            ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + saveAsFileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

            try {
                OutputStream output = ec.getResponseOutputStream();
                Files.copy(file.toPath(), output);
            } catch (Exception e) {
                System.out.println("<< Error when downloading file (2) : " + e.getMessage());
            }

            fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
            success = true;
        } else {
            success = false;
        }

        System.out.println("Return SUCCESS : " + success);
        return success;
    }
	
	*/
	
	
}
