package distributed.server

import java.util
import java.util.concurrent.ConcurrentHashMap

import com.google.gson.reflect.TypeToken
import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.UserDeserializer
import utilities.Settings

object PlayerConnectionServerManager {
  def apply(): CommunicationManager = new PlayerConnectionServerManager()
}

class PlayerConnectionServerManager extends CommunicationManager {
  override def start(): Unit = {
    val channel: Channel = DistributedConnectionImpl().connection.createChannel
    channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

    val connectedUsers = new ConcurrentHashMap[Int, User]()

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received")
        val gson = new GsonBuilder().registerTypeAdapter(classOf[UserImpl], UserDeserializer).create()
        val message = gson.fromJson(new String(body, "UTF-8"), classOf[UserImpl])

        val response = gson.toJson(connectedUsers)
        channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + message.userId, null, response.getBytes("UTF-8"))
        println("server: send")
        connectedUsers.put(message.userId, message)
      }
    }

    channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)
  }
}
