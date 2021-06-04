package ir.unitycorn.app.roomlibrelated.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "library")
public class LibraryEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "library_name")
    private String libraryName;

    @NonNull
    @ColumnInfo(name = "library_url")
    private String libraryUrl;

    @NonNull
    @ColumnInfo(name = "library_license")
    private String libraryLicense;

    public LibraryEntity(String libraryName, String libraryUrl, String libraryLicense) {
        this.libraryName = libraryName;
        this.libraryUrl = libraryUrl;
        this.libraryLicense = libraryLicense;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryUrl() {
        return libraryUrl;
    }

    public String getLibraryLicense() {
        return libraryLicense;
    }
}
