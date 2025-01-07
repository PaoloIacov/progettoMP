import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.format(formatter)) // Scrive come stringa
        }
    }

    override fun read(input: JsonReader): LocalDateTime? {
        return if (input.peek() == com.google.gson.stream.JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            LocalDateTime.parse(input.nextString(), formatter) // Legge come stringa
        }
    }
}
