package main.java.metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Service for writing metadata to JSON files.
 */
public class MetadataService {

    /**
     * Writes metadata to a JSON file in the specified directory.
     *
     * @param metadata The metadata to write
     * @param directory The directory where the metadata.json file should be created
     * @throws IOException If there is an error writing the file
     */
    public static void writeMetadata(Metadata metadata, File directory) throws IOException {
        // Ensure directory exists
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }

        File metadataFile = new File(directory, "metadata.json");
        try (FileWriter writer = new FileWriter(metadataFile)) {
            writer.write(metadata.toJson());
        }
    }
}