# 0.4-dev
* Simplified design, no longer have to save previous flash state and rollback. We only wipe flash scope when needed at *end* of the request.

# 0.3
* 4xx and 5xx error responses no longer clear flash scope
* Flash scope can now be explicitly protected through request-scoped attribute GordonFlashFilter.PROTECT_FLASH_SCOPE

# 0.2
* Initial release