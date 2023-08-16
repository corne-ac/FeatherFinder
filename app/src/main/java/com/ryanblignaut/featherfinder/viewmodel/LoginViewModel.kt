package com.ryanblignaut.featherfinder.viewmodel

import com.google.firebase.auth.FirebaseUser

class LoginViewModel : BaseViewModel<FirebaseUser>() {
    

}

/*interface UserRepository {
    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return FireBase.registerUser(email, password)
    }
}*/


/*
@Suppress("UNCHECKED_CAST")
class ViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}

inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline customCreator: (() -> VM)? = null
): Lazy<VM> = viewModels(
    ownerProducer = ownerProducer,
    factoryProducer = customCreator?.let { { ViewModelFactory(it) } })
*/

