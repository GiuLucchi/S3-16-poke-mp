package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {

  addBuildings()
  addLakes()
  addTallGrass()
  addTrees()

  mainRoad()
  square()

  private def mainRoad(): Unit = {
    for (x <- 9 to 48)  x match {
      case 9 => {
        addTile(RoadMarginTopLeft(),CoordinateImpl(9,24))
        addTile(RoadMarginLeft(), CoordinateImpl(9,25))
        addTile(RoadMarginLeft(), CoordinateImpl(9,26))
        addTile(RoadMarginBottomLeft(),CoordinateImpl(9,27))
      }
      case 48 => {
        addTile(RoadMarginTopRight(),CoordinateImpl(48,24))
        addTile(RoadMarginRight(), CoordinateImpl(48,25))
        addTile(RoadMarginRight(), CoordinateImpl(48,26))
        addTile(RoadMarginBottomRight(),CoordinateImpl(48,27))
      }
      case _ => {
        addTile(RoadMarginTop(), CoordinateImpl(x, 24))
        addTile(Road(), CoordinateImpl(x, 25))
        addTile(Road(), CoordinateImpl(x, 26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
      }
    }
  }

  private def square(): Unit = {
    for (x <- 21 to 28)
      for (y <- 27 to 32)
        (x,y) match {
          case (21,28) | (21,29) | (21,30) | (21,31) => addTile(RoadMarginLeft(), CoordinateImpl(x,y))
          case (21,32) => addTile(RoadMarginBottomLeft(), CoordinateImpl(x,y))
          case (28,28) | (28,29) | (28,30) | (28,31) => addTile(RoadMarginRight(), CoordinateImpl(x,y))
          case (28,32) => addTile(RoadMarginBottomRight(), CoordinateImpl(x,y))
          case (_,32) => addTile(RoadMarginBottom(), CoordinateImpl(x,y))
          case _ => addTile(Road(), CoordinateImpl(x,y))
        }
  }

  private def addBuildings(): Unit ={
    addTile(PokemonCenter(CoordinateImpl(10,19)), CoordinateImpl(10,19))
    addTile(Laboratory(CoordinateImpl(40,20)), CoordinateImpl(40,20))
  }

  private def addTrees(): Unit ={
    for (x <- 0 until Settings.MAP_WIDTH)
      for (y <- 0 until Settings.MAP_HEIGHT)
        if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

    for (x <- 1 to 14)
      addTile(Tree(), CoordinateImpl(x,18))

    for (x <- 15 to 16)
      for(y <- 18 to 23)
      addTile(Tree(), CoordinateImpl(x,y))

    for (x <- 43 to 49)
      for (y <- 1 to 4)
        addTile(Tree(), CoordinateImpl(x,y))

    val trees = Seq[CoordinateImpl](CoordinateImpl(39,23), CoordinateImpl(47,23))
    addTileInMultipleCoordinates(Tree(), trees)
  }

  private def addLakes(): Unit ={
    addLake(CoordinateImpl(4,5), CoordinateImpl(20,10))
    addLake(CoordinateImpl(40,40), CoordinateImpl(48,48))
    addLake(CoordinateImpl(6,28), CoordinateImpl(15,30))
    addLake(CoordinateImpl(10,42), CoordinateImpl(11,44))
  }

  private def addTallGrass(): Unit ={
    for (x <- 6 to 15)
      addTile(TallGrass(), CoordinateImpl(x,31))

    for (y <- 28 to 31) {
      addTile(TallGrass(), CoordinateImpl(5,y))
      addTile(TallGrass(), CoordinateImpl(16,y))
    }

    for (x <- 1 to 20)
      for (y <- 1 to 4)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 3)
      for (y <- 5 to 10)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 9)
      for (y <- 19 to 23)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 8)
      for (y <- 24 to 27)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 39 to 48)
      for (y <- 1 to 10)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (y <- 1 to 8)
      addTile(TallGrass(), CoordinateImpl(38,y))

    for (x <- 36 to 37)
      for (y <- 1 to 5)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 42 to 48)
      for (y <- 11 to 13)
        addTile(TallGrass(), CoordinateImpl(x,y))
  }

}