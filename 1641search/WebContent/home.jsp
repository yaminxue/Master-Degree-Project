<%@page import="strutsAction.QueryAction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="css/style.css" />

<script src="js/jquery.min.js"></script>
<script src="js/jquery.textext.js"></script>
<script src="js/textext.plugin.autocomplete.js"></script>
<script src="js/jquery.highlight.js"></script>


<title>1641 Deposition</title>

<script>
	$(document).ready(function() {
		$.ajax({
			type : "get",
			url : "1641search/getData.action",

			success : function(data) {
				datafromServer = data.split("~");
				//alert(datafromServer);

			}
		});

	});
</script>


<script type="text/javascript">
	$(document).ready(
			function() {
				$('#andbag').textext({
					plugins : 'tags autocomplete'/* ,
										tagsItems : [ 'Basic', 'JavaScript', 'PHP', 'Scala' ] */
				}).bind(
						'getSuggestions',
						function(e, data) {
							var list = datafromServer, textext = $(e.target)
									.textext()[0], query = (data ? data.query
									: '')
									|| '';

							$(this).trigger(
									'setSuggestions',
									{
										result : textext.itemManager().filter(
												list, query)
									});
						});
				;
			});
</script>

<script type="text/javascript">
	$(document).ready(
			function() {
				$('#orbag').textext({
					plugins : 'tags autocomplete'
				}).bind(
						'getSuggestions',
						function(e, data) {
							var list = datafromServer, textext = $(e.target)
									.textext()[0], query = (data ? data.query
									: '')
									|| '';

							$(this).trigger(
									'setSuggestions',
									{
										result : textext.itemManager().filter(
												list, query)
									});
						});
				;
			});
	
	
</script>


<script>

    
	function start() {
		loginUser = "${sessionScope.id}";
		//alert(loginUser);
		//loginUser=1;
		
		if(!loginUser)
			$("#header").append("<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><a href=\"login.jsp\" id=\"linkstyle\">SIGN IN</a> <a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a> <a href=\"register.jsp\" id=\"linkstyle\">CREATE ACCOUNT</a>");	
		else
			$("#header").append("<a style=\" color: black\">&nbsp;&nbsp;&nbsp;WELCOME!&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><a id=\"linkstyle\" href=\"savedDepo.jsp?id="+loginUser+"\" target=\"_blank\">MY COLLECTION</a> <a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a> <a href=\"logout.action?id="+loginUser+"\" id=\"linkstyle\" style=\" color: white\">SIGN OUT</a>");
		
		datafromServer = new Array();
		textset1 = "";
		textset2 = "";
		suggestions = new Array();

		var textarea1 = $('textarea.querybag1:last');
		textarea1.bind('setFormData', function(e, data, isEmpty) {
			var textext1 = $(e.target).textext()[0];
			textset1 = textext1.hiddenInput().val();
		});
		textarea1.textext()[0].getFormData();
		
		//alert(textset1);

		var textarea2 = $('textarea.querybag2:last');
		textarea2.bind('setFormData', function(e, data, isEmpty) {
			var textext2 = $(e.target).textext()[0];
			textset2 = textext2.hiddenInput().val();
		});
		textarea2.textext()[0].getFormData();

	}

	function search() {

		var ANDstr = "";
		var ORstr = "";

		if (textset1.length != 2)
			ANDstr = textset1.substring(2, textset1.length - 2);
		if (textset2.length != 2)
			ORstr = textset2.substring(2, textset2.length - 2);

		//alert(ANDstr);
		//alert(ORstr);

		$.ajax({
					type : "get",
					url : "1641search/Query.action",
					data : {
						andquery : ANDstr,
						orquery : ORstr
					},

					success : function(data) {
						//alert(data);
						document.getElementById("resultset").innerHTML = " ";
						if (data == "No results containing all your search terms were found.") {
							$("#resultset").append("<p>" + data + "</p>");
						}

						else {
							var temp = data.split("~");
							document.getElementById("r-down").innerHTML = " ";
							document.getElementById("r-down1").innerHTML = " ";
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
											"<a href=\"depoList.jsp?n="+tag+"\" id=\"tagstyle\" style=\"font-size: "+count+"px;\" target=\"_blank\" oncontextmenu=\"contextMenu(this)\">"+tag+"  "+"</a>");
									}

										$.ajax({
										type : "get",
										url : "1641search/getRecommendDoc.action",
										data :  {
											d : data2,
											fd : data
										},
										async : false,
		
										success : function(data6) {
											if(data6!=""){
												recDoc = data6.split("~");
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
																	userid : loginUser,
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
							
							
							for (var x = 0; x < temp.length; x++) {
								$.ajax({
											type : "get",
											url : "1641search/getParticularcontent.action",
											data : "q=" + temp[x],
											async : false,

											success : function(data1) {
												$("#resultset")
														.append(
																 "<a href=\"document.jsp?fn="
																		+ temp[x]
																		+ "\""
																		+ "target=\"_blank\" >"
																		+ temp[x]
																		+ "</a>");
												if(!loginUser)
													$("#resultset").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc(this)\" class=\""+temp[x]+"\" value=\"add to my collection\">");
												else{
													$.ajax({
														type : "get",
														url : "1641search/checkFileStatus.action",
														data : {
															userid : loginUser,
															depoid : temp[x]
														},
														async : false,

														success : function(data3) {
															//alert(data3);
															if(data3=="saved")
																$("#resultset").append("<input type=\"button\" id=\"deletebutton\" onclick=\"deleteDoc(this)\" class=\""+temp[x]+"\" value=\"remove from my collection\">");
															else
																$("#resultset").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc(this)\" class=\""+temp[x]+"\" value=\"add to my collection\">");

														}
													});
												}
												$("#resultset")
												.append( "<br>"
																		+ data1.substring(0,220)
																		+ "..." +"<br>"+"<br>");
											}
										});
							}
						}

						var andkeys = ANDstr.split("\",\"");
						//alert(andkeys);
						for (var a = 0; a < andkeys.length; a++) {
							if (andkeys[a].indexOf("Entity") != -1) {
								var temp = andkeys[a].split('(');
								//alert(temp[0]);
								$("#resultset").highlight(temp[0]);
							} else
								$("#resultset").highlight(andkeys[a]);
						}
						var orkeys = ORstr.split("\",\"");
						for (var b = 0; b < orkeys.length; b++) {
							if (orkeys[b].indexOf("Entity") != -1) {
								var temp1 = orkeys[b].split('(');
								$("#resultset").highlight(temp1[0]);
							} else
								$("#resultset").highlight(orkeys[b]);
						}

						$(".highlight").css({
							color : "#F80000 "
						});
						
					}

				});

	}
	
	function saveDoc(obj){
		//alert(obj.getAttribute("class"));
		var filename = obj.getAttribute("class");
		if(!loginUser)
			alert("Please login!");
		else{
			$.ajax({
				type : "get",
				url : "1641search/saveDoc.action",
				data : {
					userid : loginUser,
					depoid : filename
				},
				 success : function(data){
					// alert(data);
					 if(data=="saved")
						 search();
					 else{
						 search();
						 alert("Already saved");
						 
					 }
				 }
			
			});
		}
			
	}
	
	function deleteDoc(obj){
		//alert(obj.getAttribute("class"));
		var filename = obj.getAttribute("class");
		$.ajax({
			type : "get",
			url : "1641search/deleteDoc.action",
			data : {
				userid : loginUser,
				depoid : filename
			},
			success : function(data){
				//alert(data);
				 if(data=="deleted")
					 search();
				 else{
					 search();
					 alert("Already deleted");
					
				 }
					 
			 }
		});
	}
	
	/* right click event on suggestions */
	 function contextMenu(obj){  
		
		var x=obj.toString();
		//alert(x);
		 var pos = x.split("=");
		 var n = pos[1];
			//alert(n);
			var name ="";
			
			if(n.indexOf("%20")!=-1){
				var temp = n.split("%20");
				for(var i=0; i<temp.length-1; i++){
					name = name+temp[i]+" ";
				}
				name = name+temp[temp.length-1];
			}
			else
				name = n;
			
			//alert(name);
		
		
		 document.oncontextmenu = function(e){
		        e.preventDefault();
		        //alert("sdfdsg");
		 }
		 
		var x = prompt("Please enter what bag you want to add this entity to (lowercase)", "and");
		if(x=="and")
			addToAndBag(name);
		else if(x=="or")
			addToOrBag(name);
		
		
		
	 } 
	
	function addToAndBag(name){
		//alert(name);
		document.getElementById('andbag').value += name;
		document.getElementById('andbag').focus();
		var WshShell = new ActiveXObject("WScript.Shell");
		WshShell.SendKeys("{Enter}"); 
	}
	
	function addToOrBag(name){
		//alert(name);
		document.getElementById('orbag').value += name;
		document.getElementById('orbag').focus();
		var WshShell = new ActiveXObject("WScript.Shell");
		WshShell.SendKeys("{Enter}"); 
	}
	
</script>


</head>

<body onload="start()">
	<div id="header1">
		<h1>1641 Depostion</h1>
	</div>
	<div id="header">

		<!-- 	
		<a href="login.jsp">Login</a>
		<a href="register.jsp">Register</a>
		<a href="savedDepo.jsp">Saved Deposition</a> -->
	</div>

	<div id="left">
		<h2>Result</h2>
		<div id="resultset"></div>
	</div>

	<div id="right">

		<div id="r-up">
			<h2>Query</h2>
			<div id="andquerydiv">
				<p>AND BAG</p>
				<textarea id="andbag" class="querybag1" placeholder="add one..."></textarea>
			</div>
			<div id="orquerydiv">
				<p>OR BAG</p>
				<textarea id="orbag" class="querybag2" placeholder="add one..."></textarea>
			</div>
			<div id="searchbuttondiv">
				<br> <input type="button" id="searchbutton" onclick="search()"
					value="search">

			</div>
			<br>
			<h2>Recommend Entities</h2>

		</div>
		

		<div id="r-down">
		</div>
		
		<h2>Recommend Documents</h2>
		<div id="r-down1">
		</div>
	
	
	</div>



</body>
</html>
