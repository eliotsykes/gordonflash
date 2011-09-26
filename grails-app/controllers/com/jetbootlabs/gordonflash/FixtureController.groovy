package com.jetbootlabs.gordonflash

import org.apache.http.HttpResponse
import javax.servlet.http.HttpServletResponse

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
        if (params.protectFlashScope) {
            request.setAttribute(GordonFlashFilter.PROTECT_FLASH_SCOPE, true)
        }
        render(text: "<html><head><title>Hello</title></head><body>Hello World</body></html>", contentType: "text/html", encoding: "UTF-8")
    }

    def fourOhFourResponse = {
        render(status: HttpServletResponse.SC_NOT_FOUND, text: "<html><head><title>Generated 404</title></head><body>Generated 404</body></html>", contentType: "text/html", encoding: "UTF-8")
        //response.sendError(HttpServletResponse.SC_NOT_FOUND) // DO NOT USE THIS as it is does not use the generated html
    }

    def errorResponse = {
        render(status: HttpServletResponse.SC_INTERNAL_SERVER_ERROR, text: "<html><head><title>Generated 500</title></head><body>Generated 500</body></html>", contentType: "text/html", encoding: "UTF-8")
    }

    def throwException = {
        throw new RuntimeException("Forced exception")
    }
}
