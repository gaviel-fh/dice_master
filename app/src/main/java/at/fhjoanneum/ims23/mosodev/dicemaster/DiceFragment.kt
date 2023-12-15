package at.fhjoanneum.ims23.mosodev.dicemaster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.cubeofcheese.dndice.R
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
        val rand = Random().nextInt(dieSize) + 1
        dieRolledView.text = "d$dieSize"

        if (!advantageRadioButton.isChecked && !disadvantageRadioButton.isChecked) {
            resultsTextView.text = rand.toString()
        }

        if (advantageRadioButton.isChecked) {
            if (rolls[0] == -1) { // first roll
                advantageDie = dieSize
                rolls[0] = rand
                advantageRoll1.text = rand.toString()
            } else if (advantageRadioButton.isChecked && rolls[1] == -1) { // second roll
                if (dieSize == advantageDie) {
                    rolls[1] = rand
                    advantageRoll2.text = rand.toString()
                    resultsTextView.text = maxOf(rolls[0], rolls[1]).toString()
                } else { // treat as first roll
                    advantageDie = dieSize
                    rolls[0] = rand
                    advantageRoll1.text = rand.toString()
                }
            } else { // third roll, reset as first roll
                advantageDie = dieSize
                rolls[0] = rand
                rolls[1] = -1
                advantageRoll1.text = rand.toString()
                advantageRoll2.text = ""
                resultsTextView.text = ""

            }
        }
        if (disadvantageRadioButton.isChecked) {
            if (rolls[0] == -1) {
                advantageDie = dieSize
                rolls[0] = rand
                advantageRoll1.text = rand.toString()
            } else if (disadvantageRadioButton.isChecked && rolls[1] == -1) {
                if (dieSize == advantageDie) {
                    rolls[1] = rand
                    advantageRoll2.text = rand.toString()
                    resultsTextView.text = minOf(rolls[0], rolls[1]).toString()
                } else { // treat as first roll
                    advantageDie = dieSize
                    rolls[0] = rand
                    advantageRoll1.text = rand.toString()
                }
            } else {
                advantageDie = dieSize
                rolls[0] = rand
                rolls[1] = -1
                advantageRoll1.text = rand.toString()
                advantageRoll2.text = ""
                resultsTextView.text = ""
            }
        }
    }
}