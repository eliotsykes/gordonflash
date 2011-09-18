<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <title>Fixture</title>
      <script type="text/javascript" src="${resource(dir:'js',file:'jquery-1.6.4.min.js')}"></script>
  </head>
  <body>
    Fixture called
    <a href="#" id="ajaxTriggerLink">Make AJAX call</a>
    <p id="ajaxMessage">No message</p>
    <g:javascript>
        $('#ajaxTriggerLink').live('click', function(e) {
            e.preventDefault();
            $.get('/fixture/htmlResponse', function() {
                $('#ajaxMessage').html("AJAX request completed");

            });
        });
    </g:javascript>
  </body>
</html>