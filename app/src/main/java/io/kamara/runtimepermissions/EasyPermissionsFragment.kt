package io.kamara.runtimepermissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_easy_permissions.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * A placeholder fragment containing a simple view.
 */
class EasyPermissionsFragment : Fragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    companion object {
        const val TAG = "EasyPermissionsFragment"
        const val RC_CAMERA_AND_LOCATION = 111
        const val RC_RECORD_AUDIO = 112
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_easy_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRecordAudio.setOnClickListener { _ ->  recordAudio() }

        buttonCameraLocation.setOnClickListener{ _ -> takePictureWithLocation() }
    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private fun takePictureWithLocation() {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)

        // Check whether the permission has already been granted
        if (EasyPermissions.hasPermissions(context!!, *perms)){
            // Have permission, do the thing!
            Log.d(TAG, "Permissions grated to take picture with location")

        } else{
            // Permissions not granted, request them now
            Log.d(TAG, "Permissions not grated to take picture with location, requesting permissions now!")
            EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, *perms)
                            .setRationale(R.string.rationale_location_camera)
                            .setPositiveButtonText(R.string.rationale_ask_ok)
                            .setNegativeButtonText(R.string.rationale_ask_cancel)
                            .build())
        }
    }

    private fun recordAudio() {
        if (EasyPermissions.hasPermissions(context!!, Manifest.permission.RECORD_AUDIO)){
            Log.d(TAG, "Permissions grated to record audio")

        } else{
            Log.d(TAG, "Permissions not grated to record, requesting permission now! ")
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_audio),
                    RC_RECORD_AUDIO,
                    Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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


    override fun onRationaleDenied(requestCode: Int) {
        Log.d(TAG, "Rationale denied for requestCode:" + requestCode)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d(TAG, "Rationale accepted for requestCode:" + requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}
