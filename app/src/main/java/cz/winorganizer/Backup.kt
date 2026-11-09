package cz.winorganizer
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.json.JSONArray
import org.json.JSONObject
// Zaloha: export vsech tabulek do JSON, obnova zpet. V produkci doporucujeme sifrovat.
object Backup {
  suspend fun export(ctx:Context, db:AppDb, uri:Uri) = withContext(Dispatchers.IO){
    val dao = db.dao()
    val root = JSONObject()
    root.put("version", 1)
    root.put("notes", JSONArray(dao.notesAll().map{ JSONObject().put("title",it.title).put("content",it.content) }))
    root.put("tasks", JSONArray(dao.tasks().map{ JSONObject().put("title",it.title).put("done",it.done) }))
    root.put("events", JSONArray(dao.events().map{ JSONObject().put("title",it.title).put("startAt",it.startAt) }))
    root.put("photos", JSONArray(dao.photos().map{ JSONObject().put("title",it.title).put("date",it.date).put("time",it.time).put("theme",it.theme).put("weather",it.weather).put("lat",it.lat).put("lon",it.lon).put("lens",it.lens).put("place",it.place) }))
    ctx.contentResolver.openOutputStream(uri)?.use{ it.write(root.toString(2).toByteArray()) }
  }
}
