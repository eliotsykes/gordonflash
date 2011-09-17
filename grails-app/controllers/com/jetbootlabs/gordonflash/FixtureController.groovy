package com.jetbootlabs.gordonflash

class FixtureController {

    def index = {
        flash.message = params.message
        if (params.redirect) redirect(uri:'/')
    }
}
