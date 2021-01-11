package TD

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport

/** Main menu screen */
class MainMenuScreen(val game: TowerDefense) extends Screen {

  var camera: OrthographicCamera = new OrthographicCamera
  camera.setToOrtho(false, 960, 540)

  var background = new Texture("Textures/menu.png")

  var menuMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/theme.mp3"))

  /** USER INTERFACE*/

  var stage = new Stage(new ScreenViewport())
  Gdx.input.setInputProcessor(stage)

  var table = new Table
  table.setFillParent(true)
  stage.addActor(table)
  table.setPosition(0, -40)

  /** Widgets */

  var buttonFont = new BitmapFont()
  var playButtonUp = new Texture(Gdx.files.internal("Textures/button_1.png"))
  var playButtonDown = new Texture(Gdx.files.internal("Textures/button_1_down.png"))

  var upRegion   = new TextureRegion(playButtonUp,128, 64)
  var downRegion = new TextureRegion(playButtonDown, 128, 64)
  var playButtonStyle = new TextButtonStyle()
  playButtonStyle.up  = new TextureRegionDrawable(upRegion)
  playButtonStyle.down = new TextureRegionDrawable(downRegion)
  playButtonStyle.font = buttonFont

  var playButton = new TextButton("Play", playButtonStyle)

  playButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      game.setScreen(new PlayScreen(game))
      dispose()
      true
    }
  })

  table.add(playButton).padBottom(10)

  var settingsButtonUp = new Texture(Gdx.files.internal("Textures/button_1.png"))
  var settingsButtonDown = new Texture(Gdx.files.internal("Textures/button_1_down.png"))
  var settingsButtonStyle = new TextButtonStyle()
  var settingsUpRegion   = new TextureRegion(playButtonUp,128, 64)
  var settingsDownRegion = new TextureRegion(playButtonDown, 128, 64)
  settingsButtonStyle.up  = new TextureRegionDrawable(upRegion)
  settingsButtonStyle.down = new TextureRegionDrawable(downRegion)
  settingsButtonStyle.font = buttonFont

  var settingsButton = new TextButton("Settings", settingsButtonStyle)

  // Listen for clicks
  settingsButton.addListener(new InputListener(){
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) = {
      game.setScreen(new SettingsScreen(game))
      dispose()
      true
    }
  })

  table.row()
  table.add(settingsButton)


  override def render(delta: Float) = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.update
    game.batch.setProjectionMatrix(camera.combined)

    game.batch.begin
    game.batch.draw(background, 0, 0)
    game.batch.end()

    stage.draw()
  }

  override def show() = {
    game.loadConfig()
    menuMusic.loop(game.config.confs("musicVolume"))
  }

  override def resize(width: Int, height: Int)= {}

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {
    game.writeConfig()
  }

  override def dispose(): Unit = {
    background.dispose()
    menuMusic.stop()
    menuMusic.dispose()
    stage.dispose()
  }
}
