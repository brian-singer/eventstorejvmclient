package eventstore
package proto

import akka.util.{ ByteStringBuilder, ByteIterator }
import com.google.protobuf.{ MessageLite, ByteString => ProtoByteString }
import net.sandrogrzicic.scalabuff.MessageBuilder
import scala.language.reflectiveCalls
import scala.util.{ Failure, Try }
import util.{ BytesWriter, BytesReader, DefaultFormats }

object DefaultProtoFormats extends DefaultProtoFormats

trait DefaultProtoFormats extends DefaultFormats {

  type Message[T] = MessageBuilder[T]

  type MessageProvider[T] = {
    def defaultInstance: T
  }

  trait ProtoReader[T, P <: Message[P]] extends BytesReader[T] {
    def provider: MessageProvider[P]

    def fromProto(x: P): T

    def read(bi: ByteIterator): T = fromProto(provider.defaultInstance.mergeFrom(bi.toArray[Byte]))

    def byteString(bs: ProtoByteString): ByteString = ByteString(bs.asReadOnlyByteBuffer())

    def byteString(bs: Option[ProtoByteString]): ByteString = bs.fold(ByteString.empty)(x => ByteString(x.asReadOnlyByteBuffer()))

    def uuid(bs: ProtoByteString): Uuid = BytesReader[Uuid].read(byteString(bs))

    def message(x: Option[String]): Option[String] = x match {
      case Some("")                  => None
      case Some(s) if s.trim.isEmpty => None
      case _                         => x
    }
  }

  trait ProtoWriter[T] extends BytesWriter[T] {
    def toProto(x: T): MessageLite

    def write(x: T, builder: ByteStringBuilder) {
      builder.putBytes(toProto(x).toByteArray)
    }

    def protoByteString(bs: ByteString) = ProtoByteString.copyFrom(bs.toByteBuffer)

    def protoByteString(uuid: Uuid) = ProtoByteString.copyFrom(BytesWriter[Uuid].toByteString(uuid).toByteBuffer)

    def protoByteStringOption(bs: ByteString) = if (bs.isEmpty) None else Some(protoByteString(bs))
  }

}