package com.mupper.gobus.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SingleFragmentActivity : AppCompatActivity()

fun SingleFragmentActivity.setFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(android.R.id.content, fragment)
        .commit()
}