package com.nikhil.mandlik.readsms.repository

import android.content.ContentResolver
import android.provider.Telephony
import com.nikhil.mandlik.readsms.entity.SMS
import com.nikhil.mandlik.readsms.utility.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepository @Inject constructor() {


    private val projection = arrayOf(
        Telephony.Sms._ID,
        Telephony.Sms.ADDRESS,
        Telephony.Sms.DATE,
        Telephony.Sms.BODY
    )
    private val sortOrder = null
    private val selectionClause = "${Telephony.Sms.ADDRESS} = ? AND ${Telephony.Sms.DATE} > ?"


    suspend fun search(
        contentResolver: ContentResolver?,
        phoneNumber: String,
        noOfDays: Int
    ): MutableList<SMS> {
        val tempList = mutableListOf<SMS>()
        val selectionArgs = arrayOf(phoneNumber, getNoOfDaysTimeStamp(noOfDays).toString())
        val cursor = contentResolver?.query(
            Telephony.Sms.CONTENT_URI,
            projection,
            selectionClause,
            selectionArgs,
            sortOrder
        )
        cursor?.apply {
            while (moveToNext()) {
                tempList.add(
                    SMS(
                        id = getString(getColumnIndex(Telephony.Sms._ID)),
                        address = getString(getColumnIndex(Telephony.Sms.ADDRESS)),
                        body = getString(getColumnIndex(Telephony.Sms.BODY)),
                        date = getString(getColumnIndex(Telephony.Sms.DATE))
                    )
                )

            }

        }
        cursor?.close()
        return tempList
    }

    /**
     *  calculating timestamp for day from which you want to start fetching sms
     *  one day is equal to 86400000 milliseconds
     */
    private fun getNoOfDaysTimeStamp(noOfDays: Int) =
        System.currentTimeMillis() - (noOfDays * 86400000)
}