package ir.unitycorn.app.roomlibrelated.entities;

import android.app.Application;

import java.util.List;

import ir.unitycorn.app.roomlibrelated.ApplicationRoomDatabase;

public class LibraryRepository {

    private LibraryEntityDao libraryEntityDao;

    public LibraryRepository(Application application){
        ApplicationRoomDatabase applicationRoomDatabase =
                ApplicationRoomDatabase.getInstance(application);
        libraryEntityDao = applicationRoomDatabase.getLibraryEntityDao();
    }

    public String getAllLibrariesAsString(){
        StringBuilder stringBuilder = new StringBuilder("Open Source Libraries:\n\n\n");

        List<LibraryEntity> libraryEntities = libraryEntityDao.getLibraries();

        for (LibraryEntity libraryEntity : libraryEntities){
            stringBuilder.append(
                    libraryEntity.getLibraryName() + "\n" + libraryEntity.getLibraryUrl()
                    + "\n\n" + libraryEntity.getLibraryLicense()
            );


            //stringBuilder.append(libraryEntity.toString());
            stringBuilder.append("\n\n-----------------------------------------\n\n");
        }

        String stringBuilderToString = stringBuilder.toString();

        stringBuilderToString = stringBuilderToString.replaceAll("\\\\n", "\n");

        return stringBuilderToString;
    }

}
