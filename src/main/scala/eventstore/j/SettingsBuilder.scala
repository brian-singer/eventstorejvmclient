package eventstore
package j

import Settings.default
import java.net.InetSocketAddress
import scala.concurrent.duration._

class SettingsBuilder extends Builder[Settings] with ChainSet[SettingsBuilder] {
  var _address = default.address
  var _maxReconnections = default.maxReconnections
  var _reconnectionDelayMin = default.reconnectionDelayMin
  var _reconnectionDelayMax = default.reconnectionDelayMax
  var _defaultCredentials = default.defaultCredentials
  var _heartbeatInterval = default.heartbeatInterval
  var _heartbeatTimeout = default.heartbeatTimeout
  var _connectionTimeout = default.connectionTimeout
  var _operationTimeout = default.operationTimeout
  var _backpressure = default.backpressure

  def address(x: InetSocketAddress): SettingsBuilder = set {
    _address = x
  }

  def address(host: String): SettingsBuilder = address(new InetSocketAddress(host, default.address.getPort))

  def maxReconnections(x: Int): SettingsBuilder = set {
    _maxReconnections = x
  }

  def reconnectionDelayMin(x: FiniteDuration): SettingsBuilder = set {
    _reconnectionDelayMin = x
  }

  def reconnectionDelayMin(length: Long, unit: TimeUnit): SettingsBuilder = reconnectionDelayMin(FiniteDuration(length, unit))

  def reconnectionDelayMin(length: Long): SettingsBuilder = reconnectionDelayMin(length, SECONDS)

  def reconnectionDelayMax(x: FiniteDuration): SettingsBuilder = set {
    _reconnectionDelayMax = x
  }

  def reconnectionDelayMax(length: Long, unit: TimeUnit): SettingsBuilder = reconnectionDelayMax(FiniteDuration(length, unit))

  def reconnectionDelayMax(length: Long): SettingsBuilder = reconnectionDelayMax(length, SECONDS)

  def defaultCredentials(x: Option[UserCredentials]): SettingsBuilder = set {
    _defaultCredentials = x
  }

  def defaultCredentials(x: UserCredentials): SettingsBuilder = defaultCredentials(Option(x))

  def defaultCredentials(login: String, password: String): SettingsBuilder =
    defaultCredentials(UserCredentials(login = login, password = password))

  def noDefaultCredentials: SettingsBuilder = defaultCredentials(None)

  def heartbeatInterval(x: FiniteDuration): SettingsBuilder = set {
    _heartbeatInterval = x
  }

  def heartbeatInterval(length: Long, unit: TimeUnit): SettingsBuilder = heartbeatInterval(FiniteDuration(length, unit))

  def heartbeatInterval(length: Long): SettingsBuilder = heartbeatInterval(length, SECONDS)

  def heartbeatTimeout(x: FiniteDuration): SettingsBuilder = set {
    _heartbeatTimeout = x
  }

  def heartbeatTimeout(length: Long, unit: TimeUnit): SettingsBuilder = heartbeatTimeout(FiniteDuration(length, unit))

  def heartbeatTimeout(length: Long): SettingsBuilder = heartbeatTimeout(length, SECONDS)

  def connectionTimeout(x: FiniteDuration): SettingsBuilder = set {
    _connectionTimeout = x
  }

  def connectionTimeout(length: Long, unit: TimeUnit): SettingsBuilder = connectionTimeout(FiniteDuration(length, unit))

  def connectionTimeout(length: Long): SettingsBuilder = connectionTimeout(length, SECONDS)

  def operationTimeout(x: FiniteDuration): SettingsBuilder = set {
    _operationTimeout = x
  }

  def operationTimeout(length: Long, unit: TimeUnit): SettingsBuilder = operationTimeout(FiniteDuration(length, unit))

  def operationTimeout(length: Long): SettingsBuilder = operationTimeout(length, SECONDS)

  def backpressure(x: BackpressureSettings): SettingsBuilder = set {
    _backpressure = x
  }

  def build: Settings = Settings(
    address = _address,
    maxReconnections = _maxReconnections,
    reconnectionDelayMin = _reconnectionDelayMin,
    reconnectionDelayMax = _reconnectionDelayMax,
    defaultCredentials = _defaultCredentials,
    heartbeatInterval = _heartbeatInterval,
    heartbeatTimeout = _heartbeatTimeout,
    connectionTimeout = _connectionTimeout,
    backpressure = _backpressure)
}