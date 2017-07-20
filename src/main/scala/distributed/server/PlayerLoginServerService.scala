package distributed.server

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.google.gson.reflect.TypeToken
import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.PlayerMessageDeserializer
import distributed.messages.{ConnectedPlayersMessage, PlayerMessage, PlayerMessageImpl}
import utilities.Settings

object PlayerLoginServerService {
  def apply(connection: Connection, connectedUsers: ConcurrentMap[Int, User]): CommunicationService = new PlayerLoginServerService(connection, connectedUsers)
}

class PlayerLoginServerService(private val connection: Connection, private val connectedUsers: ConcurrentMap[Int, User]) extends CommunicationService {
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received new user")
        println(new String(body, "UTF-8"))
        val gson = new GsonBuilder().registerTypeAdapter(classOf[PlayerMessageImpl], PlayerMessageDeserializer).create()
        val userMessage = gson.fromJson(new String(body, "UTF-8"), classOf[PlayerMessageImpl])
        val user = userMessage.player

        val response = gson.toJson(ConnectedPlayersMessage(connectedUsers))
        channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + user.userId, null, response.getBytes("UTF-8"))
        println("server: send connected user")
        connectedUsers.put(user.userId, user)

        channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
        val newPlayerResponse = gson.toJson(userMessage)
        channel.basicPublish(Settings.NEW_PLAYER_EXCHANGE, "", null, newPlayerResponse.getBytes("UTF-8"))
        println("server: send new user")
      }
    }

    channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)
  }

}
