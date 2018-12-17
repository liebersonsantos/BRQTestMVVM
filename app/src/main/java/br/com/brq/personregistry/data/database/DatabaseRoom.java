package br.com.brq.personregistry.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import br.com.brq.personregistry.interfaces.PersonDAO;
import br.com.brq.personregistry.model.Person;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class DatabaseRoom extends RoomDatabase {

    public abstract PersonDAO personDAO();

    private static volatile DatabaseRoom INSTANCE;

    public static DatabaseRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseRoom.class, "personRegistry_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
