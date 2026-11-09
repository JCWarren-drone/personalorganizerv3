package cz.winorganizer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
class MainActivity:ComponentActivity(){
  override fun onCreate(savedInstanceState:Bundle?){
    super.onCreate(savedInstanceState)
    val db = Room.databaseBuilder(this, AppDb::class.java, "winorganizer.db").build()
    setContent{ MaterialTheme(colorScheme=darkColorScheme()){ OrganizerScreen(db) } }
  }
}
@Composable fun OrganizerScreen(db:AppDb){
  var tab by remember{mutableIntStateOf(0)}
  var tasks by remember{mutableStateOf<List<Task>>(emptyList())}
  var notes by remember{mutableStateOf<List<Note>>(emptyList())}
  var events by remember{mutableStateOf<List<Event>>(emptyList())}
  var year by remember{mutableIntStateOf(2026)}
  var month by remember{mutableIntStateOf(10)}
  val scope = rememberCoroutineScope()
  LaunchedEffect(Unit){ tasks=db.dao().tasks(); notes=db.dao().notesAll(); events=db.dao().events() }
  Column(Modifier.fillMaxSize().padding(16.dp)){
    Text("WINOrganizer", style=MaterialTheme.typography.headlineMedium)
    TabRow(tab){ listOf("Poznámky","Úkoly","Kalendář","Fotodeník").forEachIndexed{i,t-> Tab(selected=tab==i,onClick={tab=i}){ Text(t,Modifier.padding(10.dp)) } } }
    Spacer(Modifier.height(12.dp))
    when(tab){
      0->LazyColumn{ items(notes){ Text(it.title, style=MaterialTheme.typography.titleMedium); Text(it.content) } }
      1->LazyColumn{ items(tasks){ Row{ Checkbox(it.done,{c-> scope.launch{ db.dao().updateTask(it.copy(done=c)); tasks=db.dao().tasks() } }); Text(it.title) } } }
      2->{ Row{ Button(onClick={ if(month==0){month=11; year--} else month-- }){Text("<")}; Text("$month / $year", Modifier.padding(12.dp)); Button(onClick={ if(month==11){month=0; year++} else month++ }){Text(">")} }; Text("Kalendář $month/$year — rozsah 2026–2035, události se filtrují podle data. Plná měsíční mřížka je v prototypu, zde zjednodušený seznam."); LazyColumn{ items(events){ Text(it.title); Text(SimpleDateFormat("d.M.yyyy HH:mm", Locale("cs")).format(Date(it.startAt))) } } }
      3->PhotoDiaryScreen(db)
    }
    if(tab==1) Button(onClick={ scope.launch{ db.dao().insertTask(Task(folderId="ukoly", title="Nový úkol")); tasks=db.dao().tasks() } }){ Text("+ Přidat úkol") }
  }
}
