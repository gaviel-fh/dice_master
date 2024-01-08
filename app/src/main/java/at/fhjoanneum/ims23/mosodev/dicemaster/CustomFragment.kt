package at.fhjoanneum.ims23.mosodev.dicemaster

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CustomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomFragment : Fragment() {
    lateinit var resultsTextView: TextView
    lateinit var dieRolledView: TextView
    lateinit var dieSizeInput: EditText
    lateinit var customRollButton: Button

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
        return inflater.inflate(R.layout.fragment_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultsTextView = view.findViewById(R.id.resultsTextView)
        dieRolledView = view.findViewById(R.id.dieRolledView)
        dieSizeInput = view.findViewById(R.id.dieSizeInput)
        customRollButton = view.findViewById(R.id.rollButton)

        activity?.startService(Intent(activity, DiceService::class.java))

        dieSizeInput.doOnTextChanged { text, start, before, count ->
            customRollButton.isEnabled = !text.isNullOrBlank() // disabled when text is blank
        }
        customRollButton.setOnClickListener {
            if (dieSizeInput.equals(0)) {
                resultsTextView.text = "0"
            } else {
                val rand = Random().nextInt(Integer.valueOf(dieSizeInput.text.toString())) + 1
                resultsTextView.text = rand.toString()
            }
            dieRolledView.text = "d" + dieSizeInput.text.toString()
        }
    }
}