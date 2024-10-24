package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        if (instant == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(instant.toString());
        }
    }

    @Override
    public Instant read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String instantString = jsonReader.nextString(); // Чтение строки
        return Instant.parse(instantString); // Преобразование строки в Instant
    }
}

//    @Override
//    public Instant read(JsonReader in) throws IOException {
//        if (in.peek() == JsonToken.NULL) {
//            in.nextNull();
//            return null;
//        }
//        return new Instant(in.nextString());
//    }