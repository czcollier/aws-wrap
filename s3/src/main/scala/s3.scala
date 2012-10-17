package aws.s3

object S3 {

  val ACCESS_KEY_ID = ""
  val SECRET_ACCESS_KEY = ""

  object HTTPMethods extends Enumeration {
    type Method = Value
    val PUT, POST, DELETE, GET = Value
  }
  import HTTPMethods._

  object Parameters {
    import aws.core.AWS._

    def MD5(content: String) = ("Content-MD5" -> aws.core.utils.Crypto.base64(java.security.MessageDigest.getInstance("MD5").digest(content.getBytes)))

    object Permisions {

      object Grantees {
        sealed class Grantee(n: String, v: String) {
          val name = n
          val value = v
        }
        object Grantee {
          def apply(name: String, value: String) = new Grantee(name, value)
          def unapply(g: Grantee): Option[(String, String)] = Some((g.name, g.value))
        }
        case class Email(override val value: String) extends Grantee("emailAddress", value)
        case class Id(override val value: String) extends Grantee("id", value)
        case class Uri(override val value: String) extends Grantee("uri", value)
      }

      object ACLs {
        type ACL = String
        val PRIVATE: ACL = "private"
        val PUBLIC_READ: ACL = "public-read"
        val PUBLIC_READ_WRITE: ACL = "public-read-write"
        val AUTHENTICATED_READ: ACL = "authenticated-read"
        val BUCKET_OWNER_READ: ACL = "bucket-owner_read"
        val BUCKET_OWNER_FULL_CONTROL: ACL = "bucket-owner-full-control"
      }
      import ACLs._
      def X_AMZ_ACL(acl: ACL) = ("x-amz-acl" -> acl)

      import Grantees._
      private def s(gs: Seq[Grantee]) = gs.map { case Grantee(n, v) => """%s="%s"""".format(n, v) }.mkString(", ")

      type Grant = (String, String)
      def GRANT_READ(gs: Grantee*): Grant = "x-amz-grant-read" -> s(gs)
      def GRANT_WRITE(gs: Grantee*): Grant = "x-amz-grant-write" -> s(gs)
      def GRANT_READ_ACP(gs: Grantee*): Grant = "x-amz-grant-read-acp" -> s(gs)
      def GRANT_WRITE_ACP(gs: Grantee*): Grant = "x-amz-grant-write-acp" -> s(gs)
      def GRANT_FULL_CONTROL(gs: Grantee*): Grant = "x-amz-grant-full-control" -> s(gs)
    }
  }

}