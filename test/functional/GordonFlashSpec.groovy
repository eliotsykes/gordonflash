import geb.spock.GebReportingSpec

import spock.lang.*

import pages.*

@Stepwise
class GordonFlashSpec extends GebReportingSpec {

    static String messageFromVultan = "GORDON'S ALIVE!"
    static String messageFromKala = "Dispatch war rocket Ajax to bring back his body."
    static String messageFromZarkov = "Don't empty my mind! I've spent my whole life filling it!"
    static String messageFromGordon = "This Ming is a psycho!"
    static String messageFromBarin = """Tell me more about this man, "Houdini"."""
    static String messageFromMing = "Klytus, I'm bored. What play thing can you offer me today?"
    static String messageFromKlytus = "An obscure body in the S-K System, your majesty. The inhabitants refer to it as the planet Earth."
    static String messageFromDale = "Oh, Flash!"

    def "empty message on home page"() {
        given: "no flash message set"

        when: "making a request"
		to HomePage

        then: "no flash message should be present"
		flashMessage == ''
	}

    def "flash message displayed only on next request after it was set"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromVultan

        when: "making a 2nd request"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromVultan

        when: "making a 3rd request"
        to HomePage

        then: "no flash message should be present"
        flashMessage == ''
    }

    def "flash message displayed after a redirect in which it was set"() {
        when: "setting flash message during a redirect request"
        go "fixture", message: messageFromKala, redirect: true

        then: "the flash message should be present"
        at HomePage
        flashMessage == messageFromKala

        when: "making a subsequent request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''
    }

    def "flash scope is not wiped by static file requests"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromZarkov

        when: "making a request to a static file"
        go staticFile

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromZarkov

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

        where:
        staticFile << ["test.html", "css/test.css", "images/test.jpg", "pages/index.html", "pages/", "pages"]
    }

    def "flash scope is not wiped by dynamic non-HTML responses"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromGordon

        when: "making a request to a static file"
        go uri

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromGordon

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

        where:
        uri << ["fixture/jsResponse", "fixture/jsonResponse", "fixture/cssResponse", "fixture/xmlResponse"]
    }

    def "flash scope is not wiped by AJAX requests"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromBarin

        when: "making AJAX request"
        $("#ajaxTriggerLink").click()
        waitFor { $("#ajaxMessage").text() == "AJAX request completed" }

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromBarin

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

    }

    def "flash scope is not wiped by app-server default 404 response"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromMing

        when: "making a request to app-server default 404 response"
        go "non-existent-resource"

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromMing

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

    }

     def "flash scope is not wiped by app-server default 500 response"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromMing

        when: "making a request to app-server default 500 response"
        go "fixture/throwException"

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromMing

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

    }

    def "flash scope is not wiped by generated 404 HTML response"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromKlytus

        when: "making a request to generated 404 HTML response"
        go "fixture/fourOhFourResponse"
        waitFor { $("body").text() == "Generated 404" }

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromKlytus

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

    }

    def "flash scope is not wiped by 500 server error HTML response"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromDale

        when: "making a request to 500 server error HTML response"
        go "fixture/errorResponse"
        waitFor { $("body").text() == "Generated 500" }

        and: "making a request for a non-static HTML response"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromDale

        when: "making a 3rd request"
        to HomePage

        then: "the flash message should not be present"
        flashMessage == ''

    }

    def "flash scope can be explicitly protected to not be wiped on a dynamic HTML response"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromVultan

        when: "making a 2nd request where flash scope is explicity protected"
        go "fixture/htmlResponse", protectFlashScope: true
        waitFor { $("body").text() == "Hello World" }

        and: "making a 3rd request"
        to HomePage

        then: "the flash message should be present"
        flashMessage == messageFromVultan

        when: "making a 4th request"
        to HomePage

        then: "no flash message should be present"
        flashMessage == ''
    }
}