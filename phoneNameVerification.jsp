<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.owasp.encoder.Encode" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>WSO2 custom check </title>
    <link rel="stylesheet" href="themes/style.css"/>
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
</head>
<body>
<div class="header_register">
    <h2 class="heading"><span
            class="heading_register_part1">Login</span>
        <span class="heading_register_part2">Page</span>
    </h2>
</div>

<div class="wrapper">
    <div class="inner">
        <form action="https://localhost:9443/commonauth" method="post" data-ajax="false">
            <div class="form-wrapper">
                <div class="field">
                    <div class="ui fluid left icon input">
                        <input
                                type="text"
                                id="userName"
                                name="userName"
                                value=""
                                autocomplete="off"
                                tabindex="2"
                                placeholder="Username" required>
                        <i aria-hidden="true" class="lock icon"></i>
                    </div>
                </div>
                <div class="field">
                    <div class="ui fluid left icon input">
                        <input
                                type="text"
                                id="mobile"
                                name="mobile"
                                value=""
                                autocomplete="off"
                                tabindex="2"
                                placeholder="mobile" required>
                        <i aria-hidden="true" class="lock icon"></i>
                    </div>
                </div>
                <i class="zmdi zmdi-caret-down" style="font-size: 17px"></i>
                <div class="form-wrapper">
                    <input type="hidden" name="sessionDataKey" value='<%=Encode.forHtmlAttribute(request.getParameter("sessionDataKey"))%>'/>
                </div>
            </div>
            <button type="submit" class="submit_btn"> submit </button>
        </form>
    </div>
</div>
</body>
</html>