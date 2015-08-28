<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="strutsAction.GetSavedDocAction"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background-color:#F8F8F8">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<script src="js/jquery.min.js"></script>
<title>My collection</title>
<style type="text/css">
#r-down{
	text-align: justify;
	padding: 15px;
	border: 1px solid #eeeeee;
	background-color: #f5f5f5;
}
#r-down1{
	text-align: justify;
	padding: 15px;
	border: 1px solid #eeeeee;
	background-color: #f5f5f5;
}

#tagstyle {
	text-decoration: none;
}

#tagstyle:link {
	color: #0063DC;
}

#tagstyle:visited {
	color: #1057ae;
}
#tagstyle:hover {
	color: #FFFFFF;
	background: #0063DC;
}

#tagstyle:active { 
	color: #FFFFFF;
	background: #0259C4;
}

#deletebutton {
	width: 160px;
	margin-left: 10px;
	color: #CC3300;
	font-weight: bold;
}

#savebutton {
	width: 160px;
	margin-left: 10px;
	color: #009900;
	font-weight: bold;
}

</style>
<script>
	function start() {
		var values = location.search;
		var pos = values.split("=");
		userid = pos[1];
		setTimeout('showcontent()', 10);
	}

			function showcontent() {
				document.getElementById("resultset").innerHTML = " ";
				document.getElementById("r-down").innerHTML = " ";
				document.getElementById("r-down1").innerHTML = " ";
				//alert(filename);
				 $.ajax({
					type : "get",
					url : "1641search/getSavedDoc.action",
					data : "userid=" + userid,

					success : function(data) {
						//alert(data);
						if(data!=""){
							var file = data.split("~");
							for(var i=0;i<file.length;i++){
								$.ajax({
									type : "get",
									url : "1641search/getParticularcontent.action",
									data : "q=" + file[i],
									async : false,

									success : function(data1) {
								    //alert(data1);
										$("#resultset").append(
												 "<a href=\"document.jsp?fn="
												+ file[i]
												+ "\" >"
												+ file[i] 
												+ "</a><input type=\"button\" id=\"deletebutton\" onclick=\"deleteDoc(this)\" class=\""+file[i]+"\" value=\"remove from my collection\"><br>"+ data1.substring(0,220)
												+ "..." +"<br>"+"<br>");
									}
								});
							}
							

							$.ajax({
								type : "get",
								url : "1641search/getSuggestions.action",
								data : "d=" + data,
								async : false,

								success : function(data2) {
									suggestions = data2.split("~");
									//alert(suggestions);
									for (var i=0; i<suggestions.length;i++) {
										var count=Math.random()*100;
										if(count>50)
											count=count-50;
										var tag =suggestions[i];
										$("#r-down").append(
											"<a href=\"depoList.jsp?n="+tag+"\" id=\"tagstyle\" style=\"font-size: "+count+"px;\" >"+tag+"  "+"</a>");	 
									}
									
										$.ajax({
										type : "get",
										url : "1641search/getRecommendDoc.action",
										data :  {
											d : data2,
											fd : data
										},
										async : false,
		
										success : function(data3) {
											if(data3!=""){
												recDoc = data3.split("~");
												//alert(recDoc);
												
												for (var i=0; i<recDoc.length;i++) {
													$.ajax({
														type : "get",
														url : "1641search/getParticularcontent.action",
														data : "q=" + recDoc[i],
														async : false,

														success : function(data5) {
															$("#r-down1").append(
																	"<a href=\"document.jsp?fn="
																	+ recDoc[i]
																	+ "\" >"
																	+ recDoc[i]
																	+ "</a>");
															$.ajax({
																type : "get",
																url : "1641search/checkFileStatus.action",
																data : {
																	userid : userid,
																	depoid : recDoc[i]
																},
																async : false,
			
																success : function(data4) {
																	//alert(data);
																	if(data4=="saved")
																		$("#r-down1").append("<input type=\"button\" id=\"deletebutton\" onclick=\"deleteDoc(this)\" class=\""+recDoc[i]+"\" value=\"remove from my collection\">");
																	else
																		$("#r-down1").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc(this)\" class=\""+recDoc[i]+"\" value=\"add to my collection\">");
			
																}
															});
															
															
															$("#r-down1").append("<br>"+ data5.substring(0,220)
																	+ "..." +"<br>"+"<br>");
															}
													});
													 
												}
											}
										}
		
									});
											
									
								}

							});
							
						
							
						}
						
					}
				}); 
	}
			
			function deleteDoc(obj){
				//alert(obj.getAttribute("class"));
				var filename = obj.getAttribute("class");
				$.ajax({
					type : "get",
					url : "1641search/deleteDoc.action",
					data : {
						userid : userid,
						depoid : filename
					},
					success : function(data){
						//alert(data);
						 if(data=="deleted")
							 showcontent();
						 else
							 alert("action failure");
					 }
				});
			}
			
			function saveDoc(obj){
				//alert(obj.getAttribute("class"));
				var filename = obj.getAttribute("class");
				if(!userid)
					alert("Please login!");
				else{
					$.ajax({
						type : "get",
						url : "1641search/saveDoc.action",
						data : {
							userid : userid,
							depoid : filename
						},
						 success : function(data){
							// alert(data);
							 if(data=="saved")
								 showcontent();
							 else{
								 
								 alert("Already saved");
								 
							 }
						 }
					
					});
				}
					
			}
</script>

</head>

<body onload="start()">

	<div style="width: 500px; margin-left: 100px; float: left">
		<br>
		<h2>MY COLLECTION</h2>
			
		<div id="resultset"></div>
		<h2>Recommend Documents</h2>
		<div id="r-down1">
		</div>
	</div>
	
	
	<div  style="width: 600px; margin-top: 200px; margin-right: 100px; float: right">
		<h2>Recommend Entities</h2>
		<div id="r-down">
		</div>
		
	</div>
</body>
</html>