package com.josep.ws.controllers;

import com.josep.ws.dto.Greeting;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class TestController {

    @GetMapping("/test")
    public @ResponseBody String index() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Hello WebSocket</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.4.1.min.js\" integrity=\"sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js\"></script>\n" +
                "\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<noscript><h2 style=\"color: #ff0000\">Seems your browser doesn't support Javascript! Websocket relies on Javascript being\n" +
                "    enabled. Please enable\n" +
                "    Javascript and reload this page!</h2></noscript>\n" +
                "<div id=\"main-content\" class=\"container\">\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-6\">\n" +
                "            <form class=\"form-inline\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"connect\">WebSocket connection:</label>\n" +
                "                    <button id=\"connect\" class=\"btn btn-default\" type=\"submit\">Connect</button>\n" +
                "                    <button id=\"disconnect\" class=\"btn btn-default\" type=\"submit\" disabled=\"disabled\">Disconnect\n" +
                "                    </button>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "        <div class=\"col-md-6\">\n" +
                "            <form class=\"form-inline\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <label for=\"name\">What is your name?</label>\n" +
                "                    <input type=\"text\" id=\"name\" class=\"form-control\" placeholder=\"Your name here...\">\n" +
                "                </div>\n" +
                "                <button id=\"send\" class=\"btn btn-default\" type=\"submit\">Send</button>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"row\">\n" +
                "        <div class=\"col-md-12\">\n" +
                "            <table id=\"conversation\" class=\"table table-striped\">\n" +
                "                <thead>\n" +
                "                <tr>\n" +
                "                    <th>Greetings</th>\n" +
                "                </tr>\n" +
                "                </thead>\n" +
                "                <tbody id=\"greetings\">\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<script type=\"text/javascript\">\n" +
                "    var stompClient = null;\n" +
                "\n" +
                "    function setConnected(connected) {\n" +
                "        $(\"#connect\").prop(\"disabled\", connected);\n" +
                "        $(\"#disconnect\").prop(\"disabled\", !connected);\n" +
                "        if (connected) {\n" +
                "            $(\"#conversation\").show();\n" +
                "        }\n" +
                "        else {\n" +
                "            $(\"#conversation\").hide();\n" +
                "        }\n" +
                "        $(\"#greetings\").html(\"\");\n" +
                "    }\n" +
                "\n" +
                "    function connect() {\n" +
                "        var socket = new SockJS('http://localhost:8080/josep-websocket');\n" +
                "        stompClient = Stomp.over(socket);\n" +
                "        stompClient.connect({}, function (frame) {\n" +
                "            setConnected(true);\n" +
                "            console.log('Connected: ' + frame);\n" +
                "            stompClient.subscribe('/topic/greetings', function (greeting) {\n" +
                "                showGreeting(JSON.parse(greeting.body).content);\n" +
                "            });\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "    function disconnect() {\n" +
                "        if (stompClient !== null) {\n" +
                "            stompClient.disconnect();\n" +
                "        }\n" +
                "        setConnected(false);\n" +
                "        console.log(\"Disconnected\");\n" +
                "    }\n" +
                "\n" +
                "    function sendName() {\n" +
                "        console.log(JSON.stringify({'name': $(\"#name\").val()}));\n" +
                "        stompClient.send(\"/app/hello\", {}, $(\"#name\").val());\n" +
                "    }\n" +
                "\n" +
                "    function showGreeting(message) {\n" +
                "        $(\"#greetings\").append(\"<tr><td>\" + message + \"</td></tr>\");\n" +
                "    }\n" +
                "\n" +
                "    $(function () {\n" +
                "        $(\"form\").on('submit', function (e) {\n" +
                "            e.preventDefault();\n" +
                "        });\n" +
                "        $( \"#connect\" ).click(function() { connect(); });\n" +
                "        $( \"#disconnect\" ).click(function() { disconnect(); });\n" +
                "        $( \"#send\" ).click(function() { sendName(); });\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>`";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting ws(String message) {
        System.out.println("masuk 1");
        try {
            Thread.sleep(1500);
        } catch (Exception e) {}

        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message) + "!");
    }

}
