package com.example.sellspot.model

import com.google.firebase.database.ServerValue

class Message {
    var message: String? = null
        private set
    var senderId: String? = null
        private set
    var timestamp // Change the type to Object
            : Any? = null
        private set

    constructor() {}
    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
        timestamp = ServerValue.TIMESTAMP // Use ServerValue.TIMESTAMP to get the server timestamp
    }
}