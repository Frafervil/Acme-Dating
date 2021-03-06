<%--
 * register.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
		

	<form:form action="company/register.do" modelAttribute="companyForm" onsubmit="return validatePhone('Phone does not match the pattern', ${countryCode})">
		
		<form:hidden path="id" />
		
		<fieldset>
    	<legend><spring:message code="company.fieldset.personalInformation"/></legend>
    	<acme:textbox code="company.commercialName" path="commercialName" placeholder="HP"/>
		<acme:textbox code="company.name" path="name" placeholder="Homer"/>
		<acme:textbox code="company.surname" path="surname" placeholder="Simpson"/>
		<acme:textbox code="company.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
		<acme:textbox code="company.phone" path="phone" placeholder="+34 600364231"/>
		<acme:textbox code="company.email" path="email" placeholder="homerjsimpson@email.com"/>
		
		</fieldset>
		
		<fieldset>
    	<legend><spring:message code="creditCard"/></legend>
		<acme:textbox code="creditCard.holderName" path="holderName" placeholder="Homer Simpson"/>
		<acme:textbox code="creditCard.brandName" path="brandName" placeholder="Mastercard"/>
		<acme:textbox code="creditCard.number" path="number"/>
		<acme:textbox code="creditCard.expirationMonth" path="expirationMonth"/>
		<acme:textbox code="creditCard.expirationYear" path="expirationYear"/>
		<acme:textbox code="creditCard.CVV" path="CVV" placeholder="123"/>
		
		</fieldset>
		
		<fieldset>
    	<legend><spring:message code="company.fieldset.companyAccount"/></legend>
		<acme:textbox code="company.username" path="username" placeholder="HomerS"/>
		
		<acme:password code="company.password" path="password"/>
		<acme:password code="company.passwordChecker" path="passwordChecker"/>
		
		</fieldset>
		<br/>
	
		<acme:checkbox code="company.confirmTerms" path="checkBox"/>
		
		<jstl:if test="${cookie['language'].getValue()=='en'}">
			<a href="terms/englishTerms.do"><spring:message code="company.terms"/></a>
			<br/>
			<br/>	
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='es'}">
			<a href="terms/terms.do"><spring:message code="company.terms"/></a>
		</jstl:if>
		
		<input type="submit" name="register" id="register"
		value="<spring:message code="company.save" />" >&nbsp; 
		
		<acme:cancel url="welcome/index.do" code="company.cancel"/>
	</form:form>
