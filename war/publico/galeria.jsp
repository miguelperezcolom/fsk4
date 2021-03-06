<%@page import="fsk4.server.Web"%><%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><!DOCTYPE HTML>
<!--
	Arcana 2.0 by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>FesKteva - galería</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="Los últimos diseños realizados con FesKteva, la herramienta 3D que te ayuda a diseñar tu casa." />
		<meta name="keywords" content="3D room floor planner design tool" />
		<meta name="google-translate-customization" content="a437d09973638e4d-2916b7d2bcd82524-geff7d8ad352b08ef-10"></meta>
		<link href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300,300italic,700" rel="stylesheet" />
		<script src="js/jquery.min.js"></script>
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
									<li><a href="index.jsp">Inicio</a></li>
									<li><a href="perfiles.jsp">Perfiles</a></li><li class="current_page_item"><a href="galeria.jsp">Galería</a></li>
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
						<div class="12u">
								<section class="thumbnails first last">
									<div>

											<h2>Diseños más recientes</h2>


											<%=Web.galeria(request, response) %>
																			
									</div>
								</section>
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
									<li><a href="#">GalerÃ­a</a>
									<li><a href="#">Iniciar sesiÃ³n</a>
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
									<li><a href="informacionparaempresas.html">InformaciÃ³n para empresas</a>
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
								&copy; 2013 www.feskteva.com. | <a href="lopd.html">TÃ©rminos y condiciones</a>
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
