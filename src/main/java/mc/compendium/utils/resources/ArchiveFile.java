package mc.compendium.utils.resources;

import mc.compendium.utils.Arrays;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;

public class ArchiveFile {

    public static final String PATH_SEPARATOR = "/";

    public static String name(ArchiveFile file) {
        return file.path().substring(file.path().lastIndexOf(PATH_SEPARATOR)+1);
    }

    public static String toString(ArchiveFile file) {
        return "("+(file.isDir() ? "directory" : "file")+") "+file.name()+" ["+file.path()+"]";
    }

    //

    private final Archive archive;

    private ZipEntry zipEntry;
    private String path;

    private boolean isDir = false;

    private boolean locked = false;

    //

    public ArchiveFile(Archive archive, ZipEntry entry) throws ArchiveFileException {
        this(archive, entry, entry.getName());
    }

    public ArchiveFile(Archive archive, ZipEntry entry, String filepath) throws ArchiveFileException {
        this(archive, entry, filepath, false);
    }

    public ArchiveFile(Archive archive, ZipEntry entry, String filepath, boolean isDirectory) throws ArchiveFileException {
        this.archive = archive;

        this.zipEntry(entry);
        this.path(filepath);

        this.isDir = isDirectory;
    }

    //

    public Archive archive() { return this.archive; }

    public ZipEntry zipEntry() { return this.zipEntry; }
    public ZipEntry zipEntry(ZipEntry entry) throws ArchiveFileException {
        if(this.locked())
            throw new ArchiveFileException((this.isDir() ? "Directory" : "File")+" locked.");

        return this.zipEntry = entry;
    }

    public String path() { return this.path; }
    public String path(String path) throws ArchiveFileException {
        if(this.locked())
            throw new ArchiveFileException((this.isDir() ? "Directory" : "File")+" locked.");

        return this.path = path;
    }

    public String name() {
        return ArchiveFile.name(this);
    }

    //

    public boolean isDir() { return this.isDir; }

    public ArchiveDirectory asDir() { return this.isDir() ? (ArchiveDirectory) this : null; }

    //

    public InputStream content() throws IOException {
        if(this.isDir())
            return null;

        return this.archive().zip().getInputStream(this.zipEntry());
    }

    //

    public void extract(String extractionFilepath) throws IOException {
        String[] extractedFilepathFragments = extractionFilepath.split(PATH_SEPARATOR);
        this.extract(
            extractedFilepathFragments[extractedFilepathFragments.length-1],
            Arrays.join(extractedFilepathFragments, PATH_SEPARATOR, 0, extractedFilepathFragments.length - 1)
        );
    }

    public void extract(String extractionFilename, String extractionDirectorypath) throws IOException {
        Path extractionDirectorypathPath = Path.of(extractionDirectorypath);
        if(!Files.exists(extractionDirectorypathPath))
            Files.createDirectories(extractionDirectorypathPath);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(Paths.get(extractionDirectorypath, extractionFilename).toString());
            InputStream content = this.content();

            byte[] buf = new byte[1024];
            int length;

            while ((length = content.read(buf, 0, buf.length)) >= 0)
                output.write(buf, 0, length);

            output.close();
        } catch (IOException ioex) {
            if(output != null)
                output.close();
        }
    }

    //

    public boolean locked() { return this.locked; }

    public void lock(boolean state) { this.locked = state; }

    //

    @Override
    public String toString() {
        return ArchiveFile.toString(this);
    }
}
