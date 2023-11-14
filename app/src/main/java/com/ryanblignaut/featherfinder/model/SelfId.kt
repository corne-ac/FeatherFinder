package com.ryanblignaut.featherfinder.model

import com.google.firebase.firestore.Exclude

open class SelfId {
    @Exclude
    @JvmField
    var selfId: String = ""
}