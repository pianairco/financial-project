package ir.piana.financial.commons.utilities;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlConfigUtility {
    public static <T> T load(String filePath, Class<T> target) {
        Yaml yaml = new Yaml();
        try (InputStream in = target.getClassLoader().getResourceAsStream(filePath)) {
            return yaml.loadAs(in, target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
}
