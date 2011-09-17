package pages

import geb.Page

class HomePage extends Page {

    static content = {
		heading { $("h1") }
		message { $("span").text() }
	}

    static url = "/GordonFlash"

    static at = {
        heading.text() == 'Gordon Flash Grails Plugin Test'
    }

}