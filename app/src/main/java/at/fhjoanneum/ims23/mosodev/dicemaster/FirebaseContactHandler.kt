package at.fhjoanneum.ims23.mosodev.dicemaster
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseContactHandler {

    private val db: FirebaseFirestore = Firebase.firestore
    private val contactsRef = db.collection("contacts")

    fun saveContacts(contactsList: List<Contact>) {
        for (contact in contactsList) {
            contactsRef.add(contact)
                .addOnSuccessListener {
                    // Handle individual success
                }
                .addOnFailureListener {
                    // Handle individual failure
                }
        }
    }

    fun retrieveContacts(onResult: (List<Contact>?) -> Unit) {
        contactsRef.get().addOnSuccessListener { snapshot ->
            val contacts = snapshot.documents.mapNotNull { it.toObject(Contact::class.java) }
            onResult(contacts)
        }.addOnFailureListener {
            // Handle failure
            onResult(null)
        }
    }
}