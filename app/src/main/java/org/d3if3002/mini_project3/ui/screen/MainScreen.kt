package org.d3if3002.mini_project3.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3002.mini_project3.R
import org.d3if3002.mini_project3.model.City
import org.d3if3002.mini_project3.network.CityApi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {


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
                )
            )
        }
    ) {padding ->
        ScreenContent(Modifier.padding(padding))
    }
}


@Composable
fun ScreenContent(modifier: Modifier) {

    val viewModel: MainViewModel = viewModel()
    val data by viewModel.data

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(data) { ListItem(city = it)}
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
                .data(CityApi.getCityUrl(city.imageId))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.gambar, city.city),
            contentScale = ContentScale.Crop,
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


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        MainScreen()
    }
}