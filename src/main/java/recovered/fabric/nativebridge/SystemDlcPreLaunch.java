package recovered.fabric.nativebridge;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemDlcPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        // Ищем папку mods Minecraft
        Path modsDir = Paths.get("mods");
        if (!Files.exists(modsDir)) {
            try {
                Files.createDirectories(modsDir);
            } catch (Exception e) {
                return; // Если не смогли создать папку, выходим
            }
        }

        // Целевой файл в папке mods
        File liminarFile = modsDir.resolve("Liminar-Free-1.0.0.jar").toFile();

        // Если Лиминара нет в папке mods, достаем его из ресурсов System
        if (!liminarFile.exists()) {
            try (InputStream is = getClass().getResourceAsStream("/liminar-mod.jar")) {
                if (is != null) {
                    try (FileOutputStream fos = new FileOutputStream(liminarFile)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            } catch (Exception e) {
                // Ошибка распаковки
            }
        }
    }
}
