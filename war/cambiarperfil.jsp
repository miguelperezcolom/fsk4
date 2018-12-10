<%@page import="fsk4.server.Web"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%
    
    if (request.getParameter("login") != null) session.setAttribute("usuario", request.getParameter("login"));
    
    %><!DOCTYPE HTML>
<!--
	Arcana 2.0 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>FesKteva - cambiar perfil</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="La herramienta 3D que te ayuda a diseñar tu casa. The 3D tool that helps you to design your home." />
		<meta name="keywords" content="3D room floor planner design tool" />
		<meta name="google-translate-customization" content="a437d09973638e4d-2916b7d2bcd82524-geff7d8ad352b08ef-10"></meta>
		<link href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300,300italic,700" rel="stylesheet" />
		<script src="js/jquery.min.js"></script>
		<script src="js/config.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		<noscript>
			<link rel="stylesheet" href="css/skel-noscript.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-desktop.css" />
		</noscript>
		<!--[if lte IE 9]><link rel="stylesheet" href="css/style-ie9.css" /><![endif]-->
		<!--[if lte IE 8]><script src="js/html5shiv.js"></script><![endif]-->
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
									<li><a href="index.html">Inicio</a></li>
									<li class="current_page_item"><a href="perfiles.jsp">Perfiles</a></li>
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

			<div id="main-wrapper" class="subpage">
				<div class="container">
					<div class="row">
						<div class="12u skel-cell-mainContent">
					
							<!-- Content -->

								<article class="first last">
								
									<h2>Puedes utilizar Feskteva en 3 niveles, según tus necesidades</h2>


									<table class="tablaperfiles">
										<tr class="cabecera"><td>&nbsp;</td><td>Usuario gratuito</td><td>Profesional</td><td>Tienda</td></tr>
										<tr><td>Nº renders HD diarios</td><td>2</td><td>10</td><td>30</td></tr>
										<tr><td>Publicidad</td><td>Sí</td><td>No</td><td>No</td></tr>
										<tr><td>Su logo en los renders</td><td>No</td><td>No</td><td>Sí</td></tr>
										<tr><td>Precio</td><td>Gratis!</td><td>8 EUR/mes</td><td>25 EUR/mes</td></tr>
										<tr><td>&nbsp;</td>
											<td>&nbsp;<!--  <a href="../Fsk4.html" class="button">Empezar a diseñar!</a>--></td>
											<td><form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="P36QLY4L34SBQ">
<input type="image" src="https://www.paypalobjects.com/es_ES/ES/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal. La forma rápida y segura de pagar en Internet.">
<img alt="" border="0" src="https://www.paypalobjects.com/es_ES/i/scr/pixel.gif" width="1" height="1">
</form>
</td>
											<td><form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="5GJ5WKUT7Y59E">
<input type="image" src="https://www.paypalobjects.com/es_ES/ES/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal. La forma rápida y segura de pagar en Internet.">
<img alt="" border="0" src="https://www.paypalobjects.com/es_ES/i/scr/pixel.gif" width="1" height="1">
</form>

										</td></tr>
									</table>

									<div style="height: 280px;"></div>

								</article>							

						</div>
					</div>
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
