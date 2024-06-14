package org.d3if3002.mini_project3.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3002.mini_project3.BuildConfig
import org.d3if3002.mini_project3.R
import org.d3if3002.mini_project3.model.City
import org.d3if3002.mini_project3.model.User
import org.d3if3002.mini_project3.network.ApiStatus
import org.d3if3002.mini_project3.network.CityApi
import org.d3if3002.mini_project3.network.UserDataStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.city),
                            contentDescription = stringResource(id = R.string.logo),
                            modifier = Modifier.size(60.dp)
                        )
                        Text(text = stringResource(id = R.string.app_name), modifier = Modifier.padding(start = 8.dp))
                    }
            },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        }
                        else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.profil),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) {padding ->
        ScreenContent(Modifier.padding(padding))
        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }) {
                showDialog = false
            }
        }
    }
}


@Composable
fun ScreenContent(modifier: Modifier) {

    val viewModel: MainViewModel = viewModel()
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    when(status) {
        ApiStatus.LOADING -> {
//            Box(
//                modifier = modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
        }
        ApiStatus.SUCCESS -> {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(data) { ListItem(city = it)}
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrievedata() },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}


@Composable
fun ListItem(city: City) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
//                     if (city.city == "Jakarta"){
//                     CityApi.getCityUrl("not-found")
//                      } else
                    CityApi.getCityUrl(city.imageId))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.gambar, city.city),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(3.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp)
        ) {
            Text(
                text = city.city,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = city.country,
                fontSize = 10.sp,
                color = Color.White
            )
        }

    }
}



private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}


private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleIdToken.displayName ?: ""
            val email = googleIdToken.id
            val photoUrl = googleIdToken.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        MainScreen()
    }
}