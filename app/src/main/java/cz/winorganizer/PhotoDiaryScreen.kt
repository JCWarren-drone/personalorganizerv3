package cz.winorganizer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Zjednoduseny formular + seznam. V plne verzi pridej: vyber fotky z galerie (ActivityResultContracts.GetContent),
// GPS pres FusedLocationProviderClient, volani WeatherRepo.current() / .archive(), a prepinac Seznam / Mapa / Statistiky.
@Composable fun PhotoDiaryScreen(db:AppDb){
  var photos by remember{mutableStateOf<List<PhotoEntry>>(emptyList())}
  var title by remember{mutableStateOf("")}
  var place by remember{mutableStateOf("")}
  var date by remember{mutableStateOf("2026-11-07")}
  var time by remember{mutableStateOf("12:00")}
  var theme by remember{mutableStateOf("Krajina")}
  var lens by remember{mutableStateOf("50mm")}
  var showMap by remember{mutableStateOf(false)}
  var showStats by remember{mutableStateOf(false)}
  LaunchedEffect(Unit){ photos=db.dao().photos() }
  Column(Modifier.padding(16.dp)){
    Row{ Button(onClick={showMap=false; showStats=false}){Text("Seznam")}; Spacer(Modifier.width(8.dp)); Button(onClick={showMap=true; showStats=false}){Text("Mapa")}; Spacer(Modifier.width(8.dp)); Button(onClick={showStats=true; showMap=false}){Text("Statistiky")} }
    Spacer(Modifier.height(12.dp))
    when{ showStats->StatsScreen(photos); showMap->PhotoMap(photos, photos.sortedBy{it.date}); else->{
      OutlinedTextField(title,{title=it},label={Text("Název")},modifier=Modifier.fillMaxWidth())
      OutlinedTextField(place,{place=it},label={Text("Místo")},modifier=Modifier.fillMaxWidth())
      Row{ OutlinedTextField(date,{date=it},label={Text("Datum YYYY-MM-DD")},modifier=Modifier.weight(1f)); Spacer(Modifier.width(8.dp)); OutlinedTextField(time,{time=it},label={Text("Čas")},modifier=Modifier.weight(1f)) }
      Row{ OutlinedTextField(theme,{theme=it},label={Text("Téma")},modifier=Modifier.weight(1f)); Spacer(Modifier.width(8.dp)); OutlinedTextField(lens,{lens=it},label={Text("Objektiv")},modifier=Modifier.weight(1f)) }
      Text("GPS + počasí: v plné verzi tlačítka Získat GPS polohu / Načíst počasí / Načíst archivní počasí volají WeatherRepo a FusedLocationProviderClient. Viz Weather.kt a README.", style=MaterialTheme.typography.bodySmall)
      Button(onClick={ /* TODO: ulozit pres dao.insertPhoto(PhotoEntry(...)) a znovu nacist */ }){ Text("+ Přidat záznam (viz kód)") }
      Spacer(Modifier.height(12.dp))
      LazyColumn{ items(photos){ p-> Card(Modifier.fillMaxWidth().padding(vertical=4.dp)){ Column(Modifier.padding(12.dp)){ Text(p.title, style=MaterialTheme.typography.titleMedium); Text("${p.date} ${p.time} • ${p.place}"); Text("Téma: ${p.theme} • Objektiv: ${p.lens} • Počasí: ${p.weather} ${p.temperatureC ?: ""}") } } } }
    }}
  }
}
