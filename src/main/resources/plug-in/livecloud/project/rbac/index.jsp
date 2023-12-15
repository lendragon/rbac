<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title>test</title>
      <script src="js/jquery-3.3.1.min.js"></script>
  </head>
  <body>
    <button onclick="testInterface()">123</button>
    <button id="btn">345</button>
    <script>
      function testInterface() {
        /*$.get("${pageContext.request.contextPath}/livecloud/project/user.pagex", (res) => {
          alert(res);
          console.log(res);
        });*/
        $.post("${pageContext.request.contextPath}/livecloud/project/user.pagex", {"action": "query"}, (res) => {
          alert(res);
        });

        $.ajax();
      }
      $("#btn").click(testInterface);
    </script>
  </body>
</html>