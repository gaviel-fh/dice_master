package at.fhjoanneum.ims23.mosodev.dicemaster

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DiceSelector(diceTypes: List<String>, onDiceSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(width = 1.dp, color = Color.Gray)
            .padding(8.dp)
            .clickable { expanded = true }
    ) {
        Text(text = "Select a dice")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        diceTypes.forEach { dice ->
            DropdownMenuItem(onClick = {
                expanded = false
                onDiceSelected(dice)
            }, text = {
                Text(text = dice)
            })
        }
    }
}

@Composable
fun DiceRoller(selectedDice: List<String>, onRoll: () -> List<Int>) {
    Button(onClick = {
        val results = onRoll()
    }) {
        Text("Roll!")
    }
}

@Composable
fun DiceDisplay(selectedDice: List<String>, results: List<Int> = emptyList()) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            selectedDice.forEach { die ->
                Text(text = die, modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            results.forEach { result ->
                Text(text = result.toString(), modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun MainDiceRoller() {
    val diceTypes = listOf("d2", "d4", "d6", "d8", "d10", "d12", "d20", "d100")
    var selectedDice by remember { mutableStateOf(listOf<String>()) }
    var results by remember { mutableStateOf(listOf<Int>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DiceSelector(diceTypes) { dice ->
            selectedDice = selectedDice + dice
        }

        Spacer(modifier = Modifier.height(16.dp))

        DiceDisplay(selectedDice, results)

        Spacer(modifier = Modifier.height(16.dp))

        DiceRoller(selectedDice) {
            selectedDice.map { die ->
                val maxValue = die.drop(1).toInt()
                (1..maxValue).random()
            }.also { results = it }  // Assign rolled results to the results state
        }
    }
}
