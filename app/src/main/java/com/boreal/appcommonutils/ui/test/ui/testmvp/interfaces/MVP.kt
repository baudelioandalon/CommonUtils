package com.boreal.appcommonutils.ui.test.ui.testmvp.interfaces

interface MVP {
    interface View{
        fun updateUser()
    }
    interface Presenter{
        fun retrieveUser()
    }
    interface Interactor{
        fun getUser()
    }
}