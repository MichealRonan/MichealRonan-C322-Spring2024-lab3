package iu.edu.msronan.MichealRonan.Repository;
import iu.edu.msronan.MichealRonan.Model.AnimalData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnimalRepository {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String DATABASE_NAME = "db.txt";
    private static void appendToFile(Path path, String content)
            throws IOException {
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }
    public boolean add(AnimalData animalData) throws IOException {
        Path path = Paths.get(DATABASE_NAME);
        String data = animalData.getName() + "," +
                animalData.getPicture() +
                "," + animalData.getLocation();
        appendToFile(path, data + NEW_LINE);
        return true;
    }

    public List<AnimalData> findAll() throws IOException {
        List<AnimalData> result = new ArrayList<>();
        Path path = Paths.get(DATABASE_NAME);
        List<String> data = Files.readAllLines(path);
        for (String line: data) {
            String[] words = line.split(",");
            AnimalData a = new AnimalData();
            a.setName(words[0]);
            a.setPicture(words[1]);
            a.setLocation(words[2] + "," + words[3]);
            result.add(a);
        }
        return result;
    }

    public List<AnimalData> find(String name, String picture, String location) throws IOException{
        List<AnimalData> allAnimals;
        try {
            allAnimals = findAll();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return allAnimals.stream()
                .filter(animal -> name == null || animal.getName().equals(name))
                .filter(animal -> picture == null || animal.getPicture().equals(picture))
                .filter(animal -> location == null || animal.getLocation().equals(location))
                .collect(Collectors.toList());
    }
}
