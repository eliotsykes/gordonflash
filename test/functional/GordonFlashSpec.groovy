import geb.spock.GebReportingSpec

import spock.lang.*

import pages.*

@Stepwise
class GordonFlashSpec extends GebReportingSpec {

    def "empty message on home page"() {
		when:
		to HomePage
		then:
		message == ''

	}

}