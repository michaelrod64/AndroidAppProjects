package edu.miami.c10656908.photoemail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_SELECT_PICTURE = 1;
    private static final int ACTIVITY_SELECT_CONTACT = 2;
    private static final int ACTIVITY_SEND_EMAIL = 3;
    Uri selectedURI;
    String selectedURIString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent galleryIntent;

        galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,ACTIVITY_SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri contactData;
        Cursor contactsCursor = null;
        String contactName = "";
        int contactID;
        String[] emailAddress = new String[1];


        Intent nextIntent;
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case ACTIVITY_SELECT_PICTURE:
                if(resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();
                    selectedURIString = selectedURI.toString();
                }
                nextIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(nextIntent,ACTIVITY_SELECT_CONTACT);
                break;
            case ACTIVITY_SELECT_CONTACT:

                if(resultCode == Activity.RESULT_OK) {
                    contactData = data.getData();
                    contactsCursor = getContentResolver().query(contactData, null, null, null, null);
                    if(contactsCursor.moveToFirst()) {
                        contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        contactID = contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        emailAddress[0] = searchForEmailAddressById(contactID);




                    }
                    nextIntent = new Intent(Intent.ACTION_SEND);
                    nextIntent.setType("plain/text");
                    System.out.println("THE STORED EMAIL: " + emailAddress);
                    nextIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                    nextIntent.putExtra(Intent.EXTRA_SUBJECT, "Here's a neat photo!");
                    nextIntent.putExtra(Intent.EXTRA_TEXT, "Hello, " + contactName + " here's an awesome photo I wanted" +
                            "to share with you");
                    nextIntent.putExtra(Intent.EXTRA_STREAM, selectedURI);
                    startActivityForResult(nextIntent, ACTIVITY_SEND_EMAIL);
                }
                break;
            case ACTIVITY_SEND_EMAIL:
                try {
                    contactsCursor.close();
                }
                catch (Exception e) {

                }
                finish();
                break;
        }
    }
    private String searchForEmailAddressById(int contactId) {

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.DATA
        };
        Cursor emailCursor;
        String emailAddress;

        emailCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,projection,"CONTACT_ID = ?",
                new String[]{Integer.toString(contactId)},null);
        if (emailCursor.moveToFirst()) {
            emailAddress = emailCursor.getString(emailCursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA));
        } else {
            emailAddress = null;
        }
        return(emailAddress);
    }

}
