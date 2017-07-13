package controller

import model.entities.{Pokemon, Trainer}
import model.environment.Direction.Direction
import model.environment._
import model.map.Box
import utilities.Settings
import view._

import scala.collection.JavaConverters._

abstract class BuildingController(private val view: View, private val mapController: GameControllerImpl, private val _trainer: Trainer) extends GameControllerImpl(view, _trainer) {

  protected var buildingMap: BuildingMap

  this.setTrainerSpriteBack()

  override protected def doMove(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try {
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        tile match {
          case _ if tile.walkable =>
            walk(direction, nextPosition)
          case _ => trainerIsMoving = false
        }
      } catch {
        case _: ArrayIndexOutOfBoundsException =>
          trainerIsMoving = false
          if (trainer.coordinate equals buildingMap.entryCoordinate) {
            if(buildingMap.isInstanceOf[LaboratoryMap] && trainer.capturedPokemons.isEmpty){
              view.showDialogue(new ClassicDialoguePanel(this, buildingMap.npc.dialogue.asJava))
            }else {
              this.terminate()
              mapController.resume()
            }
          }
        case _: NullPointerException => trainerIsMoving = false
      }
    }
  }

  override protected def doStart(): Unit = {
    audio.loop()
  }

  override protected def doTerminate(): Unit = {
    audio.stop()
  }

  override protected def doPause(): Unit = {}

  override protected def doResume(): Unit = {}

}

class PokemonCenterController(private val view: View, private val mapController: GameControllerImpl, private val _trainer: Trainer) extends BuildingController(view, mapController, _trainer){
  override protected var buildingMap: BuildingMap = new PokemonCenterMap
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)

  audio = Audio(Settings.POKEMONCENTER_SONG)

  override protected def doStart(): Unit = {
    super.doStart()
    initView()
  }

  override protected def doResume(): Unit = {
    super.doResume()
    initView()
  }

  private def initView(): Unit = {
    view.showPokemonCenter(this, buildingMap)
    gamePanel = view.getPokemonCenterPanel
  }

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        if(nextPosition equals buildingMap.npc.coordinate){
          this.view.showDialogue(new DoctorDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(tile.isInstanceOf[Box]){
          this.pause()
          view showBoxPanel this
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

class LaboratoryController(private val view: View, private val mapController: GameControllerImpl, private val _trainer: Trainer) extends BuildingController(view, mapController, _trainer){
  override protected var buildingMap: BuildingMap = new LaboratoryMap
  this.trainer.coordinate = CoordinateImpl(buildingMap.entryCoordinate.x, buildingMap.entryCoordinate.y)

  audio = Audio(Settings.LABORATORY_SONG)

  override protected def doStart(): Unit = {
    super.doStart()
    initView()
  }

  override protected def doResume(): Unit = {
    super.doResume()
    initView()
  }

  private def initView(): Unit = {
    view.showLaboratory(this, buildingMap, true/*this.trainer.capturedPokemons.isEmpty*/)
    gamePanel = view.getLaboratoryPanel
  }

  override protected def doInteract(direction: Direction): Unit = {
    if (!isInPause) {
      val nextPosition = nextTrainerPosition(direction)
      try{
        val tile = buildingMap.map(nextPosition.x)(nextPosition.y)
        if(nextPosition equals buildingMap.npc.coordinate){
          this.view.showDialogue(new ClassicDialoguePanel(this, buildingMap.npc.dialogue.asJava))
        }
        if(true/*trainer.capturedPokemons.isEmpty*/) {
          for (pokemon <- buildingMap.pokemonNpc) if (nextPosition equals pokemon.coordinate) {
            this.pause()
            view.showInitialPokemonPanel(this, Pokemon(1,"ciao",(1,2,3,4),5,0,0,null)/*pokemon.pokemon*/)
          }
        }
      }catch{
        case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
      }
    }
  }
}

