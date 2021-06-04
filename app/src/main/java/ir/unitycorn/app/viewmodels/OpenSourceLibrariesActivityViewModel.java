package ir.unitycorn.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import ir.unitycorn.app.roomlibrelated.entities.LibraryRepository;

public class OpenSourceLibrariesActivityViewModel extends AndroidViewModel {

    private LibraryRepository libraryRepository;

    public OpenSourceLibrariesActivityViewModel(@NonNull Application application) {
        super(application);

        libraryRepository = new LibraryRepository(application);
    }

    public String getLibraries(){
        return libraryRepository.getAllLibrariesAsString();
    }
}
