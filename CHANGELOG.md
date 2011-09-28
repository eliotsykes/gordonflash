# 0.5
* Stopped the plugin's test dependencies from being included in apps that use this plugin

# 0.4
* Simplified design, no longer have to save previous flash state and rollback. We only wipe flash scope when needed at *end* of the request.
* By removing need to save previous state, we can no longer get flash scope into a dodgy state from simultaneous rolled-back requests
* Tidied up docs

# 0.3
* 4xx and 5xx error responses no longer clear flash scope
* Flash scope can now be explicitly protected through request-scoped attribute GordonFlashFilter.PROTECT_FLASH_SCOPE

# 0.2
* Initial release