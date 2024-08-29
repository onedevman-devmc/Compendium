package mc.compendium.utils.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.CodeSource;

public class ResourcesUtil {

    private static Archive currentArchive = null;

    public static Archive currentArchive() throws IOException, ArchiveFileException, URISyntaxException {
        if(ResourcesUtil.currentArchive == null) {
            CodeSource src = ResourcesUtil.class.getProtectionDomain().getCodeSource();

            if (src == null)
                return null;

            FileSystem zipfs = FileSystems.newFileSystem(Path.of(src.getLocation().getPath()));

//            ResourcesUtil.currentArchive = new Archive(src.getLocation().getPath());
        }

        return ResourcesUtil.currentArchive;
    }

}
