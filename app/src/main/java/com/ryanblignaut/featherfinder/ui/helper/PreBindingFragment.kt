package com.ryanblignaut.featherfinder.ui.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.tasks.CancellationTokenSource


/**
 * A base class that simplifies the management of fragment inflation and destruction.
 * This class automatically inflates the binding class for the fragment using reflection.
 * The binding class is the dynamically created fragment XML, represented by the type parameter T.
 * For example, if the fragment XML is fragment_categories_list.xml, T would be FragmentCategoriesListBinding.
 * The base class(T) provides a binding object to access the variables in the fragment XML.
 *
 * @param T the type of the binding class for the fragment
 */
abstract class PreBindingFragment<T : ViewBinding> : Fragment() {
    lateinit var cancellationTokenSource: CancellationTokenSource


    // The binding object used to access the variables in the fragment XML.
    private var _binding: T? = null

    /**
     * Returns the binding object for the fragment.
     * This property can only be accessed after the view has been created and will crash if accessed after destroyed.
     */
    protected val binding get() = _binding!!

    /**
     * Called when the fragment is created.
     * Override this method to add content to the view.
     */
    abstract fun addContentToView(savedInstanceState: Bundle?)

    /**
     * Called by the child class to inflate the binding for the fragment.
     * This method must be implemented to capture the T type.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to, or null if there is no parent view
     * @param attachToRoot Whether the fragment should be attached to the root of the parent view
     * @return The inflated binding object for the fragment
     */
    abstract fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean = false
    ): T

    /**
     * A generic function that inflates the binding for the fragment using reflection.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to, or null if there is no parent view
     * @return The inflated binding object for the fragment
     */
    inline fun <reified T : ViewBinding> inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): T {
        val inflateMethod = T::class.java.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        return inflateMethod.invoke(this, inflater, container, false) as T
    }

    /**
     * Called when the fragment view is being created.
     * Inflates the binding for the fragment and adds content to the view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to, or null if there is no parent view
     * @param savedInstanceState This fragment's previously saved state, if any
     * @return The root view of the fragment
     */
    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Create a CancellationTokenSource
        cancellationTokenSource = CancellationTokenSource()
        _binding = inflateBindingSelf(inflater, container, false)
        addContentToView(savedInstanceState)
        return binding.root
    }

    /**
     * This function is called when the fragment is destroyed.
     * Sets the binding to null to prevent memory leaks and free the context.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel any pending tasks.
        cancellationTokenSource.cancel()
        // Destroy the binding
        _binding = null
    }

}