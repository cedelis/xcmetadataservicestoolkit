<%--
  * Copyright (c) 2009 eXtensible Catalog Organization
  *
  * This program is free software; you can redistribute it and/or modify it under the terms of the MIT/X11 license. The text of the
  * license can be found at http://www.opensource.org/licenses/mit-license.php and copy of the license can be found on the project
  * website http://www.extensiblecatalog.org/.
  *
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="mst" uri="mst-tags"%>

<c:import url="/st/inc/doctype-frag.jsp" />

<LINK href="page-resources/css/header.css" rel="stylesheet" type="text/css">


<html>
  <head>
    <title>Edit Service</title>
    <c:import url="/st/inc/meta-frag.jsp" />

    <LINK href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/base-mst.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/yui/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/global.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/main_menu.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/tables.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/header.css" rel="stylesheet" type="text/css">
    <LINK href="page-resources/css/bodylayout.css" rel="stylesheet" type="text/css">

    <SCRIPT LANGUAGE="JavaScript" SRC="page-resources/js/utilities.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" src="page-resources/yui/connection/connection-min.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" src="page-resources/yui/container/container_core-min.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="page-resources/yui/menu/menu-min.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="page-resources/js/main_menu.js"></SCRIPT>
    <SCRIPT LANGUAGE="JavaScript" SRC="page-resources/js/edit_service.js"></SCRIPT>
  </head>


<body class="yui-skin-sam">

  <!--  yahoo doc 2 template creates a page 950 pixles wide -->
  <div id="doc2"><!-- page header - this uses the yahoo page styling -->
  <div id="hd"><!--  this is the header of the page --> <c:import url="/st/inc/header.jsp" /> <!--  this is the header of the page --> <c:import url="/st/inc/menu.jsp" /> <jsp:include page="/st/inc/breadcrumb.jsp">

    <jsp:param name="bread" value="Services | <a href='listServices.action'><U> All Services </U> </a> | Edit Service" />

  </jsp:include></div>
  <!--  end header --> <!-- body -->
  <div id="bd"><!-- Display of error message --> <c:if test="${errorType != null}">
    <div id="server_error_div">
    <div id="server_message_div" class="${errorType}"><img src="${pageContext.request.contextPath}/page-resources/img/${errorType}.jpg"> <span class="errorText"> <mst:fielderror error="${fieldErrors}">
    </mst:fielderror> </span></div>
    </div>
  </c:if>
  <div id="error_div"></div>

  <div class="clear">&nbsp;</div>
    <form name="editService" method="post" action="listServices.action">
      <table style="margin-left: 10px" width="100%">
        <input type="hidden" name="serviceId" id="serviceId" value=${temporaryService.id} />
        <br />
        <tr>
          <td colspan="2">To update a service, see the <a style="text-decoration: underline; color: blue;" href="http://code.google.com/p/xcmetadataservicestoolkit/wiki/Metadata">user manual</a> for instructions.<br>
          </td>
        </tr>
        <br />
        <tr>
          <td>
            <button class="xc_button" onclick="javascript:YAHOO.xc.mst.services.alterService.cancel();" type="button" name="cancel">Done</button>
            &nbsp;&nbsp;
          </td>
        </tr>
      </table>
    </form>
  </div>
  <!--  this is the footer of the page --> <c:import url="/st/inc/footer.jsp" /></div>
</body>
</html>
