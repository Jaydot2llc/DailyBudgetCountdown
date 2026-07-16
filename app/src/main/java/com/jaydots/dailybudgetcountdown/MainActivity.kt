package com.jaydots.dailybudgetcountdown


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Route names for navigation
private object Routes {
    const val WELCOME = "welcome"
    const val BUDGET_ENTRY = "budget_entry"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(onGetStartedClick = {
                navController.navigate(Routes.BUDGET_ENTRY)
            })
        }
        composable(Routes.BUDGET_ENTRY) {
            BudgetEntryScreen(onSubmitClick = { budgetAmount ->
                // TODO: Handle the submitted budget amount (save it, start the countdown, etc.)
            })
        }
    }
}

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Heading text
        Text(
            text = "Welcome",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        // Content text
        Text(
            text = "Hi, welcome to your Daily Budget Countdown. Let’s get started with what you want your budget to be for today!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Get Started button
        Button(
            onClick = onGetStartedClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF808080),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 32.dp)
        ) {
            Text(
                text = "Get Started >",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BudgetEntryScreen(onSubmitClick: (String) -> Unit) {
    var budgetInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header text
        Text(
            text = "Let's Get Started",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Instructional text
        Text(
            text = "Enter the amount of money you would like to start with today's budget",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        // Numeric budget input field
        OutlinedTextField(
            value = budgetInput,
            onValueChange = { newValue ->
                // Allow only digits and a single decimal point (basic currency-style input)
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                    budgetInput = newValue
                }
            },
            label = { Text("Budget amount") },
            placeholder = { Text("0.00") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        // Submit button
        Button(
            onClick = { onSubmitClick(budgetInput) },
            enabled = budgetInput.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF808080),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 24.dp)
        ) {
            Text(
                text = "Submit",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            WelcomeScreen(onGetStartedClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BudgetEntryScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            BudgetEntryScreen(onSubmitClick = {})
        }
    }
}