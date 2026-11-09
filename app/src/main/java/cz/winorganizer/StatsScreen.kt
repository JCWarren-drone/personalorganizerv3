package cz.winorganizer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable fun StatsScreen(photos:List<PhotoEntry>){
  val byTheme = photos.groupBy{it.theme}.mapValues{it.value.size}
  val byWeather = photos.groupBy{it.weather.ifBlank{"Nezadáno"}}.mapValues{it.value.size}
  val byLens = photos.groupBy{it.lens.ifBlank{"Nezadáno"}}.mapValues{it.value.size}
  val byYear = photos.groupBy{it.date.take(4)}.mapValues{it.value.size}
  val withGps = photos.count{it.lat!=null}
  val days = photos.map{it.date}.distinct().size
  Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
    Text("Statistiky focení", style=MaterialTheme.typography.headlineSmall)
    Text("Celkem fotek: ${photos.size}  •  S GPS: $withGps  •  Dní focení: $days")
    Text("První: ${photos.minByOrNull{it.date}?.date ?: "-"}  •  Poslední: ${photos.maxByOrNull{it.date}?.date ?: "-"}")
    StatBlock("Podle tématu", byTheme, photos.size)
    StatBlock("Podle počasí", byWeather, photos.size)
    StatBlock("Podle objektivu", byLens, photos.size)
    StatBlock("Podle roku", byYear, photos.size)
    val topPlaces = photos.groupBy{it.place.ifBlank{"Nezadáno"}}.mapValues{it.value.size}.entries.sortedByDescending{it.value}.take(5)
    Text("Top 5 míst:", style=MaterialTheme.typography.titleMedium)
    topPlaces.forEach{ Text("${it.key}: ${it.value}") }
  }
}
@Composable fun StatBlock(title:String, data:Map<String,Int>, total:Int){
  Column{ Text(title, style=MaterialTheme.typography.titleMedium); data.entries.sortedByDescending{it.value}.forEach{ (k,v)-> Row(Modifier.fillMaxWidth()){ Text("$k: $v  (${if(total>0) v*100/total else 0}%)") } } }
}
