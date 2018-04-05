package io.kamara.runtimepermissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks {
    companion object {
        const val TAG = "MainActivity"
        const val RC_READ_CONTACTS = 113
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            readContacts()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_easy_permissions -> {
                startActivity(Intent(this, EasyPermissionsActivity::class.java))
            }
            R.id.nav_permissions -> {
                startActivity(Intent(this, PermissionsActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun readContacts() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)){
            Log.d(TAG, "Permissions grated to read contacts")

        } else{
            Log.d(TAG, "Permissions not grated to read contacts, requesting permission now! ")
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_read_contacts),
                    RC_READ_CONTACTS,
                    Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "Permissions granted for requestCode:" + requestCode)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "Permissions denied for request with requestCode:" + requestCode)

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // This will display a dialog directing them to enable the permission in app settings.
            Log.d(TAG, "Some permission permanently denied, showing settings dialog now!")
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.d(TAG, "Incoming result from app settings")
        }
    }

}
