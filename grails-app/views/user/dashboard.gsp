<!DOCTYPE html>
<html>
<head>
	<meta name='layout' content='main'/>
    <title>DashBoard</title>
    <style type="text/css">
		#container {
		    margin-right: -300px;
		    float:left;
		    width:100%;
		}
		#content {
		    margin-right: 320px; /* 20px added for center margin */
		}
		#sidebar {
		    width:300px;
		    float:left
		}
		@media (max-width: 480px) {
		    #container {
		        margin-right:0px;
		        margin-bottom:20px;
		    }
		    #content {
		        margin-right:0px;
		        width:100%;
		    }
		    #sidebar {
		        clear:left;
		    }
		}
	</style>
</head>
<body>
<div style="background: #f1f2ea;">
	<div id="container">
	    <div id="content">
	    	<label>Dashboard</label>
	    	<div id="main_dashboard" style="display: block;">
				<div style="background:#b0e0e6">
				User information:<br>
				    <table>
				        <tr>
				        	<td>ID</td>
				            <td>Name</td>
				            <td>Email</td>
				            <td>MPhone</td>
				            <td>CreateDate</td>
				            <td>LastLoginTime</td>
				        </tr>
				        <g:each in="${userList}" var="user">
				        <tr>
				        	<td>${user.id}</td>
				            <td>${user.username}</td>
				            <td>${user.email}</td>
				            <td>${user.mobilePhone}</td>
				            <td>${user.createDate}</td>
				            <td>${user.lastLoginTime}</td>
				        </tr>
				        </g:each>        
				    </table>
			    </div>
			    
			    <div style="background:#f5f5f5">
			    Activity information:<br>
				    <table>
				        <tr>
				        	<td>ID</td>
				            <td>Name</td>
				            <td>Tag</td>
				            <td>Archived</td>
				        </tr>
				        <g:each in="${actList}" var="activity">
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
			    </div>
			    
			    <div style="background:#b0e0e6">
			    User Activity information:<br>
				    <table>
				        <tr>
				            <td>UserID</td>
				            <td>ActivityID</td>
				            <td>Relation</td>
				            <td>Status</td>
				        </tr>
				        <g:each in="${uaList}" var="ua">
				        <tr>
				            <td>${ua.user_id}</td>
				            <td>${ua.activity_id}</td>
				            <td>${ua.relation}</td>
				            <td>${ua.status}</td>
				        </tr>
				        </g:each>        
				    </table>
			    </div>
			    
			    <div style="background:#eeeeee">
			    Message information:<br>
				    <table>
				        <tr>
				            <td>id</td>
				            <td>fromType</td>
				            <td>fromID</td>
				            <td>toType</td>
				            <td>toID</td>
				            <td>DateTime</td>
				            <td>Context</td>
				        </tr>
				        <g:each in="${msgList}" var="msg">
				        <tr>
				            <td>${msg.id}</td>
				            <td>${msg.from_type}</td>
				            <td>${msg.from_id}</td>
				            <td>${msg.to_type}</td>
				            <td>${msg.to_id}</td>
				            <td>${msg.send_date_time}</td>
				            <td>${msg.context}</td>
				        </tr>
				        </g:each>        
				    </table>
			    </div>
			    
			    <div style="background:#b0e0e6">
			    User Message information:<br>
				    <table>
				        <tr>
				            <td>id</td>
				            <td>fromType</td>
				            <td>fromID</td>
				            <td>userID</td>
				            <td>MsgID</td>
				            <td>Signature</td>
				            <td>Status</td>
				        </tr>
				        <g:each in="${umList}" var="um">
				        <tr>
				            <td>${um.id}</td>
				            <td>${um.from_type}</td>
				            <td>${um.from_id}</td>
				            <td>${um.to_user_id}</td>
				            <td>${um.message_id}</td>
				            <td>${um.signature}</td>
				            <td>${um.status}</td>
				        </tr>
				        </g:each>        
				    </table>
			    </div>
			    
			    <div style="background:#eeeeee">
			    Invite Activity information:<br>
				    <table>
				        <tr>
				            <td>ID</td>
				            <td>InviterID</td>
				            <td>InviterName</td>
				            <td>InviteeEmail</td>
				            <td>ActivityID</td>
				            <td>InviteTime</td>
				            <td>Status</td>
				        </tr>
				        <g:each in="${iaList}" var="ia">
				        <tr>
				            <td>${ia.id}</td>
				            <td>${ia.inviter_id}</td>
				            <td>${ia.inviter_name}</td>
				            <td>${ia.invitee_email}</td>
				            <td>${ia.activity_id}</td>
				            <td>${ia.invite_time}</td>
				            <td>${ia.status}</td>
				        </tr>
				        </g:each>        
				    </table>
			    </div>
		    </div>
		</div>
	</div>
	<div id="sidebar">
		<label onClick="DisplayMainDashboard()">Display all</label><br>
		${currentUser.username}
	</div>
	<div style="clear:both"></div>
</div>
<!-- dashboard main -->
<script>
    
    function DisplayMainDashboard(){
    	document.getElementById("main_dashboard").style.display = "block";
    }
</script>
<!-- end  -->
</body>
</html>