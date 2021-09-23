package com.nikhil.mandlik.readsms.utility

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.RationaleDialogFragment

object PermissionHelper {

    fun hasReadSmsPermission(context : Context) =   EasyPermissions.hasPermissions(context, Manifest.permission.READ_SMS)


     fun requestPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "This Application cannot work without Read SMS Permission",
            Constants.REQ_READ_SMS_CODE,
            Manifest.permission.READ_SMS
        )
    }
}