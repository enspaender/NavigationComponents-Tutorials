package com.smarttoolfactory.tutorial6_2navigationui_viewpager2_nestednavhost.navhost

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.smarttoolfactory.tutorial6_2naigationui_viewpager2_nestednavhost.R
import com.smarttoolfactory.tutorial6_2naigationui_viewpager2_nestednavhost.databinding.FragmentNavhostHomeBinding
import com.smarttoolfactory.tutorial6_2navigationui_viewpager2_nestednavhost.blankfragment.BaseDataBindingFragment
import com.smarttoolfactory.tutorial6_2navigationui_viewpager2_nestednavhost.blankfragment.HomeFragment1


/**
 * Navigation host fragment for the Home tab.
 *
 * * [findNavController] returns the main navController, not the one required for navigating
 * nested [Fragment]s. That navController is retrieved from [NavHostFragment] of this fragment.
 *
 * * Navigation graph of this fragment uses [HomeFragment1] so it navigates to it and
 * back stack entry count is one when  [HomeFragment1] is displayed
 */
class HomeNavHostFragment : BaseDataBindingFragment<FragmentNavhostHomeBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_home

     var navController: NavController? = null

    private val nestedNavHostFragmentId = R.id.nested_nav_host_fragment_home
    private val navGraphId = R.navigation.nav_graph_home

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("🏠 ${this.javaClass.simpleName} onAttach() ${this.javaClass.simpleName} #${this.hashCode()}")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("🏠 ${this.javaClass.simpleName} onCreate() ${this.javaClass.simpleName} #${this.hashCode()}")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("🏠 ${this.javaClass.simpleName} onCreateView() ${this.javaClass.simpleName} #${this.hashCode()}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            🔥 This is navController we get from findNavController not the one required
            for navigating nested fragments
         */
        val mainNavController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment
        navController = nestedNavHostFragment?.navController



        Toast.makeText(
            requireContext(),
            "mainNavController equals findNavController ${findNavController() == mainNavController}",
            Toast.LENGTH_SHORT
        ).show()

        println(
            "🏠 ${this.javaClass.simpleName} onViewCreated(): ${this.javaClass.simpleName} #${this.hashCode()}\n" +
                    "navController: $navController, findNavController(): ${findNavController()}, mainNavController: $mainNavController\n" +
                    "navController currentBackStackEntry: ${navController!!.currentBackStackEntry!!.destination}\n" +
                    "currentDestination: ${navController!!.currentDestination}\n" +
                    "current dest id: ${navController!!.currentDestination!!.id}, " +
                    "startDestination: ${navController!!.graph.startDestination}, " +
                    "graph start dest: ${navController!!.navInflater.inflate(navGraphId).startDestination}"
        )

        /*
            🔥 Alternative 1
            Navigate to HomeFragment1 if there is no current destination and current destination
            is start destination. Set start destination as this fragment so it needs to
            navigate next destination.

            If start destination is NavHostFragment it's required to navigate to first
         */
//        if (navController!!.currentDestination == null || navController!!.currentDestination!!.id == navController!!.graph.startDestination) {
//            navController?.navigate(R.id.homeFragment1)
//        }

        /*
            🔥 Alternative 2 Reset graph to default status every time this fragment's view is created
            ❌ This does not work if initial destination if this fragment because it repeats
            creating this fragment in an infinite loop since graph is created every time
         */
//        val navInflater = navController!!.navInflater
//        nestedNavHostFragment!!.navController.graph = graph
//        val graph = navController!!.navInflater.inflate(navGraphId)
//        nestedNavHostFragment!!.navController.graph = graph

        // listen back stack changes for this NavHost
//        listenBackStack()

        // Listen on back press
//        listenOnBackPressed()

    }

    private fun listenBackStack() {

        // Get NavHostFragment
        val navHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId)
        // ChildFragmentManager of the current NavHostFragment
        val navHostChildFragmentManager = navHostFragment?.childFragmentManager

        navHostChildFragmentManager?.addOnBackStackChangedListener {

            val backStackEntryCount = navHostChildFragmentManager.backStackEntryCount
            val fragments = navHostChildFragmentManager.fragments
            val currentDestination = navController?.currentDestination

            fragments.forEach {

                println(
                    " 🏠 ${this.javaClass.simpleName} handleOnBackPressed() " +
                            "fragment: ${it.javaClass.simpleName} #${it.hashCode()}," +
                            "backStackEntryCount: $backStackEntryCount, " +
                            " isVisible: ${it.isVisible}, " +
                            " isVisible: ${it.isVisible}, " +
                            ", isResumed: ${it.isResumed}, " +
                            "currentDestination: ${currentDestination!!}, DEST ID: ${currentDestination.id}, " +
                            "startDestination: ${navController!!.graph.startDestination}"
                )
            }


            Toast.makeText(
                requireContext(),
                "🏠 ${this.javaClass.simpleName} ChildFragmentManager backStackEntryCount: $backStackEntryCount",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun listenOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onResume() {
        super.onResume()
        println("🏠 ${this.javaClass.simpleName} onResume()")
        callback.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        println("🏠 ${this.javaClass.simpleName} onPause()")
        callback.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("🏠 ${this.javaClass.simpleName} onDestroyView()")
    }

    val callback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {

            // Get NavHostFragment
            val navHostFragment =
                childFragmentManager.findFragmentById(nestedNavHostFragmentId)
            // ChildFragmentManager of the current NavHostFragment
            val navHostChildFragmentManager = navHostFragment?.childFragmentManager

            val currentDestination = navController?.currentDestination
            val backStackEntryCount = navHostChildFragmentManager!!.backStackEntryCount

            val isAtStartDestination =
                (navController?.currentDestination?.id == navController?.graph?.startDestination)

            // Returns only the fragment on top
            val fragments = navHostChildFragmentManager.fragments
            fragments.forEach {
                println(
                    "🏠 ${this.javaClass.simpleName} handleOnBackPressed() " +
                            "fragment: ${it.javaClass.simpleName} #${it.hashCode()}," +
                            "backStackEntryCount: $backStackEntryCount, " +
                            " isVisible: ${it.isVisible}, " +
                            ", isResumed: ${it.isResumed}, " +
                            ", isAtStartDestination: ${isAtStartDestination}, " +
                            "navController currentBackStackEntry: ${navController!!.currentBackStackEntry!!.destination}\n" +
                            "CURRENT DEST: ${currentDestination!!},  CURRENT DEST ID: ${currentDestination.id}, " +
                            "START DEST: ${navController!!.graph.startDestination}"
                )
            }

            // Ist it the root of nested
            if (navController?.currentDestination?.id == navController?.graph?.startDestination) {

                Toast.makeText(requireContext(), "🏠 AT START DESTINATION ", Toast.LENGTH_SHORT)
                    .show()
                remove()
                requireActivity().onBackPressed()
            } else if (isVisible) {
                navController?.navigateUp()
            }

            Toast.makeText(
                requireContext(),
                "🏠 ${this.javaClass.simpleName} backStackEntryCount: $backStackEntryCount\n" +
                        "isAtStartDestination:  $isAtStartDestination, \n" +
                        "isVisible:  $isVisible, \n" +
                        "CURRENT DEST:  ${currentDestination!!.id}, " +
                        "START DEST: ${navController!!.graph.startDestination}",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

}