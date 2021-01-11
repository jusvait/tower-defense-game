package TD

import TD.utils.UITools
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage}
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Slider, Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport

/** Settings Screen */
class SettingsScreen(val game: TowerDefense) extends Screen {
  var camera: OrthographicCamera = new OrthographicCamera
  camera.setToOrtho(false, 960, 540)

  var settingsMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/settings.mp3"))

  /** USER INTERFACE*/
  var stage = new Stage(new ScreenViewport())
  Gdx.input.setInputProcessor(stage)

  var table = new Table
  table.setFillParent(true)
  stage.addActor(table)
  var buttonFont = new BitmapFont()

  /** Widgets */

  var backButtonUp    = new Texture(Gdx.files.internal("Textures/button_1.png"))
  var backButtonDown  = new Texture(Gdx.files.internal("Textures/button_1_down.png"))
  var backButtonStyle = new TextButtonStyle()
  var backUpRegion    = new TextureRegion(backButtonUp,128, 64)
  var backDownRegion  = new TextureRegion(backButtonDown, 128, 64)

  backButtonStyle.up   = new TextureRegionDrawable(backUpRegion)
  backButtonStyle.down = new TextureRegionDrawable(backDownRegion)
  backButtonStyle.font = buttonFont

  var volumeSliderStyle       = new SliderStyle()
  var volumeSliderKnob        = new Texture(Gdx.files.internal("Textures/knob.png"))
  var volumeSliderKnobDown    = new Texture(Gdx.files.internal("Textures/knob_down.png"))
  var volumeSliderBG          = new Texture(Gdx.files.internal("Textures/slider_knob_after.png"))
  var volumeSliderKnobBefore  = new Texture(Gdx.files.internal("Textures/slider_knob_before.png"))

  var volumeSliderKnobRegion        = new TextureRegion(volumeSliderKnob, 32,32)
  var volumeSliderKnobDownRegion    = new TextureRegion(volumeSliderKnobDown, 32,32)
  var volumeSliderBGRegion          = new TextureRegion(volumeSliderBG, 128, 32)
  var volumeSliderKnobBeforeRegion  = new TextureRegion(volumeSliderKnobBefore, 128, 32)

  volumeSliderStyle.knob        = new TextureRegionDrawable(volumeSliderKnobRegion)
  volumeSliderStyle.knobDown    = new TextureRegionDrawable(volumeSliderKnobDownRegion)
  volumeSliderStyle.background  = new TextureRegionDrawable(volumeSliderBGRegion)
  volumeSliderStyle.knobBefore  = new TextureRegionDrawable(volumeSliderKnobBeforeRegion)

  var labelStyle = new LabelStyle()
  labelStyle.background = new TextureRegionDrawable(backUpRegion)
  labelStyle.font = buttonFont
  labelStyle.fontColor = Color.WHITE

  var musicVolumeSlider = UITools.createSlider("Textures/knob.png","Textures/nob_down.png",
    "Textures/slider_knob_before.png","Textures/slider_knob_after.png",128,64)

  musicVolumeSlider.setValue(game.config.confs("musicVolume"))

  musicVolumeSlider.addListener(new InputListener() {
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
      game.config.confs("musicVolume") = musicVolumeSlider.getValue
      musicVolumeSliderLabel.setText(s"Music Volume: ${(musicVolumeSlider.getValue*100f).round/100f}")
      settingsMusic.pause()
      settingsMusic.play(musicVolumeSlider.getValue)
      true
    }
  })

  table.add(musicVolumeSlider)

  var musicVolumeSliderLabel = new Label( s"Music Volume: ${(musicVolumeSlider.getValue*100f).round/100f}" , labelStyle)
  table.add(musicVolumeSliderLabel)

  table.row()

  var sfxVolumeSlider = new Slider(0f, 1f, 0.01f, false, volumeSliderStyle)

  sfxVolumeSlider.setValue(game.config.confs("sfxVolume"))

  sfxVolumeSlider.addListener(new InputListener() {
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
      game.config.confs("sfxVolume") = sfxVolumeSlider.getValue
      sfxVolumeSliderLabel.setText(s"SFX Volume: ${(sfxVolumeSlider.getValue*100f).round/100f}")
      true
    }
  })

  table.add(sfxVolumeSlider)

  var sfxVolumeSliderLabel = new Label(s"SFX Volume: ${(sfxVolumeSlider.getValue*100f).round/100f}", labelStyle)
  table.add(sfxVolumeSliderLabel)

  table.row()

  var backButton = new TextButton("Back", backButtonStyle)

  backButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      game.setScreen(new MainMenuScreen(game))
      dispose()
      true
    }
  })

  table.row()
  table.add(backButton)


  override def render(delta: Float) = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    stage.draw()
  }

  override def show() = {
    game.loadConfig()
    settingsMusic.loop(game.config.confs("musicVolume"))
  }

  override def resize(width: Int, height: Int)= {}

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {
    // Update volume sliders
    game.config.confs("musicVolume") = musicVolumeSlider.getValue
    game.config.confs("sfxVolume")   = sfxVolumeSlider.getValue

    // Stop background music
    settingsMusic.stop()

    // Write updated values in game config
    game.writeConfig()
  }

  override def dispose(): Unit = {
    game.writeConfig()
    stage.dispose()
    settingsMusic.dispose()
  }
}
