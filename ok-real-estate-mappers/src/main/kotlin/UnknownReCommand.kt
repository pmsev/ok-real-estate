import models.ReCommand

class UnknownReCommand(cmd: ReCommand) : Throwable("Wrong command $cmd at mapping toTransport stage")

