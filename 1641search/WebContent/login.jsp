<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background-color:#F8F8F8">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
<div 
		style="  position:absolute; 
    top:30%; left:32%; height:240px; 
    margin-top:-120px" >
    
    <h2>Log In</h2>
    <form method="post" action="login">
     <br>
     <div style="margin-left: 120px">
          <p class="text-center">Email</p>
        
                  <p style="margin:0;">
                    <input type="text" placeholder="Email Address" name="email">
                  </p>
				 
          <p class="text-center">Password</p>
        
                  <p style="margin:0;">
                    <input type="password"  placeholder="Input Password" name="pwd">
                  </p>
				  
              <br>
			  <p>
              <button type="submit" class="btn btn-info finsh_box " style="width:100px;">Submit</button>
			  </p>
			
          </div>
    </form>
    </div>
</body>
</html>