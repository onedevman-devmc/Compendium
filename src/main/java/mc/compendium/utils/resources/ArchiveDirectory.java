package mc.compendium.utils.resources;

import mc.compendium.utils.Arrays;
import mc.compendium.utils.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

public class ArchiveDirectory extends ArchiveFile {

    private static int indexOfFile(List<ArchiveFile> files, String filename) {
        int fileCount = files.size();
        boolean found = false;

        int i;
        for(i = 0; i < fileCount && !(found = files.get(i).name().equals(filename)); ++i);
        return found ? i : -1;
    }

    public static String toString(ArchiveDirectory directory) {
        StringBuilder result = new StringBuilder(ArchiveFile.toString(directory) + " :\n");

        ArchiveFile file;

        int fileCount = directory.count();
        for(int i = 0; i < fileCount; ++i) {
            file = directory.get(i);
            result.append("    ").append((file.isDir() ? file.asDir() : file).toString().replaceAll("\n", "\n    ")).append("\n");
        }

        result = new StringBuilder(result.substring(0, result.length() - ArchiveFile.PATH_SEPARATOR.length()));

        return result.toString();
    }

    //

    private List<ArchiveFile> files = new ArrayList<>();

    //

    public ArchiveDirectory(Archive archive, ZipEntry entry) throws ArchiveFileException {
        this(archive, entry, entry.getName());
    }

    public ArchiveDirectory(Archive archive, ZipEntry entry, String dirpath) throws ArchiveFileException {
        super(archive, entry, dirpath, true);
    }

    //

    public List<ArchiveFile> files() {
        return this.files;
    }

    public void add(ArchiveFile file) throws ArchiveFileException {
        if(this.locked()) throw new ArchiveFileException("Directory locked.");
        this.files().add(file);
    }

    //

    public int count() {
        return this.files().size();
    }

    //

    public ArchiveFile get(int fileindex) {
        return this.files().get(fileindex);
    }

    public ArchiveFile get(String filepath) {
        if(filepath.startsWith(ArchiveFile.PATH_SEPARATOR))
            filepath = filepath.substring(ArchiveFile.PATH_SEPARATOR.length());

        if(filepath.endsWith(ArchiveFile.PATH_SEPARATOR))
            filepath = filepath.substring(0, filepath.length()-ArchiveFile.PATH_SEPARATOR.length());

        String[] pathFragments = filepath.split(ArchiveFile.PATH_SEPARATOR);
        int fileIndex = indexOfFile(this.files(), pathFragments[0]);

        ArchiveFile file;

        if(fileIndex > -1) {
            file = this.get(fileIndex);

            if(pathFragments.length == 1) return file;
            if(!file.isDir()) return null;

            return file.asDir().get(Arrays.join(pathFragments, ArchiveFile.PATH_SEPARATOR, 1));
        }

        return null;
    }

    //

    public ArchiveFile remove(int fileindex) throws ArchiveFileException {
        if(this.locked())
            throw new ArchiveFileException("Directory locked.");

        return this.files().remove(fileindex);
    }

    public ArchiveFile remove(String filepath) throws ArchiveFileException {
        if(this.locked()) throw new ArchiveFileException("Directory locked.");

        if(filepath.startsWith(ArchiveFile.PATH_SEPARATOR))
            filepath = filepath.substring(ArchiveFile.PATH_SEPARATOR.length());

        if(filepath.endsWith(ArchiveFile.PATH_SEPARATOR))
            filepath = filepath.substring(0, filepath.length()-ArchiveFile.PATH_SEPARATOR.length());

        int fileIndex = indexOfFile(this.files(), filepath);
        if(fileIndex > 0) return this.remove(fileIndex);

        return null;
    }

    //

    public boolean has(String filename) {
        return indexOfFile(this.files(), filename) > -1;
    }

    //

    public void extract(String extractionFilepath) throws IOException {
        String[] extractedFilepathFragments = extractionFilepath.split(PATH_SEPARATOR);
        this.extract(
            extractedFilepathFragments[extractedFilepathFragments.length-1],
            Arrays.join(extractedFilepathFragments, PATH_SEPARATOR, 0, extractedFilepathFragments.length - 1)
        );
    }

    @Override
    public void extract(String extractionFilename, String extractionDirectorypath) throws IOException {
        Path extractionFilepathPath = Paths.get(extractionDirectorypath, extractionFilename);
        if(!Files.exists(extractionFilepathPath))
            Files.createDirectories(extractionFilepathPath);

        int fileCount = this.count();

        ArchiveFile file;
        for(int i = 0; i < fileCount; ++i) {
            file = this.get(i);
            file.extract(file.name(), extractionFilepathPath.toString());
        }
    }

    //

    public void lock(boolean state, boolean recursively) {
        super.lock(state);

        boolean isFilesListModifiable = Lists.isModifiable(this.files());
        if(this.locked()) {
            if (isFilesListModifiable)
                this.files = Collections.unmodifiableList(this.files());
        }
        else this.files = new ArrayList<>(this.files());

        if(recursively) {
            this.files().forEach(file -> {
                if(file.isDir())
                    file.asDir().lock(state, true);
                else
                    file.lock(state);
            });
        }
    }

    //

    @Override
    public String toString() {
        return ArchiveDirectory.toString(this);
    }

}
