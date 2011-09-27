# Gordon Flash Grails Plugin

Gordon Flash Grails plugin protects the flash scope from being cleared out by static file requests, AJAX requests, non-HTML responses, error responses, and explicitly protected requests.

The default config will be good enough to protect the flash scope in most cases. In special cases, you can explicitly protect the flash scope during a request by calling `request.setAttribute(GordonFlashFilter.PROTECT_FLASH_SCOPE, true)`

To install the latest version in your app: `grails install-plugin gordon-flash`


### DEVELOPER NOTES

##### Releasing+packaging plugin
1. Remove -DEV suffix from version number in GordonFlashGrailsPlugin.groovy
2. Add release notes to CHANGELOG and update version number
3. Run `grails package-plugin`
4. Push new plugin zip file to git repo
5. Tag with: `git tag "v0.x" && git push --tags`
6. Release to the grails plugin repo **without source** (source is on github) using: `grails release-plugin -zipOnly`
7. Increment version in GordonFlashGrailsPlugin.groovy to 0.x+1-DEV
8. Push to git repo

##### Running tests
`grails test-app functional:`

##### Running app
`grails run-app`

##### Enable debugging
Edit Config.groovy log4j config

### AUTHOR
Developed by Eliot Sykes <https://github.com/eliotsykes>, contributions welcome
