package cz.winorganizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
data class WeatherResult(val label:String, val tempC:Double?, val humidity:Int?, val windKmh:Double?, val source:String)
fun weatherLabel(code:Int):String = when(code){0->"Slunečno";1,2->"Polojasno";3->"Zataženo";45,48->"Mlha";51,53,55,61,63,65,80,81,82->"Déšť";71,73,75,77,85,86->"Sníh";95,96,99->"Bouřka";else->"Neznámé"}
object WeatherRepo {
  private val client = OkHttpClient()
  private suspend fun get(url:String):JSONObject = withContext(Dispatchers.IO){
    val res = client.newCall(Request.Builder().url(url).build()).execute()
    JSONObject(res.body!!.string())
  }
  // Aktualni pocasi dle GPS
  suspend fun current(lat:Double, lon:Double):WeatherResult {
    val j = get("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m")
    val c = j.getJSONObject("current")
    return WeatherResult(weatherLabel(c.getInt("weather_code")), c.optDouble("temperature_2m"), c.optInt("relative_humidity_2m"), c.optDouble("wind_speed_10m"), "Aktuální")
  }
  // Archivni pocasi pro stary zaznam: datum ve formatu YYYY-MM-DD
  suspend fun archive(lat:Double, lon:Double, date:String):WeatherResult {
    val j = get("https://archive-api.open-meteo.com/v1/archive?latitude=$lat&longitude=$lon&start_date=$date&end_date=$date&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,weather_code,wind_speed_10m_max")
    val d = j.getJSONObject("daily")
    val code = d.getJSONArray("weather_code").getInt(0)
    return WeatherResult(weatherLabel(code), d.getJSONArray("temperature_2m_max").getDouble(0), null, d.getJSONArray("wind_speed_10m_max").getDouble(0), "Archiv")
  }
}
