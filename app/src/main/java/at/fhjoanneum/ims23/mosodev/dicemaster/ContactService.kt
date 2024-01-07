package at.fhjoanneum.ims23.mosodev.dicemaster

import android.content.Context
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Contact(val id: String, val name: String, var numbers: ArrayList<String> = arrayListOf(), var emails: ArrayList<String> = arrayListOf())

class ContactHelper(private val context: Context) {
    suspend fun getPhoneContacts(): ArrayList<Contact> = withContext(Dispatchers.IO) {
        val contactsList = ArrayList<Contact>()
        val contactsCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        contactsCursor.use { cursor ->
            val idIndex = cursor?.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = cursor?.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (cursor != null && cursor.moveToNext()) {
                val id = cursor.getString(idIndex!!)
                val name = cursor.getString(nameIndex!!)
                contactsList.add(Contact(id, name))
            }
        }
        return@withContext contactsList
    }

    suspend fun getContactNumbers(): HashMap<String, ArrayList<String>> = withContext(Dispatchers.IO) {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        phoneCursor.use { cursor ->
            val contactIdIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor != null && cursor.moveToNext()) {
                val contactId = cursor.getString(contactIdIndex!!)
                val number = cursor.getString(numberIndex!!)
                contactsNumberMap.computeIfAbsent(contactId) { arrayListOf() }.add(number)
            }
        }
        return@withContext contactsNumberMap
    }

    suspend fun getContactEmails(): HashMap<String, ArrayList<String>> = withContext(Dispatchers.IO) {
        val contactsEmailMap = HashMap<String, ArrayList<String>>()
        val emailCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        emailCursor.use { cursor ->
            val contactIdIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (cursor != null && cursor.moveToNext()) {
                val contactId = cursor.getString(contactIdIndex!!)
                val email = cursor.getString(emailIndex!!)
                contactsEmailMap.computeIfAbsent(contactId) { arrayListOf() }.add(email)
            }
        }
        return@withContext contactsEmailMap
    }

    suspend fun fetchContacts(): ArrayList<Contact> {
        val contactsList = getPhoneContacts()
        val contactNumbers = getContactNumbers()
        val contactEmails = getContactEmails()

        contactsList.forEach { contact ->
            contactNumbers[contact.id]?.let { numbers ->
                contact.numbers = numbers
            }
            contactEmails[contact.id]?.let { emails ->
                contact.emails = emails
            }
        }

        return contactsList
    }
}
