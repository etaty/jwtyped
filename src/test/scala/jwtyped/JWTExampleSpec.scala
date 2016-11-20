package jwtyped

import java.security.{KeyPairGenerator, Security}

import jwtyped.algorithm._
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.specs2.ScalaCheck
import org.specs2.mutable.SpecificationLike


class JWTExampleSpec
  extends SpecificationLike
    with ScalaCheck {

  import JWTCirceSupport._

  "JWT" should {
    val payload = Payload(sub = "1234567890", name = "John Doe", admin = true)

    "HS256" in {
      val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
      val key = Secret.fromString("secret")
      val header = Header("JWT", "HS256")

      "decode" in {
        val r = JWT.decode[Header, Payload](token, HS256(key))
        r.isRight must beTrue
        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }

      "encode" in {
        val algorithm = HS256(key)
        val message = Message.from(header, payload)

        val tokenEncoded = JWT.encode(message, algorithm)

        val r = JWT.decode[Header, Payload](tokenEncoded) {
          case (header: Header, payload: Payload) =>
            Right(algorithm)
        }
        r.isRight must beTrue

        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }

      "encode with URLEncodedSecret (Auth0)" in {
        Base64Helper.encode("secret".getBytes) mustEqual "c2VjcmV0"

        val secret = Secret.from64UrlEncoded("c2VjcmV0")
        val algorithm = HS256(secret)
        val message = Message.from(header, payload)

        val tokenEncoded = JWT.encode(message, algorithm)

        val r = JWT.decode[Header, Payload](tokenEncoded) {
          case (header, payload) =>
            Right(algorithm)
        }
        r.isRight must beTrue

        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }
    }

    "RS256" in {
      val token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.EkN-DOsnsuRjRO6BxXemmJDm3HbxrbRzXglbN2S4sOkopdU4IsDxTI8jO19W_A4K8ZPJijNLis4EZsHeY559a4DFOd50_OqgHGuERTqYZyuhtF39yxJPAjUESwxk2J5k_4zM3O-vtd1Ghyo4IbqKKSy6J9mTniYJPenn5-HIirE"

      val publicKey =
        """-----BEGIN PUBLIC KEY-----
          |MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdlatRjRjogo3WojgGHFHYLugdUWAY9iR3fy4arWNA1KoS8kVw33cJibXr8bvwUAUparCwlvdbH6dvEOfou0/gCFQsHUfQrSDv+MuSUMAe8jzKE4qW+jK+xQU9a03GUnKHkkle+Q0pX/g6jXZ7r1/xAK5Do2kQ+X5xK9cipRgEKwIDAQAB
          |-----END PUBLIC KEY-----  """.stripMargin
      val privateKey =
        """-----BEGIN PRIVATE KEY-----
          |MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAN2Vq1GNGOiCjdai
          |OAYcUdgu6B1RYBj2JHd/LhqtY0DUqhLyRXDfdwmJtevxu/BQBSlqsLCW91sfp28Q
          |5+i7T+AIVCwdR9CtIO/4y5JQwB7yPMoTipb6Mr7FBT1rTcZScoeSSV75DSlf+DqN
          |dnuvX/EArkOjaRD5fnEr1yKlGAQrAgMBAAECgYAP6icC1XJ7iJztVHtXnQMX1s6f
          |BHA1esBFwOGr0tY1GtEYSURJhhNDoRtq1dLcwLKONfZ4yG79oXliFcHCSmnDGD4Q
          |6Zd6H4zMvtmHbzg649uSCUBoaFX1tvr/kKm4ZvV6zI9thb5PLY8LQZJl8heRezvq
          |NcC71M4G8PTJtzZiWQJBAPLCDZH/u3dDY4Tb7KjfzYIsl2uVItVE5YrBvi1vY+OF
          |jhcDBXx3W/LRF6fFMH4rky7nu5VJMe2swrQYC0WvzfcCQQDpq/HbQ4ejrVh6Vr/i
          |TmRAO2MR/U3Qt8j4FxZm3GxLeMMzeLUenQpK7muwI9vxpwmjlE7bqAZWt8CwNwhc
          |oL5tAkBlnZcimyK0vI+m6Iw68FvM9q93iBjpnwpat9jMmgj9D0W4GjqsrCXgEWhO
          |gPtYhRL6GmRqDBaLP7rMuhfV1s5nAkAKYfl9JKMCQtGLng8onxMVR44/XmH2xDPJ
          |0jzMlT66m8MQpmxlz9SFP9LJIM7FDgb/nbdjSzP85m1JZyiX9QyBAkAt6zL74Z2+
          |ggMt8lfkYFCm4WAV6dGJENpvGsT0w1RnZo8VoWn4PIB75vicPC4lpbYtRFuvwARm
          |BruhCPS5jFxj
          |-----END PRIVATE KEY-----""".stripMargin
      val header = Header("JWT", "RS256")

      "decode" in {
        val rs256 = RS256(PemKeyUtil.decodePublicKey(publicKey), PemKeyUtil.decodePrivateKey(privateKey))
        val r = JWT.decode[Header, Payload](token, rs256)
        r.isRight must beTrue
        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }

      "encode" in {
        val rs256 = RS256(PemKeyUtil.decodePublicKey(publicKey), PemKeyUtil.decodePrivateKey(privateKey))
        val message = Message.from(header, payload)

        val tokenEncoded = JWT.encode(message, rs256)

        val r = JWT.decode[Header, Payload](tokenEncoded) {
          case (header, payload) =>
            Right(rs256)
        }
        r.isRight must beTrue

        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }
    }

    "ES256" in {
      val header = Header("JWT", "ES256")

      "decode" in todo

      "encode" in {

        val BOUNCY_CASTLE_PROVIDER = "BC"

        if (Security.getProvider(BOUNCY_CASTLE_PROVIDER) == null) {
          Security.addProvider(new BouncyCastleProvider())
        }

        val keyPair = KeyPairGenerator.getInstance("ECDSA", "BC").generateKeyPair()

        val es256 = ES256(keyPair.getPublic, keyPair.getPrivate)

        val message = Message.from(header, payload)

        val tokenEncoded = JWT.encode(message, es256)

        val r = JWT.decode[Header, Payload](tokenEncoded) {
          case (header, payload) =>
            Right(es256)
        }
        r.isRight must beTrue

        r.right.map { token =>
          token.header === header
          token.payload === payload
        }
        ok
      }
    }
  }
}
