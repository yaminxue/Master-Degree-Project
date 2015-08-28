<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background-color:#F8F8F8">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<title>1641 deposition</title>
<script src="js/jquery.min.js"></script>

<style type="text/css">
#savebutton {
	width: 160px;
	margin-left: 300px;
	color: #009900;
	font-weight: bold;
}
#deletebutton {
	width: 160px;
	margin-left: 300px;
	color: #CC3300;
	font-weight: bold;
}
</style>

<script>
	function checkFileStatus() {
		id = "${sessionScope.id}";
		if(!id)
			$("#top").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc()\" value=\"add to my collection\">");
		else{
			$.ajax({
				type : "get",
				url : "1641search/checkFileStatus.action",
				data : {
					userid : id,
					depoid : filename
				},

				success : function(data) {
					//alert(data);
					if(data=="saved")
						$("#top").append("<input type=\"button\" id=\"deletebutton\" onclick=\"deleteDoc()\" value=\"remove from my collection\">");
					else
						$("#top").append("<input type=\"button\" id=\"savebutton\" onclick=\"saveDoc()\" value=\"add to my collection\">");

				}
			});
		}
		
	}
</script>

<script>
	function start() {
		values = location.search;
		pos = values.split("=");
		filename = pos[1];
		//alert(filename);	
		document.getElementById("filetitle").innerHTML = filename;
		setTimeout('checkFileStatus()', 10);
		setTimeout('showcontent()', 10);
	}

			function showcontent() {
				//alert(filename);
				 $.ajax({
					type : "get",
					url : "1641search/getFilecontent.action",
					data : "q=" + filename,

					success : function(data) {
						var filecontent = data.split("~");
						for(var i=0;i<filecontent.length;i++){
							
							$("#resultset").append("<p>"+filecontent[i]+"<br>"+"</p>");
						}
					}
				}); 
	}
			
			function saveDoc(){
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
								 window.location.reload();
							 else
								 alert("action failure");
						 }
					
					});
				}
					
			}
			
			function deleteDoc(){
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
							 window.location.reload();
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
		<div id="top">
		<h2 id="filetitle"></h2>
<!-- 		<input type="button" id="savebutton" onclick="saveDoc()"
			value="save this document"> -->
			</div>>
		<div id="resultset"></div>
	</div>
</body>
</html>