package reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOutputService implements OutputService {
    private final String path;

    public FileOutputService(String path) throws IOException {
        this.path = path;
        Files.writeString(Path.of(path), "");
    }

    public synchronized void outputExtent(String title, Collection items) throws IOException {

        try (FileWriter writer = new FileWriter(
                path, StandardCharsets.UTF_8, true)) {

            writer.write("\n" + title + "\n-----------\n");

            for (Object item : items) {
                OutputServiceContributor contributor = (OutputServiceContributor) item;

                writer.write(contributor.getFormattedString());
                writer.write(System.lineSeparator());
            }
        }
    }

    public String getName() {
        return "File output service";
    }
}
