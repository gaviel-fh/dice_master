package at.fhjoanneum.ims23.mosodev.dicemaster

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiceFragment : Fragment() {
    
    lateinit var advantageRoll1: TextView
    lateinit var advantageRoll2: TextView

    lateinit var advantageRadioButton: RadioButton
    lateinit var straightRadioButton: RadioButton
    lateinit var disadvantageRadioButton: RadioButton

    lateinit var d4Button: Button
    lateinit var d6Button: Button
    lateinit var d8Button: Button 
    lateinit var d10Button: Button 
    lateinit var d20Button: Button 
    lateinit var resultsTextView: TextView 
    lateinit var dieRolledView: TextView

    private val rolls: IntArray = intArrayOf(-1, -1)
    // represents the die currently being used to roll (dis)advantage
    private var advantageDie: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadContacts()

        d4Button = view.findViewById(R.id.d4Button)
        d6Button = view.findViewById(R.id.d6Button)
        d8Button = view.findViewById(R.id.d8Button)
        d10Button = view.findViewById(R.id.d10Button)
        d20Button = view.findViewById(R.id.d20Button)

        advantageRoll1 = view.findViewById(R.id.advantageRoll1)
        advantageRoll2 = view.findViewById(R.id.advantageRoll2)

        advantageRadioButton = view.findViewById(R.id.advantageRadioButton)
        straightRadioButton = view.findViewById(R.id.straightRadioButton)
        disadvantageRadioButton = view.findViewById(R.id.disadvantageRadioButton)

        resultsTextView = view.findViewById(R.id.resultsTextView)
        dieRolledView = view.findViewById(R.id.dieRolledView)

        advantageRadioButton.setOnClickListener {
            advantageRoll1.text = ""
            advantageRoll2.text = ""
            advantageDie = -1
            resultsTextView.text = ""
            dieRolledView.text = "die rolled"

        }
        straightRadioButton.setOnClickListener {
            advantageRoll1.text = ""
            advantageRoll2.text = ""
            advantageDie = -1
            resultsTextView.text = ""
            dieRolledView.text = "die rolled"
        }
        disadvantageRadioButton.setOnClickListener {
            advantageRoll1.text = ""
            advantageRoll2.text = ""
            advantageDie = -1
            resultsTextView.text = ""
            dieRolledView.text = "die rolled"
        }

        d4Button.setOnClickListener {
            rollDie(4)
        }
        d6Button.setOnClickListener {
            rollDie(6)
        }
        d8Button.setOnClickListener {
            rollDie(8)
        }
        d10Button.setOnClickListener {
            rollDie(10)
        }
        d20Button.setOnClickListener {
            rollDie(20)
        }
    }
    fun rollDie(dieSize: Int) {
        val dice = Dice(dieSize)
        val result = dice.roll()

        dieRolledView.text = "d$dieSize"
        resultsTextView.text = result.toString()

        if (advantageRadioButton.isChecked || disadvantageRadioButton.isChecked) {
            if (rolls[0] == -1) {
                // First roll
                rolls[0] = result
                advantageRoll1.text = result.toString()
                advantageRoll2.text = ""
            } else {
                // Second roll
                advantageRoll2.text = result.toString()
                val finalResult = if (advantageRadioButton.isChecked) maxOf(rolls[0], result) else minOf(rolls[0], result)
                resultsTextView.text = finalResult.toString()

                // Reset for the next set of rolls
                rolls[0] = -1
                rolls[1] = -1
            }
        } else {
            // Reset if not rolling with advantage or disadvantage
            rolls[0] = -1
            rolls[1] = -1
        }

        val handler = SocketProcessHandler("192.168.56.102", 4444)
        Thread(handler).start()
    }

    private fun loadContacts() {
        // Check for READ_CONTACTS permission
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val contactHelper = ContactHelper(requireContext())
                val contacts = contactHelper.fetchContacts()
                val myContacts = contacts.toString()
            } catch (e: Exception) {
                e.printStackTrace()  // Log the exception
            }
        }
    }

    companion object {
        private const val REQUEST_READ_CONTACTS = 1
    }
}