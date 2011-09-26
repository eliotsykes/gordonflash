# Gordon Flash Grails Plugin

Gordon Flash Grails plugin protects the flash scope from being cleared out by static file requests, AJAX requests, non-HTML responses, error responses, and explicitly protected requests.

At time of writing this plugin is only available from the GitHub repo <https://github.com/eliotsykes/gordonflash>

Download the latest packaged plugin from <https://github.com/eliotsykes/gordonflash/grails-gordon-flash-VERSION.zip>

### DEVELOPER NOTES

##### Releasing+packaging plugin
1. Increment version number in GordonFlashGrailsPlugin.groovy
2. Run `grails package-plugin`
3. Push new plugin zip file to git repo

##### Running tests
`grails test-app functional:`

##### Running app
`grails run-app`

##### Enable debugging
Edit Config.groovy log4j config