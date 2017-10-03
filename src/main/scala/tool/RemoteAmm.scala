package tool

import org.apache.sshd.server.session.ServerSession

/**
  *
  */
object RemoteAmm extends App {
  val x = 10
  var y = "aaaaaa"

  def dostart() = {
    import ammonite.sshd._
    import org.apache.sshd.server.auth.password.PasswordAuthenticator
    val replServer = new SshdRepl(
      SshServerConfig(
        address = "localhost", // or "0.0.0.0" for public-facing shells
        port = 22222, // Any available port
        passwordAuthenticator = Some(new PasswordAuthenticator {
          def authenticate(username: String, password: String, session: ServerSession): Boolean = {
            (username, password) == ("user", "pwd")
          }
        }) // or publicKeyAuthenticator
      ),
      predef = "repl.frontEnd() = ammonite.repl.FrontEnd.JLineUnix"
    )
    replServer.start()
  }

  dostart()

  Thread.currentThread().join()
}
