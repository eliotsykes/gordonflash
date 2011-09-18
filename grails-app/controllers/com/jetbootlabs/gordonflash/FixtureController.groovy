package com.jetbootlabs.gordonflash

class FixtureController {

    def index = {
        flash.message = params.message
        if (params.redirect) redirect(uri:'/')
    }

    def jsResponse = {
        render(text: "var test = 'hello';", contentType: "text/javascript")
    }

    def jsonResponse = {
        render(text: """{ "hello" : "world" }""", contentType: "application/json")
    }

    def cssResponse = {
        render(text: "body { width: 100%; }", contentType: "text/css")
    }

    def xmlResponse = {
        render(text: "<xml>some xml</xml>", contentType: "text/xml", encoding: "UTF-8")
    }

    def htmlResponse = {
        render(text: "<html><head><title>Hello</title></head><body>Hello World</body></html>", contentType: "text/html", encoding: "UTF-8")
    }
}
