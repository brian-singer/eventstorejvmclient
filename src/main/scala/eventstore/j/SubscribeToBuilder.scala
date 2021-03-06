package eventstore
package j

import Builder._

class SubscribeToBuilder extends Builder[SubscribeTo] with ResolveLinkTosSnippet[SubscribeToBuilder] {
  var _stream: EventStream = null

  def toAll(): SubscribeToBuilder = set {
    _stream = EventStream.All
  }

  def toStream(streamId: String): SubscribeToBuilder = set {
    _stream = EventStream(streamId)
  }

  def resolveLinkTos(x: Boolean): SubscribeToBuilder = ResolveLinkTosSnippet.resolveLinkTos(x)

  def build: SubscribeTo = SubscribeTo(
    stream = _stream,
    resolveLinkTos = ResolveLinkTosSnippet.value)
}