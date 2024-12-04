package com.example.volatoon.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volatoon.R
import com.example.volatoon.utils.DataStoreManager
import com.example.volatoon.viewmodel.LoginViewModel
import com.example.volatoon.viewmodel.ProfileViewModel
import kotlinx.coroutines.Job
import androidx.compose.ui.unit.sp


@Composable
fun ProfileScreen (
    onLogOut: () -> Job,
    onNavigateToBookmark: () -> Unit,
    viewState: ProfileViewModel.ProfileResState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.error != null -> {
                Text(text = "ERROR OCCURRED: ${viewState.error}")
                Button(onClick = { onLogOut() }) {
                    Text("LogOut", color = Color.Red)
                }
            }

            viewState.profileDataRes == null -> {
                Text(text = "No profile data available.")
            }

            else -> {

                ProfileHeader(
                    fullName = viewState.profileDataRes.body()?.userData?.fullName ?: "N/A",
                    userName = viewState.profileDataRes.body()?.userData?.userName ?: "N/A"
                )

                UserQuote(
                    quote = "Aku ingin mencintaimu dengan sederhana " +
                            "dengan isyarat yang tak sempat disampaikan " +
                            "awan kepada hujan yang menjadikannya tiada"
                )


                ProfileActions(
                    onNavigateToBookmark = onNavigateToBookmark,
                    onLogOut = onLogOut
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(fullName: String, userName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {

        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp)
        )


        Text(
            text = fullName,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                color = Color.Black
            )
        )


        Text(
            text = "@$userName",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                color = Color.Gray
            )
        )
    }
}

@Composable
fun UserQuote(quote: String) {
    Text(
        text = quote,
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            color = Color.Gray
        ),
        modifier = Modifier
            .padding(16.dp)
            .background(Color.LightGray)
            .padding(8.dp)
    )
}

@Composable
fun ProfileActions(
    onNavigateToBookmark: () -> Unit,
    onLogOut: () -> Job
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { /* Navigate to Premium */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow
            )
        ) {
            Text("VolaToon Premium", color = Color.Black)
        }

        Button(
            onClick = { onNavigateToBookmark() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan
            )
        ) {
            Text("Bookmarks & History", color = Color.Black)
        }

        Button(
            onClick = { /* Navigate to Settings */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan
            )
        ) {
            Text("Settings", color = Color.Black)
        }

        Button(
            onClick = { onLogOut() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text("Logout", color = Color.Red)
        }
    }
}

