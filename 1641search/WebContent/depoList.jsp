<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background-color:#F8F8F8">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>Deposition related to recommend entity</title>

<script src="js/jquery.min.js"></script>
<style type="text/css">
#savebutton {
	width: 160px;
	margin-left: 10px;
	color: #009900;
	font-weight: bold;
}
#deletebutton {
	width: 160px;
	margin-left: 10px;
	color: #CC3300;
	font-weight: bold;
}
</style>
<script>
	function start() {
		id = "${sessionScope.id}";
		var values = location.search;
		var pos = values.split("=");
	    var n = pos[1];
		//alert(n);
		if(n.indexOf("%C3%B4")!=-1){
			var x = n.indexOf("%C3%B4");
			n = n.substring(0,x)+"ô"+n.substring(x+6,n.length);
		}
		//alert(n);
		name ="";
		
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
		document.getElementById("title").innerHTML = "Depositions concerning "+name;
		setTimeout('showDepoList()', 10);
	}
	
	
	function showDepoList(){
		$.ajax({
			type : "get",
			url : "1641search/getDepoList.action",
			data : "n=" + name,
			
			success : function(data) {
				document.getElementById("resultset").innerHTML = " ";
				//alert(data);
				var temp = data.split("~");
				for(var i=0; i<temp.length; i++){
					$.ajax({
						type : "get",
						url : "1641search/getParticularcontent.action",
						data : "q=" + temp[i],
						async : false,

						success : function(data1) {
							$("#resultset").append( "<a href=\"document.jsp?fn="
																				+ temp[i]
																				+ "\" >"
																				+ temp[i]
																				+ "</a>");
							if(!id)
								$("#resultset").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc(this)\" class=\""+temp[i]+"\" value=\"add to my collection\">");
							else{
								$.ajax({
									type : "get",
									url : "1641search/checkFileStatus.action",
									data : {
										userid : id,
										depoid : temp[i]
									},
									async : false,

									success : function(data3) {
										//alert(data3);
										if(data3=="saved")
											$("#resultset").append("<input type=\"button\" id=\"deletebutton\" onclick=\"deleteDoc(this)\" class=\""+temp[i]+"\" value=\"remove from my collection\">");
										else
											$("#resultset").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc(this)\" class=\""+temp[i]+"\" value=\"add to my collection\">");

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
		});
		
	}
	
	
	function saveDoc(obj){
		//alert(obj.getAttribute("class"));
		var filename = obj.getAttribute("class");
		if(!id)
			alert("Please login!");
		else{
			$.ajax({
				type : "get",
				url : "1641search/saveDoc.action",
				data : {
					userid : id,
					depoid : filename
				},
				 success : function(data){
					// alert(data);
					 if(data=="saved")
						 showDepoList();
					 else
						 alert("action failure");
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
				userid : id,
				depoid : filename
			},
			success : function(data){
				//alert(data);
				 if(data=="deleted")
					 showDepoList();
				 else
					 alert("action failure");
			 }
		});
	}
</script>

</head>

<body onload="start()">

	<div align="center"
		style="width: 500px; margin-left: auto; margin-right: auto;">
		<br>
		<h2 id="title"></h2>
		<div id="resultset"></div>
	</div>
</body>

</html>
