<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<jsp:include page="../modulos/header-estrutura.jsp" />
	<title>Consulta Nutricional</title>
</head>
<body>

	<jsp:include page="../modulos/header.jsp" />
	
	<div class="container">
	
		<form method="post" action="ConsultaNutricional">

			<script>
  	 		function habilitar(){  
	    		if(document.getElementById('check').checked){  
        			document.getElementById('inputText').disabled = false;  
    			} else {  
	        		document.getElementById('inputText').disabled = true;  
    				}  
			}  
			</script>
		
			<label style="cursor: pointer;">  
        	<input type="checkbox" id="check" onclick="habilitar();" /> Medicamentos  
    		</label> 
			<input type=text size='50' disabled="disabled" id="inputText">
			</br> </br>
			
			Alterações Gastrointestinais
			
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox1" value="option1"> Disfagia </label>
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox2" value="option2"> Pirose </label>
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox3" value="option3"> Náusea </label>
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox4" value="option4"> Vômitos </label>
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox5" value="option5"> Diarreia </label>
			<label class="checkbox inline"><input type="checkbox" id="inlineCheckbox6" value="option6"> Constipação </label>
			</div>

		</form>
	
	</div>
	
	<jsp:include page="../modulos/footer.jsp" />	

</body>
</html>