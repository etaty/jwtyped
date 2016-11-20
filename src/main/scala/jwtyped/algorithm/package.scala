package jwtyped

import java.security.{PrivateKey, PublicKey}

package object algorithm {

  def RS256(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA256withRSA", publicKey, privateKey)

  def RS384(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA384withRSA", publicKey, privateKey)

  def RS512(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA512withRSA", publicKey, privateKey)

  def ES256(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA256withECDSA", publicKey, privateKey)

  def ES384(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA384withECDSA", publicKey, privateKey)

  def ES512(publicKey: PublicKey, privateKey: PrivateKey): AlgoPublicPrivateKey = AlgoPublicPrivateKey("SHA512withECDSA", publicKey, privateKey)

  def HS256(secret: Secret): AlgoSecretKey = AlgoSecretKey("HmacSHA256", secret)

  def HS384(secret: Secret): AlgoSecretKey = AlgoSecretKey("HmacSHA384", secret)

  def HS512(secret: Secret): AlgoSecretKey = AlgoSecretKey("HmacSHA512", secret)
}
