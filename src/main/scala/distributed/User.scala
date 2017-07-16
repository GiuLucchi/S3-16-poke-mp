package distributed

import java.lang.reflect.Type

import com.google.gson.{Gson, JsonDeserializationContext, JsonDeserializer, JsonElement}
import model.environment.{Coordinate, CoordinateImpl}

trait User {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate
}

object User {
  def apply(userId: Int, username: String, idImage: Int, position: Coordinate): User =
    new UserImpl(userId, username, idImage, position)
}

class UserImpl(override val userId: Int, override val username: String,
               override val idImage: Int, override val position: Coordinate) extends User


class UserDeserializer extends JsonDeserializer[UserImpl] {
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): UserImpl = {
    val jsonUser = json.getAsJsonObject
    val jsonPosition = jsonUser.getAsJsonObject("position")
    val position = CoordinateImpl(jsonPosition.get("x").getAsInt, jsonPosition.get("y").getAsInt)
    val user = new UserImpl(jsonUser.get("userId").getAsInt, jsonUser.get("username").getAsString, jsonUser.get("idImage").getAsInt, position)
    user
  }
}