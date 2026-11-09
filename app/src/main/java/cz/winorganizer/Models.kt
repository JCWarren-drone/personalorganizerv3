package cz.winorganizer
import androidx.room.*
import java.util.UUID
@Entity(tableName="folders") data class Folder(@PrimaryKey val id:String=UUID.randomUUID().toString(), val name:String, val type:String, val parentId:String?=null)
@Entity(tableName="notes") data class Note(@PrimaryKey val id:String=UUID.randomUUID().toString(), val folderId:String, val title:String, val content:String, val updatedAt:Long=System.currentTimeMillis())
@Entity(tableName="tasks") data class Task(@PrimaryKey val id:String=UUID.randomUUID().toString(), val folderId:String, val title:String, val done:Boolean=false, val priority:Int=1, val dueAt:Long?=null)
@Entity(tableName="events") data class Event(@PrimaryKey val id:String=UUID.randomUUID().toString(), val title:String, val startAt:Long, val note:String="")
@Entity(tableName="photos") data class PhotoEntry(
  @PrimaryKey val id:String=UUID.randomUUID().toString(),
  val title:String, val description:String="", val place:String="",
  val date:String, val time:String="", val theme:String="Krajina",
  val weather:String="", val temperatureC:Double?=null, val humidity:Int?=null, val windKmh:Double?=null, val weatherSource:String="",
  val lat:Double?=null, val lon:Double?=null, val lens:String="", val photoUri:String=""
)
@Dao interface OrganizerDao {
  @Query("SELECT * FROM folders ORDER BY name") suspend fun folders():List<Folder>
  @Insert suspend fun insertFolder(f:Folder)
  @Query("SELECT * FROM notes ORDER BY updatedAt DESC") suspend fun notesAll():List<Note>
  @Insert suspend fun insertNote(n:Note); @Update suspend fun updateNote(n:Note); @Delete suspend fun deleteNote(n:Note)
  @Query("SELECT * FROM tasks ORDER BY done, dueAt") suspend fun tasks():List<Task>
  @Insert suspend fun insertTask(t:Task); @Update suspend fun updateTask(t:Task); @Delete suspend fun deleteTask(t:Task)
  @Query("SELECT * FROM events ORDER BY startAt") suspend fun events():List<Event>
  @Insert suspend fun insertEvent(e:Event); @Delete suspend fun deleteEvent(e:Event)
  @Query("SELECT * FROM photos ORDER BY date DESC, time DESC") suspend fun photos():List<PhotoEntry>
  @Insert suspend fun insertPhoto(p:PhotoEntry); @Update suspend fun updatePhoto(p:PhotoEntry); @Delete suspend fun deletePhoto(p:PhotoEntry)
}
@Database(entities=[Folder::class,Note::class,Task::class,Event::class,PhotoEntry::class], version=1) abstract class AppDb:RoomDatabase(){ abstract fun dao():OrganizerDao }
