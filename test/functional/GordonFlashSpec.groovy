import geb.spock.GebReportingSpec

import spock.lang.*

import pages.*

@Stepwise
class GordonFlashSpec extends GebReportingSpec {

    static String messageFromVultan = "GORDON'S ALIVE!"
    static String messageFromKala = "Dispatch war rocket Ajax to bring back his body."
    static String messageFromZarkov = "Don't empty my mind! I've spent my whole life filling it!"

    def "empty message on home page"() {
        given: "no flash message set"

        when: "making a request"
		to HomePage

        then: "no flash message is present"
		flashMessage == ''
	}

    def "flash message displayed only on next request after it was set"() {
        given: "flash message set in 1st request"
        go "fixture", message: messageFromVultan

        when: "making a 2nd request"
        to HomePage

        then: "the flash message is present"
        flashMessage == messageFromVultan

        when: "making a 3rd request"
        to HomePage

        then: "the flash message is not present"
        flashMessage == ''
    }

    def "flash message displayed after a redirect in which it was set"() {
        when: "flash message set in redirect request"
        go "fixture", message: messageFromKala, redirect: true

        then: "the flash message is present"
        at HomePage
        flashMessage == messageFromKala

        when: "making a subsequent request"
        to HomePage

        then: "the flash message is not present"
        flashMessage == ''
    }
}