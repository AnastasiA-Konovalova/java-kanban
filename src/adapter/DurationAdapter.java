package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {


    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(duration.toSeconds());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofSeconds(jsonReader.nextLong());
    }
}

//    public Duration read(JsonReader in) throws IOException {
//        if (in.peek() != JsonReader.Token.NULL) {
//            // Читаем значение и создаем Duration из миллисекунд
//            return Duration.ofMillis(in.nextLong());
//        } else {
//            in.nextNull();
//            return null;
//        }
