<%@page import="fsk.server.modelo.Helper"%>
<%@page import="fsk4.server.Web"%><%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><!DOCTYPE HTML>
<!--
	Arcana 2.0 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>FesKteva</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="FesKteva es la herramienta 3D que te ayuda a diseñar tu casa." />
		<meta name="keywords" content="FesKteva 3D room floor planner design tool diseño interiores " />
		<meta name="google-translate-customization" content="a437d09973638e4d-2916b7d2bcd82524-geff7d8ad352b08ef-10"></meta>
		<link href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300,300italic,700" rel="stylesheet" />
		<script src="js/jquery-1.10.2.min.js"></script>
		<script src="js/ui/minified/jquery-ui.min.js"></script>
		<script src="js/config.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		<link rel="stylesheet" href="css/hint.min.css"></link>
		<noscript>
			<link rel="stylesheet" href="css/skel-noscript.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-desktop.css" /><link rel="stylesheet" href="css/hint.min.css"></link>
		</noscript>
		<!--[if lte IE 9]><link rel="stylesheet" href="css/style-ie9.css" /><![endif]-->
		<!--[if lte IE 8]><script src="js/html5shiv.js"></script><![endif]-->
		
		<script>
  $(function() {
    //$( document ).tooltip();
  });
  </script>
  
	</head>
	<body>

		<!-- Header -->

			<div id="header-wrapper">
				<header class="container" id="site-header">
					<div class="row">
						<div class="12u">
<!--
							<div id="logo">
								<h1>FesKteva</h1>
							</div>
-->
							<nav id="nav">
								<ul>
									<li class="current_page_item"><a href="index.jsp">Inicio</a></li>
									<li><a href="perfiles.jsp">Perfiles</a></li><li><a href="galeria.jsp">Galería</a></li>
<!--
									<li><a href="profesionales.html">Profesionales</a></li>
-->
<!--
									<li><a href="threecolumn1.html">Three Column #1</a></li>
									<li><a href="threecolumn2.html">Three Column #2</a></li>
									<li><a href="twocolumn1.html">Two Column #1</a></li>
									<li><a href="twocolumn2.html">Two Column #2</a></li>
									<li><a href="onecolumn.html">One Column</a></li>
-->
									<li><div id="google_translate_element"></div><script type="text/javascript">
function googleTranslateElementInit() {
  new google.translate.TranslateElement({pageLanguage: 'es', includedLanguages: 'ca,de,el,en,eu,fr,it,ja,no,pt,ru,sv,tr,es', layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL, autoDisplay: false}, 'google_translate_element');
}
</script><script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script></li>
								</ul>
							</nav>
						</div>
					</div>
				</header>
			</div>

		<!-- Main -->

			<div id="main-wrapper">
				<div class="container">
					
					<!-- Banner -->

						<div class="row">
							<div class="12u">
								<div id="banner">
									<a href="#"><img src="images/banner.jpg" alt="" /></a>									
									<div class="caption">
										<span><strong>FesKteva</strong>: enseñamos tus ideas</span>
										<a href="<%
										
										String ua = request.getHeader( "User-Agent" );
										boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
										boolean ok = !isMSIE || (ua.toUpperCase().contains("MSIE 9") || ua.toUpperCase().contains("MSIE 1"));
										
										if (ok) {
											out.print("../Fsk4.html");
										} else {
											out.print("navegadornosoportado.html");
										}
										
										%>" class="button">Empezar a diseñar!</a>
										<img src="images/moviles.png" style="float: right; width: 200px;">
									</div>
								</div>
							</div>
						</div>

					<!-- Features -->

						<div class="row">
							<div class="4u">
								<section class="first">
									<h2>1. Imagina</h2>
									<p>Es tan sencillo como imaginar. Se trata de dar forma a la habitación. Dibujar paredes, colocar ventanas... cambiar texturas, parquets, baldosas...</p>
								</section>							
							</div>
							<div class="4u">
								<section>
									<h2>2. Escoge</h2>
									<p>Coloca muebles, lavabos, armarios, cocinas, electrodomésticos... cambia texturas, coloración, dimensiones...</p>
								</section>							
							</div>
							<div class="4u">
								<section>
									<h2>3. Visualiza</h2>
									<p>Un simple click, das la órden y listo. Visualizas el diseño en 3D, lo guardas y lo envías por email. El éxito está asegurado</p>
								</section>							
							</div>							
						</div>

					<!-- Divider -->

						<div class="row">
							<div class="12u">
								<div class="divider divider-top"></div>
							</div>
						</div>

					<!-- Highlight Box -->

						<div class="row">
							<div class="12u">
								<div class="highlight-box">
								<!-- 
									<h2>El éxito para todos está asegurado</h2>
									-->
									<h2 style="line-height: 1em;">Si es un fabricante y está interesado en incluir su producto por favor no dude en <a href="mailto: pere@feskteva.com">contactar</a> con nosotros</h2>
									<!--<span>(tempus aliquam lorem blandit etiam suspendisse dapibus)</span>-->
								</div>
							</div>
						</div>

					<!-- Divider -->
						<div class="row">
							<div class="12u">
								<div class="divider divider-bottom"></div>
							</div>
						</div>

					<!-- Thumbnails -->

						<div class="row">
							<div class="12u">
								<section class="thumbnails first last">
									<div>
										<div class="row">
											<h2>Cocina</h2>
											<%
											{
											String[][] ids = Helper.sqlString("sql: select ren_render from fsk_render where ren_homecocina order by ren_id desc limit 3");
											for (String[] id : ids) { %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="http://www.feskteva.com/Fsk4.html?idficherorender=<%=id[0]%>"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=<%=id[0]%>"  data-toggle="tooltip" title="Haz clic para abrir este plano" width="200" height="150" /></a>
												</div>
											</div>
											<% } 
											} %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>


										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Baño</h2>
											<%
											{
											String[][] ids = Helper.sqlString("sql: select ren_render from fsk_render where ren_homebanyo order by ren_id desc limit 3");
											for (String[] id : ids) { %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="http://www.feskteva.com/Fsk4.html?idficherorender=<%=id[0]%>"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=<%=id[0]%>"  data-toggle="tooltip" title="Haz clic para abrir este plano" width="200" height="150" /></a>
												</div>
											</div>
											<% } 
											} %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Salón</h2>
											<%
											{
											String[][] ids = Helper.sqlString("sql: select ren_render from fsk_render where ren_homesalon order by ren_id desc limit 3");
											for (String[] id : ids) { %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="http://www.feskteva.com/Fsk4.html?idficherorender=<%=id[0]%>"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=<%=id[0]%>"  data-toggle="tooltip" title="Haz clic para abrir este plano" width="200" height="150" /></a>
												</div>
											</div>
											<% } 
											} %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Dormitorio</h2>
											<%
											{
											String[][] ids = Helper.sqlString("sql: select ren_render from fsk_render where ren_homedormitorio order by ren_id desc limit 3");
											for (String[] id : ids) { %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="http://www.feskteva.com/Fsk4.html?idficherorender=<%=id[0]%>"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=<%=id[0]%>"  data-toggle="tooltip" title="Haz clic para abrir este plano" width="200" height="150" /></a>
												</div>
											</div>
											<% } 
											} %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>

										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Negocio</h2>
											<%
											{
											String[][] ids = Helper.sqlString("sql: select ren_render from fsk_render where ren_homenegocio order by ren_id desc limit 3");
											for (String[] id : ids) { %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="http://www.feskteva.com/Fsk4.html?idficherorender=<%=id[0]%>"><img src="http://www.feskteva.com/miniatura.jsp?idfichero=<%=id[0]%>"  data-toggle="tooltip" title="Haz clic para abrir este plano" width="200" height="150" /></a>
												</div>
											</div>
											<% } 
											} %>
											<div class="3u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>

<!--

										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Baño</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Salón</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Comedor</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Dormitorio</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Estudio - loft</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Oficina</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

										<div class="row">
											<h2>Jardín</h2>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/preview.png" alt="" /></a>
												</div>
											</div>
											<div class="2u">
												<div class="thumbnail first">
													<a href="#"><img src="images/watermark.png" alt="" /></a>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="12u">
												<div class="divider"></div>
											</div>
										</div>

-->
									</div>
								</section>
							</div>
						</div>

					<!-- Divider -->
<!--
						<div class="row">
							<div class="12u">
								<div class="divider divider-top"></div>
							</div>
						</div>
-->						
					<!-- CTA Box -->
<!--
						<div class="row">
							<div class="12u">
								<div class="cta-box">
									<span>Amet lorem varius tempus consequat lorem?</span>
									<a href="#" class="button">Ipsum Consequat</a>
								</div>
							</div>
						</div>
-->
				</div>
			</div>

		<!-- Footer -->

			<div id="footer-wrapper">
				<footer class="container" id="site-footer">
					<div class="row">
<!--
						<div class="3u">
							<section class="first">
								<h2>Enlaces</h2>
								<ul class="link-list">
									<li><a href="#">Galería</a>
									<li><a href="#">Iniciar sesión</a>
									<li><a href="#">Registrarse</a>
								</ul>
							</section>
						</div>
						<div class="3u">
							<section>
								<h2>Enlaces</h2>
								<ul class="link-list">
									<li><a href="#">Haz tu casa</a>
									<li><a href="#">Ayuda</a>
								</ul>
							</section>
						</div>
-->
						<div class="3u">
							<section>
								<h2>Enlaces</h2>
								<ul class="link-list">
									<li><a href="contacto.html">Contacto</a>
									<li><a href="quienessomos.html">Quienes somos</a>
								</ul>
							</section>
						</div>
						<div class="3u">
							<section class="last">
								<h2>Extras</h2>
								<ul class="link-list">
									<li><a href="informacionparaempresas.html">Información para empresas</a>
								</ul>
							</section>
						</div>
					</div>
					<div class="row">
						<div class="12u">
							<div class="divider"></div>
						</div>
					</div>
					<div class="row">
						<div class="12u">
							<div id="copyright">
								&copy; 2013 www.feskteva.com. | <a href="lopd.html">Términos y condiciones</a>
							</div>
						</div>
					</div>
				</footer>
			</div>

<script>

var firstDot = window.location.hostname.indexOf('.');
var tld = ".net";
var isSubdomain = firstDot < window.location.hostname.indexOf(tld);
var domain;

if (isSubdomain == true) {
    domain = window.location.hostname.substring(firstDot == -1 ? 0 : firstDot + 1);
}
else {
  domain = window.location.hostname;
}

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-41617717-1']);
_gaq.push(['_setDomainName', domain]);
_gaq.push(['_setAllowLinker', true]);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript';
  ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 
  'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0];
  s.parentNode.insertBefore(ga, s);
})();

</script>
	</body>
</html>
