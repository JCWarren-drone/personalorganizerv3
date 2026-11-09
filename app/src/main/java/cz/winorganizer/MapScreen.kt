package cz.winorganizer
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.math.*
fun haversineKm(aLat:Double,aLon:Double,bLat:Double,bLon:Double):Double{
  val r=6371.0; val dLat=Math.toRadians(bLat-aLat); val dLon=Math.toRadians(bLon-aLon)
  val h=sin(dLat/2).pow(2)+cos(Math.toRadians(aLat))*cos(Math.toRadians(bLat))*sin(dLon/2).pow(2)
  return 2*r*asin(sqrt(h))
}
fun routeDistanceKm(points:List<PhotoEntry>):Double{
  var s=0.0; for(i in 0 until points.size-1){ val a=points[i]; val b=points[i+1]; if(a.lat!=null&&a.lon!=null&&b.lat!=null&&b.lon!=null) s+=haversineKm(a.lat,a.lon,b.lat,b.lon) }; return s
}
@Composable fun PhotoMap(photos:List<PhotoEntry>, route:List<PhotoEntry>){
  AndroidView(factory={ ctx -> MapView(ctx).apply{ setTileSource(TileSourceFactory.MAPNIK); setMultiTouchControls(true); controller.setZoom(7.0); controller.setCenter(GeoPoint(49.8,15.5)) } }, update={ map ->
    map.overlays.clear()
    photos.filter{it.lat!=null&&it.lon!=null}.forEach{ p -> Marker(map).apply{ position=GeoPoint(p.lat!!,p.lon!!); title=p.title; snippet=p.place; map.overlays.add(this) } }
    if(route.size>=2){ val line=Polyline(); line.setPoints(route.filter{it.lat!=null&&it.lon!=null}.map{GeoPoint(it.lat!!,it.lon!!)}); map.overlays.add(line) }
    map.invalidate()
  })
}
