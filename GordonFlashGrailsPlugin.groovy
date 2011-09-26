import com.jetbootlabs.gordonflash.GordonFlashFilter

class GordonFlashGrailsPlugin {
    // the plugin version
    def version = "0.3"
    
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    // TODO: Plugin excludes
    def pluginExcludes = [
            "grails-app/**",
            "scripts/**",
            "lib/**",
            "target/**",
            "test/**",
            "web-app/**",
            "README.txt",
            "*.log",
            ".gitignore",
            ".git/**",
            "*.iml",
            "*.ipr",
            "*.iws"
    ]

    def author = "Eliot Sykes"
    def authorEmail = "eliotsykes gmail"
    def title = "Gordon Flash Grails plugin protects flash scope from being cleared out unnecessarily"
    def description = '''\\
Gordon Flash Grails plugin protects the flash scope from being cleared out by static file requests, AJAX requests, and non-HTML responses.
'''

    def documentation = "http://grails.org/plugin/gordon-flash"

    def doWithWebDescriptor = { webXml ->
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
                'dispatcher'('REQUEST')
            }

        }
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }
}
