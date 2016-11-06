# JWTyped

[![Build Status](https://travis-ci.org/etaty/jwtyped.svg?branch=master)](https://travis-ci.org/etaty/jwtyped) [![codecov](https://codecov.io/gh/etaty/jwtyped/branch/master/graph/badge.svg)](https://codecov.io/gh/etaty/jwtyped) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.etaty/jwtyped_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.etaty/jwtyped_2.11)


Opinionated implementation of JWT in scala
* secure by default (only secure algorithm supported, no _none_)
* small api enforcing security
* typed
* no external dependencies

## Getting Started

Dependencies
```sbt
libraryDependencies += "com.github.etaty" %% "jwtyped" % "0.1.0"
```

## Usage
```scala
// encode
val key = Secret.fromString("secret")
val algorithm = HS256(key)
val header = Header("JWT", "HS256")
val payload = Payload(sub = "1234567890", name = "John Doe", admin = true)
val message = Message.from(header, payload)
val tokenEncoded = JWT.encode(message, algorithm)

// decode
JWT.decode[Header, Payload](tokenEncoded, algorithm)

// or
JWT.decode[Header, Payload](tokenEncoded, { 
  case (Header, Payload) =>
    // you can decide which algorithm you want to use
    Right(algorithm)
})


```

## Algorithms supported

[see implemation file](src/main/scala/jwtyped/Algorithm.scala)

* HmacSHA*
    * HS256
    * HS384
    * HS512
* SHA*withRSA
    * RS256
    * RS384
    * RS512
* SHA*withECDSA
    * ES256
    * ES384
    * ES512

### SHA*withECDSA with Bouncy Castle

Dependencies 
```sbt
libraryDependencies += "org.bouncycastle" % "bcpkix-jdk15on" % "1.55"
```

Add bouncy castle as a provider
```scala
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

val BOUNCY_CASTLE_PROVIDER = "BC"

if (Security.getProvider(BOUNCY_CASTLE_PROVIDER) == null) {
  Security.addProvider(new BouncyCastleProvider())
}
```