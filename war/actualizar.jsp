<%@page import="fsk4.server.Web"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Actualizar catálogo</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="css/hint.min.css" />

<link rel="stylesheet" href="css/ui-lightness/jquery-ui-1.10.3.custom.min.css" />
<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-tab.js"></script>
	<script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>



<script type="text/javascript">

$(document).ready(function() {
	
      
      $("#f")
        /*
         * clear local storage when the form is submitted
         */
        .submit(function(){
	  		$("#boton-refrescar").attr("disabled", "disabled");
			$("#boton-refrescar").val("Mandando al servidor. Un momento por favor...");
	
			
	  	    var url = "actualizarajax.jsp"; // the script where you handle the form input.
	
	  	  var jqxhr = $.post(url, $("#f").serialize())
	  		.done(function() { 

	  			alert("Proceso en marcha. Recibirás un email cuando se haya completado el proceso."); 
	  			
	  			})
	  		.fail(function(xhr, textStatus, errorThrown) { 
	  			alert("Error:" + xhr.responseText + " / " + textStatus + " / " + errorThrown); 
	  		})
	  		.always(function() {
	     		$("#boton-refrescar").removeAttr("disabled");
	    		$("#boton-refrescar").val("Actualizar catálogo");
	  		});
	  	    return false;
        })
        ;
    
      $("#f2")
      /*
       * clear local storage when the form is submitted
       */
      .submit(function(){
	  		$("#boton-crearexcel").attr("disabled", "disabled");
			$("#boton-crearexcel").val("Mandando al servidor. Un momento por favor...");
	
			
	  	    var url = "crearexcelajax.jsp"; // the script where you handle the form input.
	
	  	  var jqxhr = $.post(url, $("#f").serialize())
	  		.done(function() { 

	  			alert("Proceso en marcha. Recibirás un email cuando se haya completado el proceso."); 
	  			
	  			})
	  		.fail(function(xhr, textStatus, errorThrown) { 
	  			alert("Error:" + xhr.responseText + " / " + textStatus + " / " + errorThrown); 
	  		})
	  		.always(function() {
	     		$("#boton-crearexcel").removeAttr("disabled");
	    		$("#boton-crearexcel").val("Crear excel catálogo");
	  		});
	  	    return false;
      })
      ;
	
});


</script>


</head>
<body>

<form id="f" method="post" style="margin: 15px; float: left;">
	<table>
		<tr>
			<td><input id="boton-refrescar" type="submit" class="btn btn-info" value="Actualizar catálogo"></td>
		</tr>
	</table>
</form>

<form id="f2" method="post" style="margin: 15px; float: left;">
	<table>
		<tr>
			<td><input id="boton-crearexcel" type="submit" class="btn btn-info" value="Crear excel catálogo"></td>
		</tr>
	</table>
</form>

</body>
</html>