<!DOCTYPE html>
<html lang="en-GB">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	<title>Notification System</title>
	<link rel="stylesheet" href="/${context.path}/css/login.css" type="text/css"/>
</head>
<!-- page specific tags -->

<body class="controller-account action-login">
<div id="wrapper">

	<!-- div id="top-menu">
        <div id="account"></div>
    </div-->

	<div id="header">
		<h1>Notification System</h1>
	</div>

	<div id="main" class="nosidebar">
		<div id="content">

			<div id="login-form">
				<form accept-charset="UTF-8" action="/${context.path}/j_security_check" method="post">
					<input name="back_url" type="hidden">
					<table>
						<tbody>
						<tr>
							<td colspan="2" style="text-align:center; font-size:12px; color:red; font-weight: bold;">
								<div style="padding-bottom: 5px;">
									<%= request.getParameter("error") != null ? "Invalid user or password!" : "" %>
								</div>
							</td>
						</tr>
						<tr>
							<td style="text-align:right;"><label for="username">Login:</label></td>
							<td style="text-align:left;"><input id="username" name="j_username" tabindex="1" type="text"></td>
						</tr>
						<tr>
							<td style="text-align:right;"><label for="password">Password:</label></td>
							<td style="text-align:left;"><input id="password" name="j_password" tabindex="2" type="password"></td>
						</tr>
						<tr>
							<td></td>
							<td style="text-align:left;">
							</td>
						</tr>
						<tr>
							<td style="text-align:left;">
							</td>
							<td style="text-align:right;">
								<input type="submit" name="login" value="Login >>" tabindex="5">
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>

			<div style="clear:both;"></div>
		</div>
	</div>

	<div id="footer">
		<div class="bgl">
			<div class="bgr">
				Copyright &copy; <script>document.write(new Date().getFullYear())</script>. Suttons Motors Group. (Version: ${version.number})
			</div>
		</div>
	</div>

	<div id="ajax-indicator" style="display:none;"><span>Loading...</span></div>
	<div id="ajax-modal" style="display:none;"></div>

</div>
</body>
</html>