package app.presenter.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import app.domain.util.time.*
import app.presenter.components.common.*
import app.presenter.components.window.*
import app.presenter.theme.*
import java.time.LocalDateTime
import java.time.format.*
import java.util.*

@Composable
fun DateTimePickerDialog(
    title: String? = null,
    visible: Boolean,
    onClose: () -> Unit,
    onDateTimePicked: (LocalDateTime) -> Unit
) {
    val dialogState = rememberDialogState(size = DpSize(500.dp, 320.dp))

    val currentTimeAsCalendar by remember { mutableStateOf(Calendar.getInstance()) }

    val year by remember { mutableStateOf(currentTimeAsCalendar.get(Calendar.YEAR)) }
    var selectedYear by remember { mutableStateOf(year) }
    val yearOptions = remember { mutableStateListOf(year, year + 1, year + 2) }

    val month by remember { mutableStateOf(currentTimeAsCalendar.get(Calendar.MONTH)) }
    var selectedMonth by remember { mutableStateOf(month) }
    val monthOptions = remember(selectedYear) {
        if (selectedYear == year) {
            List(12 - month) { index ->
                month + index
            }.toMutableStateList()
        } else {
            mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        }
    }

    val day by remember { mutableStateOf(currentTimeAsCalendar.get(Calendar.DAY_OF_MONTH)) }
    var selectedDay by remember { mutableStateOf(day) }
    val dayOptions = remember(selectedYear, selectedMonth) {
        if (selectedYear == year && selectedMonth == month) {
            List(getMonthDays(month, year) - day) { index ->
                day + index
            }
        } else {
            List(getMonthDays(selectedMonth, selectedYear)) { index ->
                index + 1
            }
        }.toMutableStateList()
    }

    val hour by remember { mutableStateOf(currentTimeAsCalendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedHour by remember { mutableStateOf(hour) }
    val hourOptions = remember(selectedYear, selectedMonth, selectedDay) {
        if (selectedYear == year && selectedMonth == month && selectedDay == day) {
            List(24 - hour) { index ->
                hour + index
            }
        } else {
            List(24) { index ->
                index
            }
        }.toMutableStateList()
    }

    val minute by remember { mutableStateOf(currentTimeAsCalendar.get(Calendar.MINUTE)) }
    var selectedMinute by remember { mutableStateOf(minute) }
    val minuteOptions = remember(selectedYear, selectedMonth, selectedDay, selectedHour) {
        if (selectedYear == year && selectedMonth == month && selectedDay == day && selectedHour == hour) {
            List(60 - minute) { index ->
                minute + index
            }
        } else {
            List(60) { index ->
                index
            }
        }.toMutableStateList()
    }

    val localDateTime by remember(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute) {
        mutableStateOf(LocalDateTime.of(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute))
    }

    DialogWindow(
        state = dialogState,
        onCloseRequest = { onClose() },
        visible = visible,
        resizable = false,
        onKeyEvent = { event ->
            when (event.key) {
                Key.Escape -> onClose()
                else -> Unit
            }
            false
        },
        undecorated = true
    ) {
        DialogWindowTitleBar(
            title = title ?: "Select date and time",
            onClose = onClose
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier.width(64.dp),
                        text = "Date: ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "(${localDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    OptionsTextField(
                        modifier = Modifier.width(100.dp),
                        startValue = year.toString(),
                        label = "",
                        options = yearOptions.map { it.toString() },
                        onOptionSelected = { index ->
                            selectedYear = yearOptions[index]
                        },
                        showEditIcon = false,
                        decorated = true
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OptionsTextField(
                        modifier = Modifier.width(160.dp),
                        startValue = month.toString(),
                        label = "",
                        options = monthOptions.map { parseMonth(it) },
                        onOptionSelected = { index ->
                            selectedMonth = monthOptions[index]
                        },
                        showEditIcon = false,
                        decorated = true
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OptionsTextField(
                        modifier = Modifier.width(80.dp),
                        startValue = day.toString(),
                        label = "",
                        options = dayOptions.map { it.toString() },
                        onOptionSelected = { index ->
                            selectedDay = dayOptions[index]
                        },
                        showEditIcon = false,
                        decorated = true
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.width(56.dp),
                    text = "Time: ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    OptionsTextField(
                        modifier = Modifier.width(100.dp),
                        startValue = hour.toString(),
                        label = "",
                        options = hourOptions.map { it.toString() },
                        onOptionSelected = { index ->
                            selectedHour = hourOptions[index]
                        },
                        showEditIcon = false,
                        decorated = true
                    )
                    Text(
                        text = " : ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    OptionsTextField(
                        modifier = Modifier.width(100.dp),
                        startValue = minute.toString(),
                        label = "",
                        options = minuteOptions.map { it.toString() },
                        onOptionSelected = { index ->
                            selectedMinute = minuteOptions[index]
                        },
                        showEditIcon = false,
                        decorated = true
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier.width(160.dp),
                        onClick = {
                            onDateTimePicked(localDateTime)
                        },
                        colors = buttonColors()
                    ) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Button(
                        modifier = Modifier.width(160.dp),
                        onClick = onClose,
                        colors = buttonColors()
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}