package com.srima.contactupdate;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button contacts, updatecontact;
    int PICK_CONTACT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = (Button) findViewById(R.id.but1);
        updatecontact = (Button) findViewById(R.id.but2);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        updatecontact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateContact("Appa", "9441363631");

            }
        });
    }
        private void updateContact(String name, String phone) {
            ContentResolver cr = getContentResolver();

            String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ? AND " +
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";
            String[] params = new String[] {name,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};

            Cursor phoneCur = managedQuery(ContactsContract.Data.CONTENT_URI, null, where, params, null);

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            if ( (null == phoneCur)  ) {
                createContact(name, phone);
            } else {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, params)
                        .withValue(ContactsContract.CommonDataKinds.Phone.DATA, phone)
                        .build());
            }

            phoneCur.close();

            try {
                cr.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Toast.makeText(MainActivity.this, "Updated the phone number of 'Sample Name' to: " + phone, Toast.LENGTH_SHORT).show();
        }

    private void createContact(String name, String phone) {
    }

}
