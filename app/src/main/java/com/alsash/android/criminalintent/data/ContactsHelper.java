package com.alsash.android.criminalintent.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

public class ContactsHelper {

    private Context mContext;

    ContactsHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ContactsHelper get(Context context) {
        return new ContactsHelper(context);
    }

    @Nullable
    public String getConcactPhoneByName(String contactName) {
        return getContactPhoneById(
                getContactIdByName(contactName)
        );
    }

    @Nullable
    public String getContactIdByName(@Nullable String contactName) {

        if (contactName == null) return null;

        String contactId = null;

        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,            // SELECT FROM content_uri
                new String[]{ContactsContract.Contacts._ID},      // id (column)
                ContactsContract.Contacts.DISPLAY_NAME + " = ?", // WHERE display_name =
                new String[]{contactName},                        // contact_name
                null
        );

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                contactId = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return contactId;
    }

    public String getContactPhoneById(@Nullable String contactId) {

        if (contactId == null) return null;

        String contactPhone = null;

        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                contactPhone = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return contactPhone;
    }
}
