<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
	<title>krpano.com - Cubical Pano</title>
	<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="x-ua-compatible" content="IE=edge" />
	<style>
		@-ms-viewport { width: device-width; }
		@media only screen and (min-device-width: 800px) { html { overflow:hidden; } }
		html { height:100%; }
		body { height:100%; overflow:hidden; margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; font-size:16px; color:#FFFFFF; background-color:#000000; }
	</style>
</head>
<body>

<script src="http://www.fescateva.com/swfkrpano.js"></script>

<%

	String panorama = request.getParameter("panorama");
	if (panorama == null) panorama = "cathedral.jpg";
	
%>


<div id="pano" style="width:100%;height:100%;">
	<noscript><table style="width:100%;height:100%;"><tr style="valign:middle;"><td><div style="text-align:center;">ERROR:<br/><br/>Javascript not activated<br/><br/></div></td></tr></table></noscript>
	<script>
		embedpano({swf:"http://www.fescateva.com/krpano.swf", xml:"<%=panorama%>", target:"pano", html5:"auto", passQueryParameters:true});
	</script>
</div>

</body>
</html>