import com.jetbootlabs.gordonflash.GordonFlashFilter

class GordonFlashGrailsPlugin {
    // the plugin version
    def version = "0.1"
    
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Eliot Sykes"
    def authorEmail = "eliotsykes gmail"
    def title = "Gordon Flash - tweaks to Grails Flash Scope"
    def description = '''\\
Gordon Flash Grails plugin ensures the flash scope is only wiped by full non-static HTML page request/responses.
Plugin stops static resource and AJAX requests from clearing out the flash scope.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/gordon-flash"

    def doWithWebDescriptor = { webXml ->
        // TODO: Ensure dispatchers are the same as for GrailsWebRequestFilter
        // TODO: Ensure appears before GrailsWebRequestFilter
        def contextParam = webXml.'context-param'
        contextParam[contextParam.size() - 1] + {
            'filter' {
                'filter-name'('gordonFlashFilter')
                'filter-class'(GordonFlashFilter.name)
            }
        }

        def filterMapping = webXml.'filter-mapping'.find {
            it.'filter-name'.text() == "charEncodingFilter"
        }

        filterMapping + {
            'filter-mapping' {
                'filter-name'('gordonFlashFilter')
                'url-pattern'('/*')
                'dispatcher'('FORWARD')
                'dispatcher'('ERROR')
            }

        }
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
