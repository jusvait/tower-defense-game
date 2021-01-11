package TD.utils

import scala.collection.mutable

/** Config
 *
 *  Describes game's configs
 * */
class Config {
  // Defaults
  final val DEFAULT_MUSIC_VOLUME: Float = 0.1f
  final val DEFAULT_SFX_VOLUME:   Float = 0.1f

  val confs = new mutable.HashMap[String, Float]()

  // Load volume configurations from config or set to default
  Serializer.loadConfig(this)

  confs("musicVolume") = DEFAULT_MUSIC_VOLUME
  confs("sfxVolume")   = DEFAULT_SFX_VOLUME

  def resetToDefaults() = {
    confs("musicVolume") = DEFAULT_MUSIC_VOLUME
    confs("sfxVolume")   = DEFAULT_SFX_VOLUME
  }

}
