package com.iskportal.molekule.core.format;

import com.iskportal.molekule.core.geomertry.Triangle
import com.iskportal.molekule.core.geomertry.point.Point
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

actual class STL {

    /**
     * Parses an STL file, attempting to automatically detect whether the file
     * is an ASCII or binary STL file
     *
     * @param filepath The file to parse
     * @return A list of triangles representing all of the triangles in the STL
     * file.
     * @throws IOException              Thrown if there was a problem reading the file
     * (typically means the file does not exist or is not a file).
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    @Throws(IOException::class, IllegalArgumentException::class)
    actual fun parseSTLData(byteArray: ByteArray): List<Triangle> {

        // determine if it is ASCII or binary STL

        //some binary STL files has "solid" in the first 80 chars
        //this breaks logic that determines if a file is ascii based on it
        //simply beginning with "solid"
        var isASCIISTL = false

        //read the first 512 chars or less
        val buf = readblock(byteArray, 0, 512)
        var sb = StringBuffer()
        var inl = readline(buf, sb, 0)
        var line = sb.toString()
        var st = StringTokenizer(line)
        var token = st.nextToken()
        if (token == "solid") { //start with "solid"
            if (inl > -1) { //found new line for next line
                sb = StringBuffer()
                inl = readline(buf, sb, inl + 1) //read next line
                line = sb.toString()
                st = StringTokenizer(line)
                token = st.nextToken()
                if (token == "endsolid") isASCIISTL = true //empty ascii file
                else if (token == "facet") {
                    isASCIISTL = true //ascii file
                } else if (isbinaryfile(byteArray)) isASCIISTL = false //binary file
            } else { //no linefeed
                if (isbinaryfile(byteArray)) isASCIISTL = false //binary file
            }
        } else { //does not starts with "solid"
            if (isbinaryfile(byteArray)) isASCIISTL = false //binary file
        }

        // read file to array of triangles
        val mesh: List<Triangle>
        if (isASCIISTL) {
            val charset = Charset.forName("UTF-8")
            mesh = readASCII(charset.decode(ByteBuffer.wrap(byteArray)).toString().lowercase(Locale.getDefault()))
        } else {
            mesh = readBinary(byteArray)
        }
        return mesh
    }

    fun readblock(allBytes: ByteArray, offset: Int, length: Int): String {
        var length = length
        if (allBytes.size - offset < length) length = allBytes.size - offset
        val charset = Charset.forName("UTF-8")
        val decode = charset.decode(ByteBuffer.wrap(allBytes, offset, length))
        return decode.toString().lowercase(Locale.getDefault())
    }

    fun readline(buf: String, sb: StringBuffer, offset: Int): Int {
        val il = buf.indexOf('\n', offset)
        if (il > -1) sb.append(buf.substring(offset, il - 1))
        else sb.append(buf.substring(offset))
        return il
    }

    fun lastline(buf: String): String {
        var i = buf.length
        while (--i > -1) {
            if (buf[i] == '\n') break
        }
        return if (i > -1) buf.substring(i + 1)
        else ""
    }

    @Throws(IllegalArgumentException::class)
    fun isbinaryfile(allBytes: ByteArray): Boolean {
        require(allBytes.size >= 84) { "invalid binary file, length<84" }
        val numtriangles = byteatoint(Arrays.copyOfRange(allBytes, 80, 84))
        if (allBytes.size >= 84 + numtriangles * 50) return true //is binary file
        else {
            val msg = "invalid binary file, num triangles does not match length specs"
            throw IllegalArgumentException(msg)
        }
    }

    // little endian
    fun byteatoint(bytes: ByteArray): Int {
        assert(bytes.size == 4)
        var r = 0
        r = bytes[0].toInt() and 0xff
        r = r or ((bytes[1].toInt() and 0xff) shl 8)
        r = r or ((bytes[2].toInt() and 0xff) shl 16)
        r = r or ((bytes[3].toInt() and 0xff) shl 24)
        return r
    }

    fun inttobytea(value: Int): ByteArray {
        val bytes = ByteArray(4)
        bytes[0] = value.toByte()
        bytes[1] = (value shr 8).toByte()
        bytes[2] = (value shr 16).toByte()
        bytes[3] = (value shr 24).toByte()
        return bytes
    }

    /**
     * Parses binary STL file content provided as a byte array
     *
     * @param allBytes binary STL
     * @return A list of triangles representing all of the triangles in the STL
     * file.
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    @Throws(java.lang.IllegalArgumentException::class)
    fun readBinary(allBytes: ByteArray?): List<Triangle> {
        Logger.getLogger(STL::class.java.name).log(Level.FINEST, "Parsing binary STL format")
        val `in` = DataInputStream(ByteArrayInputStream(allBytes))
        val triangles = ArrayList<Triangle>()
        try {
            // skip the header
            val header = ByteArray(80)
            `in`.read(header)
            // get number triangles (not really needed)
            // WARNING: STL FILES ARE SMALL-ENDIAN
            val numberTriangles = Integer.reverseBytes(`in`.readInt())
            triangles.ensureCapacity(numberTriangles)
            // read triangles
            try {
                while (`in`.available() > 0) {
                    val nvec = FloatArray(3)
                    for (i in nvec.indices) {
                        nvec[i] = java.lang.Float.intBitsToFloat(Integer.reverseBytes(`in`.readInt()))
                    }
                    val normal = Point(nvec[0].toDouble(), nvec[1].toDouble(), nvec[2].toDouble()) // not used (yet)
                    val vertices = arrayOfNulls<Point>(3)
                    for (v in vertices.indices) {
                        val vals = FloatArray(3)
                        for (d in vals.indices) {
                            vals[d] = java.lang.Float.intBitsToFloat(Integer.reverseBytes(`in`.readInt()))
                        }
                        vertices[v] = Point(vals[0].toDouble(), vals[1].toDouble(), vals[2].toDouble())
                    }
                    val attribute = java.lang.Short.reverseBytes(`in`.readShort()) // not used (yet)
                    triangles.add(
                        Triangle(
                            vertices[0]!!,
                            vertices[1]!!, vertices[2]!!
                        )
                    )
                }
            } catch (ex: Exception) {
                throw java.lang.IllegalArgumentException(
                    "Malformed STL binary at triangle number " + (triangles.size + 1),
                    ex
                )
            }
        } catch (ex: IOException) {
            // IO exceptions are impossible with byte array input streams,
            // but still need to be caught
            Logger.getLogger(STL::class.java.name)
                .log(Level.SEVERE, "HOLY SHIT! A ByteArrayInputStream threw an exception!", ex)
        }
        return triangles
    }


    /**
     * Reads an STL ASCII file content provided as a String
     *
     * @param content ASCII STL
     * @return A list of triangles representing all of the triangles in the STL
     * file.
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    @Throws(java.lang.IllegalArgumentException::class)
    fun readASCII(content: String): List<Triangle> {
        Logger.getLogger(STL::class.java.name).log(Level.FINEST, "Parsing ASCII STL format")
        // string is lowercase
        val triangles = ArrayList<Triangle>()

        var position = 0
        while ((position < content.length) and (position >= 0)) {
            position = content.indexOf("facet", position)
            if (position < 0) {
                break
            }
            try {
                val vertices = arrayOfNulls<Point>(3)
                for (v in vertices.indices) {
                    position = content.indexOf("vertex", position) + "vertex".length
                    while (Character.isWhitespace(content[position])) {
                        position++
                    }
                    var nextSpace: Int
                    val vals = DoubleArray(3)
                    for (d in vals.indices) {
                        nextSpace = position + 1
                        while (!Character.isWhitespace(content[nextSpace])) {
                            nextSpace++
                        }
                        val value = content.substring(position, nextSpace)
                        vals[d] = value.toDouble()
                        position = nextSpace
                        while (Character.isWhitespace(content[position])) {
                            position++
                        }
                    }
                    vertices[v] = Point(vals[0], vals[1], vals[2])
                }
                position = content.indexOf("endfacet", position) + "endfacet".length
                triangles.add(
                    Triangle(
                        vertices[0]!!,
                        vertices[1]!!, vertices[2]!!
                    )
                )
            } catch (ex: java.lang.Exception) {
                var back = position - 128
                if (back < 0) {
                    back = 0
                }
                var forward = position + 128
                if (position > content.length) {
                    forward = content.length
                }
                throw java.lang.IllegalArgumentException(
                    "Malformed STL syntax near \"" + content.substring(
                        back,
                        forward
                    ) + "\"", ex
                )
            }
        }

        return triangles
    }

}