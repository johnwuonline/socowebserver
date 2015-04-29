<html>
<%@ page import="grails.util.Holders" %>

<sec:ifNotSwitched>
	<sec:ifAllGranted roles='ROLE_SWITCH_USER'>
	<g:if test='${user.username}'>
	<g:set var='canRunAs' value='${true}'/>
	</g:if>
	</sec:ifAllGranted>
</sec:ifNotSwitched>

<head>
	<meta name='layout' content='main'/>
	<title>UserActivity</title>
</head>

<body>
	Your friend send you an invitation.
	Activity information:<br>
    <table>
        <tr>
        	<td>ID</td>
            <td>Name</td>
            <td>Tag</td>
            <td>Archived</td>
        </tr>
        <g:each in="${current_activity}" var="activity">
        <tr>
        	<td>${activity.id}</td>
            <td>${activity.name}</td>
            <td>${activity.tag}</td>
            <td>${activity.is_archived}</td>
            <g:each in="${aaList}" var="aa">
            	<g:if test="${activity.id == aa.activity_id}">
				    <td>${aa.name}:${aa.value}</td>
				</g:if>
            </g:each>
        </tr>
        </g:each>        
    </table>
</body>
</html>