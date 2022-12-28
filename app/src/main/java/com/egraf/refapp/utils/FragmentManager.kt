package com.egraf.refapp.utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

fun FragmentManager.close(vararg tags: String) {
    for (tag in tags) {
        val fragment =
            this.findFragmentByTag(tag)
        if (fragment != null) {
            this.commit {
                setReorderingAllowed(true)
                remove(fragment)
            }

        }
    }
}

