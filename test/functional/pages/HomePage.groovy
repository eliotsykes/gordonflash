package pages

import geb.Page

class HomePage extends Page {

    static content = {
		heading { $("h1") }
		flashMessage { $("#message").text() }
	}

    static url = "/"

    static at = {
        heading.text() == 'Gordon Flash Grails Plugin Test'
    }

}