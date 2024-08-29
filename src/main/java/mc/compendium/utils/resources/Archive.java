package mc.compendium.utils.resources;

import mc.compendium.utils.Arrays;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Archive extends ArchiveDirectory {

    private ZipFile zip;

    //

    public Archive(String path) throws IOException, ArchiveFileException {
        this(new ZipFile(path));
    }

    public Archive(ZipFile zip) throws ArchiveFileException, IOException {
        super(null, null, null);
        this.zip = zip;

        this.update();
    }

    //

    @Override
    public String name() {
        return ArchiveFile.name(this);
    }

    //

    public void update() throws ArchiveFileException, IOException {
        this.zip = new ZipFile(this.path());

        this.lock(false);

        //

        ArchiveDirectory directory;

        ArchiveFile file;
        String filepath;
        String filename;
        String[] filepathFragments;

        Enumeration<? extends ZipEntry> entries = this.zip().entries();

        ZipEntry entry;
        while(entries.hasMoreElements()) {
            entry = entries.nextElement();

            filepath = entry.getName();
            if(filepath.endsWith(ArchiveFile.PATH_SEPARATOR))
                filepath = filepath.substring(0, filepath.length()-ArchiveFile.PATH_SEPARATOR.length());

            filepathFragments = filepath.split(ArchiveFile.PATH_SEPARATOR);
            filename = filepathFragments[filepathFragments.length-1];

            directory = this;
            for(int i = 0; i < filepathFragments.length-1; ++i) {
                ArchiveFile pathSearchingFileDirectory = directory.get(filepathFragments[i]);

                if(pathSearchingFileDirectory == null) {
                    pathSearchingFileDirectory = new ArchiveDirectory(this, null, Arrays.join(filepathFragments, ArchiveFile.PATH_SEPARATOR, 0, i+1));
                    directory.add(pathSearchingFileDirectory);
                }

                if(pathSearchingFileDirectory.isDir())
                    directory = pathSearchingFileDirectory.asDir();
            }

            file = directory.get(filename);

            if(file == null) {
                file = entry.isDirectory() ? new ArchiveDirectory(this, entry) : new ArchiveFile(this, entry);
                directory.add(file);
            }
            else {
                file.zipEntry(entry);
                file.path(filepath);
            }
        }

        //

        this.lock(true);
    }

    //

    public ZipFile zip() { return this.zip; }

    @Override
    public String path() { return this.zip().getName(); }

}